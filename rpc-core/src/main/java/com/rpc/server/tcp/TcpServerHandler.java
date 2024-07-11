package com.rpc.server.tcp;

import com.rpc.dto.RpcRequest;
import com.rpc.dto.RpcResponse;
import com.rpc.protocol.ProtocolMessage;
import com.rpc.protocol.ProtocolMessageDecoder;
import com.rpc.protocol.ProtocolMessageEncoder;
import com.rpc.protocol.ProtocolMessageStatusEnum;
import com.rpc.protocol.ProtocolMessageTypeEnum;
import com.rpc.registry.impl.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.Method;

public class TcpServerHandler implements Handler<NetSocket> {

    @Override
    public void handle(NetSocket netSocket) {
        //处理连接
        TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            ProtocolMessage<RpcRequest> message;
            try{
                message = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RpcRequest request = message.getBody();

            //构造响应结果
            RpcResponse response = new RpcResponse();
            ProtocolMessage.Header header = message.getHeader();
            try{
                //反射调用服务实现类并封装返回结果
                Class<?> implClass = LocalRegistry.get(request.getServiceName());
                Method method = implClass.getMethod(request.getMethodName(), request.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), request.getArgs());
                response.setData(result);
                response.setDataType(method.getReturnType());
                response.setMessage("OK");
                header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
            } catch (Exception e) {
                e.printStackTrace();
                response.setMessage(e.getMessage());
                response.setException(e);
                header.setStatus((byte) ProtocolMessageStatusEnum.BAD_RESPONSE.getValue());
            }

            //发送响应
            header.setType((byte)ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> responseProtocolMessage = new ProtocolMessage<RpcResponse>(header, response);
            try{
                Buffer encode = ProtocolMessageEncoder.encode(responseProtocolMessage);
                System.out.println("success send response:"+responseProtocolMessage);
                netSocket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        netSocket.handler(tcpBufferHandlerWrapper);
    }
}
