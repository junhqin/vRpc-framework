package com.rpc;

import com.rpc.config.RegistryConfig;
import com.rpc.config.RpcConfig;
import com.rpc.constant.RpcConstant;
import com.rpc.registry.Registry;
import com.rpc.registry.RegistryFactory;
import com.rpc.registry.impl.ZooKeeperRegistry;
import com.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;
/**
 * 单例模式：一个类中只有一个实例
 * 双重检查锁
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());
        //注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);
        // 创建并注册 Shutdown Hook，JVM 退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }
    public static void init(){
        RpcConfig newRpcConfig;
        try{
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            //读取配置文件失败
            log.info("read config fail");
            e.printStackTrace();
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }
    /**
     * 获取配置
     */
    public static RpcConfig getRpcConfig(){
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
