package com.rpc.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistryConfig {
    /**
     * 地址
     */
    private String host = "localhost";
    /**
     * 超时时间
     */
    private Long timeout = 1000L;
    /**
     * 端口
     */
    private Integer port = 2181;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 注册中心类别
     */
    private String registry;
}
