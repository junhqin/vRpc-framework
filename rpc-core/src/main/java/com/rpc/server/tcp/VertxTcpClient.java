package com.rpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.rpc.RpcApplication;
import com.rpc.dto.RpcRequest;
import com.rpc.dto.RpcResponse;
import com.rpc.dto.ServiceMetaInfo;
import com.rpc.loadbalancer.ConsistentHashLoadBalancer;
import com.rpc.loadbalancer.LoadBalancer;
import com.rpc.protocol.ProtocolConstant;
import com.rpc.protocol.ProtocolMessage;
import com.rpc.protocol.ProtocolMessageDecoder;
import com.rpc.protocol.ProtocolMessageEncoder;
import com.rpc.protocol.ProtocolMessageSerializerEnum;
import com.rpc.protocol.ProtocolMessageTypeEnum;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.List;
/**
 * 将之前的serviceProxy类单独封装成Vertx TCP 请求客户端
 */
@Slf4j
public class VertxTcpClient {

    public static RpcResponse dpRequest(RpcRequest rpcRequest, List<ServiceMetaInfo> serviceMetaInfoList) throws ExecutionException, InterruptedException {
        //一致性哈希负载均衡
        LoadBalancer loadBalancer = new ConsistentHashLoadBalancer(serviceMetaInfoList);
        Long requestId = IdUtil.getSnowflakeNextId();
        ServiceMetaInfo serviceMetaInfo = loadBalancer.select(String.valueOf(requestId));
        //发送TCP请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseCompletableFuture = new CompletableFuture<>();



        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(),
                result -> {
                    if (result.succeeded()) {
                        log.info("Connected to TCP server!");
                        NetSocket socket = result.result();
                        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                        ProtocolMessage.Header header = new ProtocolMessage.Header();
                        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                        header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                        header.setRequestId(requestId);
                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                        protocolMessage.setHeader(header);
                        protocolMessage.setBody(rpcRequest);
                        try {
                            Buffer encoderBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                            socket.write(encoderBuffer);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        TcpBufferHandlerWrapper tcpBufferHandlerWrapper;
                        tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                            try {
                                ProtocolMessage<RpcResponse> rpcResponseProtocolMessage =
                                        (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                responseCompletableFuture.complete(rpcResponseProtocolMessage.getBody());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        socket.handler(tcpBufferHandlerWrapper);
                    }
                });
        RpcResponse rpcResponse = responseCompletableFuture.get();
        netClient.close();
        return rpcResponse;
    }
    public static void main(String[] args) {
        Vertx vertx =Vertx.vertx();
        vertx.createNetClient().connect(9998, "localhost", result ->{
            if(result.succeeded()){
                System.out.println("Connected to TCP server");
                io.vertx.core.net.NetSocket socket = result.result();
                for(int i=0; i<100 ;i++){
                    socket.write("Hello, server!Hello, server!Hello, server!");
                }
                //发送数据
                socket.handler(buffer -> {
                    System.out.println("received response from server: "+ buffer.toString());
                });
            }else{
                System.err.println("Failed to connect!");
            }
        });
    }
}
