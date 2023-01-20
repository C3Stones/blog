package com.c3stones.es.config;


import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.net.URL;

/**
 * Elasticsearch 配置类
 *
 * @author CL
 */
@Configuration
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    /**
     * 重写 RestHighLevelClient
     *
     * @return {@link RestHighLevelClient}
     */
    @Override
    public RestHighLevelClient elasticsearchClient() {
        HttpHost[] httpHosts = elasticsearchProperties.getUris().stream().map(uri -> {
            URL url = URLUtil.url(uri);
            return new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        }).toArray(HttpHost[]::new);
        int connectTimeout = (int) elasticsearchProperties.getConnectionTimeout().getSeconds() * 1000;
        int socketTimeout = (int) elasticsearchProperties.getSocketTimeout().getSeconds() * 1000;
        String username = elasticsearchProperties.getUsername();
        String password = elasticsearchProperties.getPassword();
        return new RestHighLevelClient(RestClient
                .builder(httpHosts)
                .setRequestConfigCallback(
                        requestConfigBuilder -> requestConfigBuilder
                                .setConnectTimeout(connectTimeout)
                                .setSocketTimeout(socketTimeout)
                                .setConnectionRequestTimeout(connectTimeout))
                .setHttpClientConfigCallback(
                        httpClientBuilder -> {
                            if (StrUtil.isNotEmpty(username) && StrUtil.isNotEmpty(password)) {
                                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
                                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                            }
                            return httpClientBuilder;
                        })
        );
    }

}
