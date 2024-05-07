package com.rpc.client.example;


import com.google.protobuf.RpcUtil;
import com.rpc.common.model.User;
import com.rpc.common.service.UserService;
import com.rpc.config.RpcConfig;
import com.rpc.proxy.ProxyFactory;
import com.rpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @learn <a href="https://codefather.cn">编程宝典</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println("rpcConfig is "+rpcConfig);
        // 获取代理
        UserService userService = ProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("junhqin");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        System.out.println(userService.getNumber());
    }
}
