package com.rpc.server.service.impl;
import com.rpc.common.model.User;
import com.rpc.common.service.UserService;

public class UserServiceImpl2 implements UserService{
    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        user.setName("junhqin");
        return user;
    }
}
