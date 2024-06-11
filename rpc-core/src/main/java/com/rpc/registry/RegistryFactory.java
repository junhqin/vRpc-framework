package com.rpc.registry;

import com.rpc.registry.Registry;
import com.rpc.registry.impl.ZooKeeperRegistry;
import com.rpc.spi.SpiLoader;

/**
 * 注册中心工厂（用于获取注册中心对象）
 *
 */
public class RegistryFactory {

    // SPI 动态加载
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new ZooKeeperRegistry();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }

}
