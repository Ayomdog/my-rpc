package com.ayom.myrpc.fault.tolerant;

import com.ayom.myrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理异常
 *
 * 系统出现部分非重要功能的异常时,直接忽略掉,不做任何处理,就像错误没发生过一样
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("服务错误",e);
        return new RpcResponse();
    }
}
