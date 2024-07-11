package com.rpc.loadbalancer;

import com.rpc.dto.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

public interface LoadBalancer {
    /**
     * 选择服务提供方调用
     * @param requestParams
     * @return
     */
    ServiceMetaInfo select(String requestParams);

}
