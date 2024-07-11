package com.rpc.server.service.impl;
import com.rpc.common.model.User;
import com.rpc.common.service.UserService;
import com.rpc.starter.annotation.RpcService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl2 implements UserService{
    @Override
    public User getUser(User user) {
        System.out.println("服务2-用户名：" + user.getName());
        return user;
    }
}
