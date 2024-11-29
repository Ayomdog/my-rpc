package com.ayom.myrpc.fault.tolerant;

import com.ayom.myrpc.model.RpcResponse;

import java.util.Map;

/**
 * 故障转移
 *
 * 一次调用失败后,切换一个其他节点再次进行调用
 */
public class FailOverTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 具体实现
        return null;
    }
}
