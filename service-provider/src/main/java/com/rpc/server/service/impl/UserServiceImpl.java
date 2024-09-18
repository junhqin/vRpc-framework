package com.rpc.server.service.impl;
import com.rpc.common.model.User;
import com.rpc.common.service.UserService;
import com.rpc.starter.annotation.RpcService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@RpcService
public class UserServiceImpl implements UserService {

    public User getUser(User user) {
        user.setName("我是junhqin");
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
