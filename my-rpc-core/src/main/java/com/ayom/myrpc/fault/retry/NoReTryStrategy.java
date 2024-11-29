package com.ayom.myrpc.fault.retry;

import com.ayom.myrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试 - 重试策略
 */
public class NoReTryStrategy implements RetryStrategy{
    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        //RpcResponse call = callable.call();
        return callable.call();
    }
}
