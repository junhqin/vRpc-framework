package com.rpc.loadbalancer;


import com.rpc.dto.ServiceMetaInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.CRC32;

/**
 * 一致性哈希负载均衡器
 */
@AllArgsConstructor
@Getter
@Setter
public class ConsistentHashLoadBalancer implements LoadBalancer {
    /**
     * 一致性Hash环，存放虚拟节点
     */
    private final TreeMap<Long, ServiceMetaInfo> virtualNodes = new TreeMap<>();
    //原有服务列表
    public List<ServiceMetaInfo> serviceMetaInfos;

    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(String requestParams) {
        if (serviceMetaInfos.isEmpty()) return null;
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfos) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                long hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }
        long hash = getHash(requestParams);
        System.out.println(hash);
        Map.Entry<Long, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }

    private long getHash(String key) {
        CRC32 crc32 = new CRC32();
        crc32.update(key.getBytes());
        return crc32.getValue();
    }
}
