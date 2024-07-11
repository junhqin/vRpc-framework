package com.rpc.client.example;


import com.google.protobuf.RpcUtil;
import com.rpc.bootstrap.ConsumerBootstrap;
import com.rpc.common.model.User;
import com.rpc.common.service.UserService;
import com.rpc.config.RpcConfig;
import com.rpc.proxy.ProxyFactory;
import com.rpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 *
 */
public class ConsumerExample {

    public static void main(String[] args) {
//        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
//        System.out.println("rpcConfig is "+rpcConfig);
        ConsumerBootstrap.init();
        // 获取代理
        UserService userService = ProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("junhqin");
        // 调用
        for(int i=0;i<3;i++){
            try{
                User newUser = userService.getUser(user);
                if (newUser != null) {
                    System.out.println("user == "+newUser.getName());
                } else {
                    System.out.println("user == null");
                }
            }catch(Exception e){
                throw new RuntimeException(e);
            }

        }
    }
}
