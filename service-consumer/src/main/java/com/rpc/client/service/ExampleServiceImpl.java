package com.rpc.client.service;

import com.rpc.common.model.User;
import com.rpc.common.service.UserService;
import com.rpc.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {
    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("junhqin");
        User result = userService.getUser(user);
        System.out.println(result.getName());
    }
}
