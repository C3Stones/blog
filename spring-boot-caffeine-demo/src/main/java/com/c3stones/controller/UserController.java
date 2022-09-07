package com.c3stones.controller;

import com.c3stones.clients.CacheClient;
import com.c3stones.compent.Response;
import com.c3stones.entity.User;
import com.c3stones.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 用户Controller
 *
 * @author CL
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CacheClient cacheClient;

    /**
     * 新增用户
     *
     * @param user 用户
     * @return
     */
    @RequestMapping("/save")
    public Response save(User user) {
        cacheClient.putOfNative(user.getId(), user);
        return Response.success();
    }

    /**
     * 更新用户
     *
     * @param user 用户
     * @return
     */
    @RequestMapping("/update")
    public Response update(User user) {
        User oldUser = cacheClient.getByNative(user.getId(), User.class);
        if (Objects.nonNull(oldUser)) {
            BeanUtil.copyPropertiesIgnoreNull(user, oldUser);
            cacheClient.putOfNative(user.getId(), oldUser);
        }
        return Response.success();
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return
     */
    @RequestMapping("/delete")
    public Response delete(String id) {
        cacheClient.removeOfNative(id);
        return Response.success();
    }

    /**
     * 查询用户
     *
     * @param id 用户ID
     * @return
     */
    @RequestMapping("/get")
    public Response get(String id) {
        User user = cacheClient.getByNative(id, User.class);
        return Response.success(user);
    }

}
