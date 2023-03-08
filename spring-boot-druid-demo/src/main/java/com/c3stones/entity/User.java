package com.c3stones.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 用户
 *
 * @author CL
 */
@Data
@TableName(keepGlobalPrefix = true)
public class User extends Model<User> {

    @TableId
    private Long id;

    @TableField
    private String username;

    @TableField
    private String sex;

    @TableField
    private String phone;

}
