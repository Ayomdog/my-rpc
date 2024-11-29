package com.ayom.myrpc.config;

import com.ayom.myrpc.fault.retry.RetryStrategyKeys;
import com.ayom.myrpc.fault.tolerant.TolerantStrategyKeys;
import com.ayom.myrpc.loadbalancer.LoadBalanceKeys;
import com.ayom.myrpc.loadbalancer.LoadBalancer;
import com.ayom.myrpc.loadbalancer.RoundRobinLoadBalancer;
import com.ayom.myrpc.serializer.Serializer;
import com.ayom.myrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC框架配置
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "my-rpc";
    /**
     * 版本号
     */
    private String version = "1.0";
    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";
    /**
     * 服务器端口号
     */
    private Integer serverPort = 80;
    /**
     * 模拟调用
     */
    private boolean mock = false;
    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;
    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalanceKeys.ROUND_ROBIN;
    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;
    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
