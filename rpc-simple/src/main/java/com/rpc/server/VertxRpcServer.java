package com.rpc.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

//基于vert.x
public class VertxRpcServer {
    public static  final int PORT = 9998;
    public void doStart(){
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 HTTP 服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        server.requestHandler(new VertxRpcServerHandler());

        // 启动 HTTP 服务器并监听指定端口
        server.listen(PORT, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + PORT);
            } else {
                System.err.println("Failed to start server: " + result.cause());
            }
        });


    }
}
