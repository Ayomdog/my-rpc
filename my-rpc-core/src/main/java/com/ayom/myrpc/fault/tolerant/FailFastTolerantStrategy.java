package com.ayom.myrpc.fault.tolerant;

import com.ayom.myrpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败容错策略
 *
 * 系统出现错误调用时,立刻报错,交给外层调用方法
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错",e);
    }
}
