package com.c3stones.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.c3stones.dto.UserShippingAddressDto;
import com.c3stones.entity.ShippingAddress;
import com.c3stones.entity.User;
import com.c3stones.service.UserService;
import com.github.yulichang.toolkit.MPJWrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 用户 Controller
 *
 * @author CL
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询用户列表数据
     *
     * @return
     */
    @RequestMapping(value = "/listData")
    public List<User> listData() {
        return userService.list(Wrappers.emptyWrapper());
    }

    /**
     * 查询用户分页数据
     *
     * @return
     */
    @RequestMapping(value = "/pageData")
    public IPage<User> pageData(long current, long size, User user) {
        return userService.page(new Page<>(current, size), Wrappers.query(user));
    }

    /**
     * 查询用户默认收货地址
     *
     * @return
     */
    @RequestMapping(value = "/default")
    public UserShippingAddressDto userDefaultShippingAddress(UserShippingAddressDto userShippingAddressDto) {
        return userService.selectJoinOne(UserShippingAddressDto.class, MPJWrappers.<User>lambdaJoin()
                .selectAll(User.class)
                .select(ShippingAddress::getAddress)
                .selectAs(ShippingAddress::getIsDefault, UserShippingAddressDto::getDef)
                .leftJoin(ShippingAddress.class, ShippingAddress::getUserId, User::getId)
                .eq(User::getId, userShippingAddressDto.getId())
                .eq(ShippingAddress::getIsDefault, "t")
                .last("limit 1"));
    }

    /**
     * 查询用户收货地址分页数据
     *
     * @return
     */
    @RequestMapping(value = "/pageAddressData")
    public IPage<UserShippingAddressDto> pageUserShippingAddressData(long current, long size, UserShippingAddressDto userShippingAddressDto) {
        // Mybatis-plus 对count进行了优化，因此需要关掉优化关联统计sql，详见：https://gitee.com/best_handsome/mybatis-plus-join/issues/I55FIU
        return userService.selectJoinListPage(new Page<>(current, size).setOptimizeCountSql(false), UserShippingAddressDto.class, MPJWrappers.<User>lambdaJoin()
                .selectAll(User.class)
                .select(ShippingAddress::getAddress)
                .selectAs(ShippingAddress::getIsDefault, UserShippingAddressDto::getDef)
                .leftJoin(ShippingAddress.class, ShippingAddress::getUserId, User::getId)
                .eq(Objects.nonNull(userShippingAddressDto.getId()), User::getId, userShippingAddressDto.getId())
                .like(StringUtils.isNotEmpty(userShippingAddressDto.getUsername()), User::getUsername, userShippingAddressDto.getUsername())
                .eq(StringUtils.isNotEmpty(userShippingAddressDto.getDef()), ShippingAddress::getIsDefault, userShippingAddressDto.getDef()));
    }

    /**
     * 统计用户收货地址数量
     *
     * @return
     */
    @RequestMapping(value = "/countAddress")
    public long countUserShippingAddress(UserShippingAddressDto userShippingAddressDto) {
        return userService.selectJoinCount(MPJWrappers.lambdaJoin()
                .select(ShippingAddress::getAddress)
                .leftJoin(ShippingAddress.class, ShippingAddress::getUserId, User::getId)
                .eq(User::getId, userShippingAddressDto.getId()));
    }

}
