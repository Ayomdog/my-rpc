package com.ayom.myrpc.fault.retry;

import com.ayom.myrpc.model.RpcResponse;
import org.junit.Test;

import java.lang.invoke.VarHandle;

/**
 * 重试策略测试
 */
public class RetryStrategyTest {

    RetryStrategy retryStrategy = new NoReTryStrategy();

    @Test
    public void doRetry(){
        try{
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟测试失败");
            });
            System.out.println(rpcResponse);
        }catch(Exception e){
            System.out.println("重试多次失败");
            e.printStackTrace();
            String message = e.getMessage();
            System.out.println(message);
        }
    }

}
