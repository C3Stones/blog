package com.c3stones.json.enums;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 操作符 枚举类
 *
 * @author CL
 */
@Getter
@AllArgsConstructor
public enum Operator {

    EQ(" = ", "=="),
    NEQ(" != ", "!="),
    GT(" > ", ">"),
    LT(" < ", "<"),
    GTE(" >= ", ">="),
    LTE(" <= ", "<="),
    IN(" in ", "in", "json_contain", "json_contains"),
    NOT_IN(" not in ", "notIn", "json_not_contain"),
    CONTAIN(" like ", "contain"),
    NOT_CONSTAIN(" not like ", "notContain"),
    EMPTY(" is null ", "isNull"),
    NOT_EMPTY(" is not null ", "notNull"),
    START_WITH(CONTAIN.character, "startWith"),
    END_WITH(CONTAIN.character, "endWith"),
    RANGE(" between ", "range"),
    ;

    private final String character;
    private final List<String> operators;

    Operator(String character, String... operators) {
        this.character = character;
        this.operators = Arrays.asList(operators);
    }

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return {@link Operator}
     */
    public static Operator findByValue(String value) {
        if (StrUtil.isNotEmpty(value)) {
            for (Operator operator : values()) {
                if (StrUtil.equalsAnyIgnoreCase(value, operator.getOperators().toArray(new String[0]))) {
                    return operator;
                }
            }
        }
        return EQ;
    }

    /**
     * 判断字符串
     *
     * @param source   源
     * @param operator 操作符
     * @param target   目标
     * @return {@link Boolean}
     */
    public static boolean judgeStr(String source, Operator operator, String target) {
        boolean result = false;
        if (!(StrUtil.isEmpty(source) || StrUtil.isEmpty(target))) {
            switch (operator) {
                case EQ:
                    result = StrUtil.equals(source, target);
                    break;
                case NEQ:
                    result = !StrUtil.equals(source, target);
                    break;
                case CONTAIN:
                    result = StrUtil.contains(source, target);
                    break;
                case NOT_CONSTAIN:
                    result = !StrUtil.contains(source, target);
                    break;
                case IN:
                    result = StrUtil.equalsAny(source, target.split(StrUtil.COMMA));
                    break;
                case NOT_IN:
                    result = !StrUtil.equalsAny(source, target.split(StrUtil.COMMA));
                    break;
                case START_WITH:
                    result = StrUtil.startWith(source, target);
                    break;
                case END_WITH:
                    result = StrUtil.endWith(source, target);
                    break;
                default:
            }
        }
        return result;
    }

    /**
     * 判断数字
     *
     * @param source   源
     * @param operator 操作符
     * @param targets  值
     * @return {@link Boolean}
     */
    public static boolean judgeNumber(Number source, Operator operator, Number... targets) {
        boolean result = false;
        if (!(Objects.isNull(source) || Objects.isNull(targets))) {
            double src = NumberUtil.toDouble(source);
            Double[] values = Stream.of(targets).map(NumberUtil::toDouble).toArray(Double[]::new);
            switch (operator) {
                case EQ:
                    result = NumberUtil.equals(src, values[0]);
                    break;
                case NEQ:
                    result = !NumberUtil.equals(src, values[0]);
                    break;
                case GT:
                    result = src > Opt.ofNullable(values[0]).orElse(Double.MAX_VALUE);
                    break;
                case LT:
                    result = src < Opt.ofNullable(values[0]).orElse(Double.MIN_VALUE);
                    break;
                case GTE:
                    result = src >= Opt.ofNullable(values[0]).orElse(Double.MAX_VALUE);
                    break;
                case LTE:
                    result = src <= Opt.ofNullable(values[0]).orElse(Double.MIN_VALUE);
                    break;
                case IN:
                    result = StrUtil.equalsAny(source.toString(), Stream.of(targets)
                            .map(Number::toString).toArray(String[]::new));
                    break;
                case NOT_IN:
                    result = !StrUtil.equalsAny(source.toString(), Stream.of(targets)
                            .map(Number::toString).toArray(String[]::new));
                    break;
                case RANGE:
                    result = src >= Opt.ofNullable(values[0]).orElse(Double.MAX_VALUE)
                            && src <= Opt.ofNullable(values[1]).orElse(Double.MIN_VALUE);
                    break;
                default:
            }
        }
        return result;
    }

    /**
     * 判断时间
     *
     * @param before   之前时间
     * @param operator 操作符
     * @param after    之后时间
     * @return {@link Boolean}
     */
    public static boolean judgeDate(Date before, Operator operator, Date after) {
        if (Objects.isNull(before) || Objects.isNull(after)) {
            return false;
        }
        return judgeNumber(before.getTime(), operator, after.getTime());
    }

    /**
     * 判断数组
     *
     * @param sources  源
     * @param operator 操作符
     * @param targets  值
     * @return {@link Boolean}
     */
    public static boolean judgeArray(Object[] sources, Operator operator, Object... targets) {
        boolean result = false;
        if (!(Objects.isNull(sources) || Objects.isNull(targets))) {
            String[] src = Stream.of(sources).filter(Objects::nonNull).map(Objects::toString).toArray(String[]::new);
            String[] values = Stream.of(targets).filter(Objects::nonNull).map(Objects::toString).toArray(String[]::new);
            switch (operator) {
                case IN:
                    result = ArrayUtil.containsAny(src, values);
                    break;
                case NOT_IN:
                    result = !ArrayUtil.containsAny(src, values);
                    break;
                default:
            }
        }
        return result;
    }

    /**
     * 反转操作符
     *
     * @param operator 操作符
     * @return {@link Operator}
     */
    public static Operator reverse(Operator operator) {
        if (Objects.nonNull(operator)) {
            switch (operator) {
                case EQ:
                    operator = NEQ;
                    break;
                case NEQ:
                    operator = EQ;
                    break;
                case GT:
                    operator = LT;
                    break;
                case LT:
                    operator = GT;
                    break;
                case GTE:
                    operator = LTE;
                    break;
                case LTE:
                    operator = GTE;
                    break;
                case IN:
                    operator = NOT_IN;
                    break;
                case NOT_IN:
                    operator = IN;
                    break;
                case CONTAIN:
                    operator = NOT_CONSTAIN;
                    break;
                case NOT_CONSTAIN:
                    operator = CONTAIN;
                    break;
                case EMPTY:
                    operator = NOT_EMPTY;
                    break;
                case NOT_EMPTY:
                    operator = EMPTY;
                    break;
                case START_WITH:
                    operator = END_WITH;
                    break;
                case END_WITH:
                    operator = START_WITH;
                    break;
                default:
            }
        }
        return operator;
    }

}