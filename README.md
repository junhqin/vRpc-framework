# RPC framework

### 项目介绍

一款基于 Vert.x + Zookeeper 的轻量级 RPC 框架。该框架提供服务注册、发现，自定义协议，负载均衡，容错机制等功能，
并且使用了 Java SPI 机制提供了标准服务接口，开发者能够根据需求对框架进行扩展。


通过学习该项目，可以了解到 RPC 框架的底层实现原理，下面是 RPC 框架调用流程图：
![image.png](https://git.acwing.com/junhqin/rawimage/-/raw/main/pictures/2024/05/7_15_34_57_20240507153457.png)

### 功能设计
#### 注册中心模块
![](https://git.acwing.com/junhqin/rawimage/-/raw/main/pictures/2024/05/7_15_37_22_20240507153721.png)
#### 目录结构
```text
|-- service-common                  -- 示例代码的一些依赖，包括接口、model
|-- rpc-core      -- 框架核心实现
|-- rpc-spring-boot-starter -- 注解驱动，可在 Spring Boot 项目中使用 
|-- service-consumer       -- 服务消费者示例
|-- service-provider       -- 服务提供者示例
|-- example-consumer     -- 使用注解的服务消费者示例
|-- example-provider     -- 使用注解的服务提供在示例
```

#### 具体功能

- 支持 `application.properties` 和 `application.yml` 配置文件
- 使用 Vert.x 框架进行网络通信
- Java SPI 机制
- 实现注册中心(监听机制)
- 提供接口的 Mock 服务
- **服务中心：** 提供 Etcd 和 Zookeeper 两种实现
- **序列化器：** 提供多种序列化器
    - Json 序列化
    - Kryo 序列化
    - Hessian 序列化
- **自定义传输协议**
- **负载均衡：** 实现轮询、随机、一致性哈希等策略
- **容错机制：** 静默处理
- **注解驱动设计**、

***1. 注册中心实现监听机制：***
![image-20240611143046053](https://git.acwing.com/junhqin/rawimage/-/raw/main/pictures/2024/06/11_14_30_46_image-20240611143046053.png)
`服务消费者`创建监听器对服务节点key进行监听

***2. 自定义协议：***
消息结构设计：
```
魔数:作用是安全校验，防止服务器处理了非框架发来的乱七八糟的消息(类似 HTTPS 的安全证书)

版本号:保证请求和响应的一致性(类似 HTTP 协议有 1.0/2.0 等版本)

序列化方式:来告诉服务端和客户端如何解析数据(类似 HTTP 的 Content-Type 内容类型)

类型:标识是请求还是响应?或者是心跳检测等其他用途。(类似 HTTP 有请求头和响应头)

状态:如果是响应，记录响应的结果(类似 HTTP 的 200 状态代码)
```
![](https://git.acwing.com/junhqin/rawimage/-/raw/main/pictures/2024/06/12_9_25_32_image-20240612092525959.png)
