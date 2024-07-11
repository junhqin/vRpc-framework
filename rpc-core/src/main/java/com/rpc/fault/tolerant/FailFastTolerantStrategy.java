package com.rpc.fault.tolerant;

import com.rpc.dto.RpcResponse;

import java.util.Map;

/**
 * 快速失败策略（立刻通知外层调用方）
 */
public class FailFastTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错", e);
    }
}