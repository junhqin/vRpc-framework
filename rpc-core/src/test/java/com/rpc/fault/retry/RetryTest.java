package com.rpc.fault.retry;

import com.rpc.dto.RpcResponse;
import com.rpc.server.tcp.VertxTcpClient;
import org.junit.Test;

public class RetryTest {
    RetryStrategy retryStrategy1 = new NoRetryStrategy();
    RetryStrategy retryStrategy2 = new IntervalRetryStrategy();
    VertxTcpClient vertxTcpClient = new VertxTcpClient();

    @Test
    public void testNoStrategy(){
        try {
            RpcResponse rpcResponse = retryStrategy1.doRetry(()->{
                System.out.println("失败，不重试");
                RpcResponse rpcResponse1 = new RpcResponse();
                rpcResponse1.setMessage("模拟测试不重试");
                return rpcResponse1;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIntervalStrategy(){
        try{
            RpcResponse rpcResponse = retryStrategy2.doRetry(()->{
                System.out.println("..");
                RpcResponse rpcResponse1 = null;
                throw new RuntimeException("错误");
            });
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
