package com.c3stones.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户信息
 *
 * @author CL
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "test_user", autoResultMap = true)
public class User {

    @TableId
    private Long id;

    @TableField
    private String name;

    @TableField
    private Integer age;

    @TableField
    private String sex;

    @TableField
    private String address;

    @TableField
    private Boolean status;

    @TableField(value = "createTime")
    private LocalDate createTime;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> hobbies;

    @TableField(value = "luckyNumbers", typeHandler = JacksonTypeHandler.class)
    private List<Integer> luckyNumbers;

}