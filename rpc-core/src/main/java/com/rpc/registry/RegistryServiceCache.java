package com.rpc.registry;


import com.rpc.dto.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册中心服务本地缓存 ，用一个map来存储服务信息
 *
 */
@Slf4j
public class RegistryServiceCache {

    /**
     * 服务缓存
     */
    Map<String, List<ServiceMetaInfo>> serviceCache = new HashMap<>();

    /**
     * 写缓存
     *
     * @param newServiceCache
     * @return
     */
    public void writeCache(List<ServiceMetaInfo> newServiceCache) {
        if(!newServiceCache.isEmpty()){
            String serviceKey = newServiceCache.get(0).getServiceKey();
            serviceCache.put(serviceKey, newServiceCache);
        }else {
            log.info("newServiceCache is empty");
        }
    }

    /**
     * 读缓存
     *
     * @return
     */
    public List<ServiceMetaInfo> readCache(String serviceKey) {
        return serviceCache.get(serviceKey);
    }

    /**
     * 清空某节点的缓存
     */
    public void clearCache(String serviceKey) {
        this.serviceCache.remove(serviceKey);
    }
}
