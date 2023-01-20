package com.c3stones.db.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.c3stones.Application;
import com.c3stones.common.DataFactory;
import com.c3stones.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 用户信息 Mapper 单元测试
 *
 * @author CL
 */
@SpringBootTest(classes = Application.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * 构造用户信息
     *
     * @return {@link Stream<Arguments>}
     */
    private static Stream<Arguments> user() {
        return DataFactory.user().stream().map(Arguments::of);
    }

    /**
     * 测试新增
     *
     * @param user 用户信息
     */
    @Order(1)
    @ParameterizedTest
    @MethodSource(value = {"user"})
    public void testSave(User user) {
        // 自增主键
        user.setId(null);
        int result = userMapper.insert(user);

        Assertions.assertEquals(1, result);
    }

    /**
     * 测试查询
     */
    @Order(2)
    @Test
    public void testQuery() {
        List<User> result = userMapper.selectList(Wrappers.emptyWrapper());

        result.forEach(user -> {
            Optional<User> optional = DataFactory.user().stream().filter(u ->
                    StrUtil.equals(user.getAccount(), u.getAccount())).findFirst();
            Assertions.assertTrue(optional.isPresent());
            Assertions.assertEquals(user, optional.get());
        });
    }

    /**
     * 测试统计
     */
    @Order(3)
    @Test
    public void testCount() {
        Long count = userMapper.selectCount(Wrappers.emptyWrapper());

        Assertions.assertEquals(DataFactory.user().size(), count.intValue());
    }

}