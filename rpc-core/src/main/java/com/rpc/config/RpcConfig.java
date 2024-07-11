package com.rpc.config;

import com.rpc.fault.retry.RetryStrategyKeys;
import com.rpc.serializer.SerializerKeys;
import lombok.Data;

@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "my-rpc";
    /**
     * 版本号
     */
    private String version = "1.0";
    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";
    /**
     * 服务器端口号
     */
    private Integer serverPort = 9998;
    /**
     * Mock选项
     */
    private boolean mock = false;
    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig ;

    /**
     * 重试机制
     */
    private String retryStrategy = RetryStrategyKeys.FIXED_INTERVAL;
}
