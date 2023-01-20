package com.c3stones.es.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.c3stones.Application;
import com.c3stones.db.mapper.UserMapper;
import com.c3stones.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息 Repository 单元测试
 *
 * @author CL
 */
@SpringBootTest(classes = Application.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    /**
     * 测试新增
     */
    @Order(1)
    @Test
    public void testSave() {
        List<User> userList = userMapper.selectList(Wrappers.emptyWrapper());

        Iterable<User> result = userRepository.saveAll(userList);

        Assertions.assertEquals(userList.size(), CollUtil.newArrayList(result).size());
    }

    /**
     * 测试查询
     */
    @Order(2)
    @Test
    public void testQuery() {
        Iterable<User> result = userRepository.findAll();

        List<User> userList = userMapper.selectList(Wrappers.emptyWrapper());

        List<Long> resultIdList = CollUtil.newArrayList(result).stream().map(User::getId).collect(Collectors.toList());
        List<Long> userIdList = userList.stream().map(User::getId).collect(Collectors.toList());
        Assertions.assertTrue(CollUtil.containsAll(userIdList, resultIdList));
    }

    /**
     * 测试统计
     */
    @Order(3)
    @Test
    public void testCount() {
        long result = userRepository.count();

        Long count = userMapper.selectCount(Wrappers.emptyWrapper());

        Assertions.assertEquals(count, result);
    }

}