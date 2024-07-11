package com.rpc.server.example;

import com.rpc.RpcApplication;
import com.rpc.bootstrap.ProviderBootstrap;
import com.rpc.common.service.UserService;
import com.rpc.config.RegistryConfig;
import com.rpc.config.RpcConfig;
import com.rpc.dto.ServiceMetaInfo;
import com.rpc.dto.ServiceRegisterInfo;
import com.rpc.registry.Registry;
import com.rpc.registry.RegistryFactory;
import com.rpc.registry.impl.LocalRegistry;
import com.rpc.server.VertxHttpServer;
import com.rpc.server.service.impl.UserServiceImpl;
import com.rpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

public class ProviderExample {

    public static void main(String[] args) {
        // 要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
