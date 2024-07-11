package com.rpc.proxy;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.rpc.RpcApplication;
import com.rpc.config.RpcConfig;
import com.rpc.constant.RpcConstant;
import com.rpc.dto.RpcRequest;
import com.rpc.dto.RpcResponse;
import com.rpc.dto.ServiceMetaInfo;
import com.rpc.fault.retry.RetryStrategy;
import com.rpc.fault.retry.RetryStrategyFactory;
import com.rpc.fault.tolerant.FailFastTolerantStrategy;
import com.rpc.fault.tolerant.TolerantStrategy;
import com.rpc.loadbalancer.ConsistentHashLoadBalancer;
import com.rpc.loadbalancer.LoadBalancer;
import com.rpc.protocol.ProtocolConstant;
import com.rpc.protocol.ProtocolMessage;
import com.rpc.protocol.ProtocolMessageDecoder;
import com.rpc.protocol.ProtocolMessageEncoder;
import com.rpc.protocol.ProtocolMessageSerializerEnum;
import com.rpc.protocol.ProtocolMessageTypeEnum;
import com.rpc.registry.Registry;
import com.rpc.registry.RegistryFactory;
import com.rpc.serializer.Serializer;
import com.rpc.serializer.SerializerFactory;
import com.rpc.server.tcp.VertxTcpClient;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理（JDK 动态代理）
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @learn <a href="https://codefather.cn">编程宝典</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // SPI指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            //从注册中心获取服务提供者的请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfos)) {
                throw new RuntimeException("暂无服务地址");
            }
            RpcResponse rpcResponse;
            try{
                //发送TCP请求,并应用重试机制
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(() ->
                        VertxTcpClient.dpRequest(rpcRequest, serviceMetaInfos));
            } catch (Exception e){
                //容错机制
                TolerantStrategy tolerantStrategy = new FailFastTolerantStrategy();
                rpcResponse = tolerantStrategy.doTolerant(null, e);
            }
            return rpcResponse.getData();
        } catch (IOException e) {
            throw new RuntimeException("调用服务失败：", e);
        }
    }
}
