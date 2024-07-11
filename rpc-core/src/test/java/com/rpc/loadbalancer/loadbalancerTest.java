package com.rpc.loadbalancer;

import cn.hutool.core.util.IdUtil;
import com.rpc.dto.ServiceMetaInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class loadbalancerTest {

    List<ServiceMetaInfo> serviceMetaInfoList = new ArrayList<>();
    @Test
    public void ConsistentHashTest(){
        for(int i=0;i<10;i++)
        {
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServicePort(i);
            serviceMetaInfo.setServiceName("service"+i);
            serviceMetaInfo.setServiceHost("localhost");
            serviceMetaInfoList.add(serviceMetaInfo);
        }
        ConsistentHashLoadBalancer consistentHashLoadBalancer = new ConsistentHashLoadBalancer(serviceMetaInfoList);
        ServiceMetaInfo serviceMetaInfo2 = consistentHashLoadBalancer.select(String.valueOf(IdUtil.getSnowflakeNextId()));
        System.out.println(serviceMetaInfo2.getServiceAddress());
        TreeMap<Long, ServiceMetaInfo> testMap = consistentHashLoadBalancer.getVirtualNodes();
        for(Map.Entry<Long, ServiceMetaInfo> entry : testMap.entrySet()){
            System.out.println("Hash: " + entry.getKey() + ", ServiceMetaInfo: " + entry.getValue().getServiceAddress());
        }

    }



}
