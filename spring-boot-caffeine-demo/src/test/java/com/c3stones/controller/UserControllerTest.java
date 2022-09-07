package com.c3stones.controller;

import com.c3stones.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.c3stones.compent.Response.SUCCESS_CODE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserController 单元测试
 *
 * @author CL
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    /**
     * 接口测试
     */
    @Test
    public void userTest() throws Exception {
        User user = new User()
                .setId("001")
                .setUsername("张三")
                .setAge(24);

        log.info("新增用户 ==>");
        save(user);

        log.info("查询用户 ==>");
        select(
                user.getId(),
                jsonPath("$.data.id").value(user.getId())
        );

        log.info("修改用户信息 ==>");
        User newUser = new User()
                .setId(user.getId())
                .setAge(26);
        update(newUser);

        log.info("查询用户 ==>");
        select(
                user.getId(),
                jsonPath("$.data.id").value(newUser.getId()),
                jsonPath("$.data.age").value(newUser.getAge())
        );

        log.info("删除用户 ==>");
        delete(user.getId());

        log.info("查询用户 ==>");
        select(
                user.getId(),
                jsonPath("$.data").isEmpty()
        );

        log.info("测试超时 ==>");
        save(user);

        log.info("查询用户 ==>");
        select(
                user.getId(),
                jsonPath("$.data").isNotEmpty()
        );

        log.info("等待五分钟（本地全局缓存默认过期时间） ==>");
        Thread.sleep(5 * 60 * 1000 + 10);

        log.info("查询用户 ==>");
        select(
                user.getId(),
                jsonPath("$.data").isEmpty()
        );
    }

    /**
     * 新增用户
     *
     * @param user
     */
    private void save(User user) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
                        .param("id", user.getId())
                        .param("username", user.getUsername())
                        .param("age", user.getAge().toString()))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }

    /**
     * 查询用户
     *
     * @param id
     * @param matcher
     */
    private void select(String id, ResultMatcher... matcher) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/get")
                        .param("id", id))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andExpectAll(matcher)                                          // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }

    /**
     * 更新用户
     *
     * @param newUser
     */
    private void update(User newUser) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/update")
                        .param("id", newUser.getId())
                        .param("username", newUser.getUsername())
                        .param("age", newUser.getAge().toString()))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }

    /**
     * 删除用户
     *
     * @param id
     */
    private void delete(String id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete")
                        .param("id", id))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }
}