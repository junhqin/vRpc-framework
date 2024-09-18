package com.rpc.client.controller;

import com.rpc.common.model.User;
import com.rpc.common.service.UserService;
import com.rpc.starter.annotation.RpcReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {
    @RpcReference
    private UserService userService;
    @GetMapping("/user")
    public String getUser() {
        User user = new User();
        user.setName("junhqin");
        User result = userService.getUser(user);
        return result.getName();
    }
}
