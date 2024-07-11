package com.rpc.fault.tolerant;


import com.rpc.dto.RpcResponse;
import com.rpc.protocol.ProtocolMessageStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理异常（记录一条错误日志，然后正常返回一个对象）
 */
@Slf4j
public class FailSafeTolerantStrategy {
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.error("静默处理异常：", e);
        return new RpcResponse(null, null, "服务报错",e);
    }
}
