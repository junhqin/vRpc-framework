package com.rpc.server.service.impl;

import com.rpc.common.service.UserService;
import com.rpc.proxy.ProxyFactory;
import com.rpc.registry.LocalRegistry;
import com.rpc.server.VertxRpcServer;

public class EasyProviderExample {
    public static void main(String[] args) {
        //注册服务
        LocalRegistry localRegistry = new LocalRegistry();
        localRegistry.register(UserService.class.getName(),UserServiceImpl.class);
        //启动web服务器
        VertxRpcServer vertxRpcServer = new VertxRpcServer();
        vertxRpcServer.doStart();
    }
}
