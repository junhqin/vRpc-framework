package com.rpc.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//基于vert.x
public class VertxRpcServer {
    public static  final int PORT = 9998;
    public void doStart(){
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 HTTP 服务器
        HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        server.requestHandler(new HttpServerHandler());
        // 启动 HTTP 服务器并监听指定端口
        server.listen(PORT, result -> {
            if (result.succeeded()) {
                log.info("Server is now listening on port " + PORT);
            } else {
                log.error("Failed to start server: " + result.cause());
            }
        });


    }
}
