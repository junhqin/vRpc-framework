package com.rpc.server.example;

import com.rpc.common.service.UserService;
import com.rpc.registry.impl.LocalRegistry;
import com.rpc.server.VertxRpcServer;
import com.rpc.server.service.impl.UserServiceImpl;

public class EasyProviderExample {
    public static void main(String[] args) {
        //注册服务
        LocalRegistry localRegistry = new LocalRegistry();
        localRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        //启动web服务器
        VertxRpcServer vertxRpcServer = new VertxRpcServer();
        vertxRpcServer.doStart();
    }
}
