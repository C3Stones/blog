package com.c3stones.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.c3stones.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户 Controller
 *
 * @author CL
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 用户信息
     */
    @RequestMapping("/{id}")
    public User get(@PathVariable Long id) {
        return new User().selectById(id);
    }

    /**
     * 查询全部用户
     *
     * @return 用户列表
     */
    @RequestMapping("/list")
    public List<User> list() {
        return new User().selectAll();
    }

    /**
     * 更新用户
     *
     * @return 更新结果
     */
    @RequestMapping("/update")
    public Boolean update() {
        return new User().update(new LambdaUpdateWrapper<User>().set(User::getPhone, "123456789"));
    }

    /**
     * 删除用户
     *
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public Boolean delete() {
        return new User().delete(new QueryWrapper<>());
    }

}
