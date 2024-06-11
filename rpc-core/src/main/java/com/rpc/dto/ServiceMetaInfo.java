package com.rpc.dto;

import com.rpc.constant.RpcConstant;
import lombok.Data;

/**
 * 服务元信息，用于注册信息的定义
 */
@Data
public class ServiceMetaInfo {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本号
     */
    private String serviceVersion= RpcConstant.DEFAULT_SERVICE_VERSION;
    /**
     * 服务域名
     */
    private String serviceHost;
    /**
     * 服务端口
     */
    private Integer servicePort;
    /**
     * 服务分组
     */
    private String serviceGroup = "default";
    /**
     * 获取服务键名
     * @return
     */
    public String getServiceKey() {
        // 后续可扩展服务分组
        return String.format("%s:%s", serviceName, serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }

    /**
     * 获取完整服务地址
     */
    public String getServiceAddress() {
        return String.format("http://%s:%s", serviceHost, servicePort);
    }
}
