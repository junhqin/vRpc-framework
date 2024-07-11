package com.rpc.starter.annotation;

import com.rpc.starter.bootstrap.RpcConsumerBootstrap;
import com.rpc.starter.bootstrap.RpcInitBootstrap;
import com.rpc.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {
    /**
     * 需要启动 server
     * @return
     */
    boolean needServer() default true;
}
