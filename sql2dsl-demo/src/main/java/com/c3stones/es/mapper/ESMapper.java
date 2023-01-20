package com.c3stones.es.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static cn.hutool.core.text.CharSequenceUtil.containsAny;
import static cn.hutool.core.text.CharSequenceUtil.equalsIgnoreCase;
import static cn.hutool.core.util.ArrayUtil.isEmpty;
import static com.c3stones.es.constants.Constant.*;

/**
 * Elasticseach 通用 Mapper
 *
 * @author CL
 */
@Component
public class ESMapper {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 集合
     *
     * @param provider Elaticsearch Mapper Provider
     * @return {@link List}
     * @throws IOException 查询异常
     */
    public List<Map<String, Object>> aggregation(ESMapperProvider provider) throws IOException {
        List<Map<String, Object>> result = CollUtil.newArrayList();
        // 聚合查询时，设置form和size为0
        ESMapperProvider.DslModel dsl = provider.getDsl();
        dsl.setFrom(0);
        dsl.setSize(0);
        SearchResponse response = execute(provider);
        List<Aggregation> aggregationList = Opt.ofNullable(response)
                .map(SearchResponse::getAggregations)
                .map(Aggregations::asList).orElse(ListUtil.empty());
        aggregationList.forEach(aggregation -> {
            List<Map<String, Object>> tmp = aggregationResult(aggregation);
            if (CollUtil.isNotEmpty(tmp)) result.addAll(tmp);
        });
        return result;
    }

    /**
     * 查询
     *
     * @param provider Elaticsearch Mapper Provider
     * @return {@link List }
     * @throws IOException 查询异常
     */
    public List<Map<String, Object>> query(ESMapperProvider provider) throws IOException {
        SearchResponse response = execute(provider);
        SearchHit[] searchHits = Opt.ofNullable(response).map(SearchResponse::getHits).map(SearchHits::getHits).orElse(new SearchHit[0]);
        return Stream.of(searchHits).map(hit -> JSONUtil.parseObj(hit.getSourceAsString()).entrySet().stream()
                .filter(entry -> isEmpty(provider.getIncludes()) || containsAny(entry.getKey(), provider.getIncludes()))
                .filter(entry -> isEmpty(provider.getExcludes()) || !containsAny(entry.getKey(), provider.getExcludes()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))).collect(Collectors.toList());
    }

    /**
     * 执行
     *
     * @param provider Elaticsearch Mapper Provider
     * @return {@link SearchResponse}
     * @throws IOException 查询异常
     */
    private SearchResponse execute(ESMapperProvider provider) throws IOException {
        ESMapperProvider.DslModel dsl = provider.getDsl();

        SearchRequest request = new SearchRequest();
        request.indices(provider.getIndex());

        SearchSourceBuilder source = new SearchSourceBuilder();
        source.query(QueryBuilders.wrapperQuery(dsl.getQuery().toString()));

        String[] includes = provider.getIncludes();
        String[] excludes = provider.getExcludes();
        if (ArrayUtil.isNotEmpty(includes) || ArrayUtil.isNotEmpty(excludes)) {
            source.fetchSource(includes, excludes);
        }

        Opt.ofNullable(aggregations(dsl.getAggregations())).ifPresent(source::aggregation);

        JSONArray sortArray = dsl.getSort();
        if (Objects.nonNull(sortArray) && !sortArray.isEmpty()) {
            IntStream.range(0, sortArray.size()).forEach(i -> sortArray.getJSONObject(i).forEach((name, order) -> source.sort(name,
                    equalsIgnoreCase(String.valueOf(order), String.valueOf(SortOrder.ASC)) ? SortOrder.ASC : SortOrder.DESC)));
        }

        source.from(Opt.ofNullable(dsl.getFrom()).orElse(0));
        source.size(Opt.ofNullable(dsl.getSize()).orElse(10_000));

        request.source(source);

        return restHighLevelClient.search(request, RequestOptions.DEFAULT);
    }

    /**
     * 聚合查询
     *
     * @param aggregations 聚合DSL
     * @return {@link AggregationBuilder}
     */
    private AggregationBuilder aggregations(JSONObject aggregations) {
        if (Objects.isNull(aggregations)) return null;
        AggregationBuilder aggregationBuilder = null;
        for (String name : aggregations.keySet()) {
            JSONObject aggregation = aggregations.getJSONObject(name);
            for (String function : aggregation.keySet()) {
                JSONObject details = aggregation.getJSONObject(function);
                String field = details.getStr(DSL_AGGREGATIONS_FIELD);
                switch (function.toLowerCase()) {
                    case DSL_VALUE_COUNT:
                        aggregationBuilder = AggregationBuilders.count(name).field(field);
                        break;
                    case DSL_MAX:
                        aggregationBuilder = AggregationBuilders.max(name).field(field);
                        break;
                    case DSL_MIN:
                        aggregationBuilder = AggregationBuilders.min(name).field(field);
                        break;
                    case DSL_AVG:
                        aggregationBuilder = AggregationBuilders.avg(name).field(field);
                        break;
                    case DSL_SUM:
                        aggregationBuilder = AggregationBuilders.sum(name).field(field);
                        break;
                    case DSL_TERMS:
                        int size = details.getInt(DSL_TERMS_SIZE, Integer.MAX_VALUE);
                        aggregationBuilder = AggregationBuilders.terms(name).field(field).size(size);
                        break;
                    default:
                }
            }
            if (Objects.nonNull(aggregationBuilder)) {
                JSONObject subAggregations = aggregation.getJSONObject(DSL_AGGREGATIONS);
                if (!JSONUtil.isNull(subAggregations) && !subAggregations.isEmpty()) {
                    aggregationBuilder.subAggregation(aggregations(subAggregations));
                }
            }
        }
        return aggregationBuilder;
    }

    /**
     * 聚合结果
     *
     * @param aggregation 聚合
     * @return {@link List}
     */
    private List<Map<String, Object>> aggregationResult(Aggregation aggregation) {
        List<Map<String, Object>> result = CollUtil.newArrayList();
        if (Objects.nonNull(aggregation)) {
            String name = aggregation.getName();
            if (aggregation instanceof ParsedValueCount) {
                result.add(MapUtil.of(name, ((ParsedValueCount) aggregation).getValue()));
            } else if (aggregation instanceof ParsedMax) {
                result.add(MapUtil.of(name, ((ParsedMax) aggregation).getValue()));
            } else if (aggregation instanceof ParsedMin) {
                result.add(MapUtil.of(name, ((ParsedMin) aggregation).getValue()));
            } else if (aggregation instanceof ParsedAvg) {
                result.add(MapUtil.of(name, ((ParsedAvg) aggregation).getValue()));
            } else if (aggregation instanceof ParsedSum) {
                result.add(MapUtil.of(name, ((ParsedSum) aggregation).getValue()));
            } else if (aggregation instanceof ParsedTerms) {
                List<? extends Terms.Bucket> buckets = ((ParsedTerms) aggregation).getBuckets();
                for (Terms.Bucket bucket : buckets) {
                    ParsedTerms.ParsedBucket parsedBucket = (ParsedTerms.ParsedBucket) bucket;
                    Map<String, Object> map = MapUtil.ofEntries(MapUtil.entry(name, parsedBucket.getKey()),
                            MapUtil.entry(DSL_COUNT, parsedBucket.getDocCount()));
                    Opt.ofNullable(bucket.getAggregations()).map(Aggregations::asList).ifPresent(action
                            -> action.forEach(subAgg -> aggregationResult(subAgg).forEach(map::putAll)));
                    result.add(map);
                }
            }
        }
        return result;
    }

}
