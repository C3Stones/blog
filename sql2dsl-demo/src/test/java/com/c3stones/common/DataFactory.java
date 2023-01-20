package com.c3stones.common;

import com.c3stones.entity.User;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 数据工厂
 *
 * @author CL
 */
public class DataFactory {

    /**
     * 构造用户信息
     *
     * @return {@link List}
     */
    public static List<User> user() {
        return Arrays.asList(
                new User(1L, "张三", "zhangsan", 20, 1, "西安", new Date()),
                new User(2L, "李四", "lisi", 25, 0, "北京", new Date()),
                new User(3L, "王五", "wangwu", 30, 1, "上海", new Date()),
                new User(4L, "赵六", "zhaoliu", 30, 0, "北京", new Date())
        );
    }

    /**
     * 构造查询SQL
     * <p style="color:yellow">
     * ps: 函数必须指定别名
     * <p/>
     *
     * @return {@link List}
     */
    public static List<String> mysqlQuery() {
        return Arrays.asList(
                "select * from user",
                "select * from user order by age desc",
                "select id, username from user limit 0,2",
                "select * from user where age between 25 and 30",
                "select id, age, sex from user where create_time between '2023-01-01' and '2023-01-31'",
                "select * from user where create_time between from_unixtime(1672502400000/1000) and from_unixtime(1675180799999/1000)",
                "select * from user where id < 10 and username like concat('%' ,'张', '%')",
                "select * from user where id < 10 and username not like '%李%'",
                "select id, account, address from user where age > 18 and (username like concat('张', '%') or account = lower('zhangsan') or address in ('北京', '西安'))"
        );
    }

    /**
     * 构造聚合SQL
     * <p style="color:yellow">
     * ps: 函数必须指定别名
     * <p/>
     *
     * @return {@link List}
     */
    public static List<String> mysqlAggregation() {
        return Arrays.asList(
                "select count(*) as count from user",
                "select count(id) as count from user where sex = 0 or sex = -1",
                "select age, count(id) as count from user group by age",
                "select address, sex, count(*) as count from user group by address, sex",
                "select age, count(id) as count from user where age > 25 or username like concat('%' ,'张', '%')  group by age",
                "select min(age) as min from user",
                "select sex, max(age) as max from user group by sex",
                "select avg(age) as avg from user",
                "select sex, sum(age) as sum from user group by sex"
        );
    }

}
