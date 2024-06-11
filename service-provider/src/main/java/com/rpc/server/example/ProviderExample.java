package com.rpc.server.example;

import com.rpc.RpcApplication;
import com.rpc.common.service.UserService;
import com.rpc.config.RegistryConfig;
import com.rpc.config.RpcConfig;
import com.rpc.dto.ServiceMetaInfo;
import com.rpc.registry.Registry;
import com.rpc.registry.RegistryFactory;
import com.rpc.registry.impl.LocalRegistry;
import com.rpc.registry.impl.ZooKeeperRegistry;
import com.rpc.server.VertxRpcServer;
import com.rpc.server.service.impl.UserServiceImpl;

public class ProviderExample {
    public static void main(String[] args){
        RpcApplication.init();
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);
        RpcConfig rpcConfig  =RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(UserService.class.getName());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        //尝试在注册中心注册服务
        try{
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //启动web服务
        VertxRpcServer vertxRpcServer = new VertxRpcServer();
        vertxRpcServer.doStart();

    }
}
