package com.ayom.myrpc.fault.tolerant;

import com.ayom.myrpc.model.RpcResponse;

import java.util.Map;

/**
 * 失败自动恢复
 *
 * 系统的某个功能出现调用失败或错误时,通过其他的方法,恢复该功能的正常,可以理解为降级
 * 比如重试,调用其他服务等
 */
public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 具体实现
        return null;
    }
}
