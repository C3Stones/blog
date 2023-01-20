package com.c3stones.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Objects;

/**
 * 用户信息
 *
 * @author CL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user")
@Document(indexName = "user")
public class User {

    /**
     * ID
     */
    @Id
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @Field(type = FieldType.Keyword)
    @TableField
    private String username;

    /**
     * 账号
     */
    @Field(type = FieldType.Keyword)
    @TableField
    private String account;

    /**
     * 年龄
     */
    @Field(type = FieldType.Integer)
    @TableField
    private Integer age;

    /**
     * 性别 0-女 1-男
     */
    @Field(type = FieldType.Integer)
    @TableField
    private Integer sex;

    /**
     * 地址
     */
    @Field(type = FieldType.Keyword)
    @TableField
    private String address;

    /**
     * 创建时间
     */
    @Field(name = "create_time", type = FieldType.Date)
    @TableField
    private Date createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username)
                && Objects.equals(account, user.account)
                && Objects.equals(age, user.age)
                && Objects.equals(sex, user.sex)
                && Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, account, age, sex, address);
    }

}

