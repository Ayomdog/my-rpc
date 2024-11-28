package com.ayom.myrpc.loadbalancer;

import com.ayom.myrpc.spi.SpiLoader;

/**
 * 负载均衡器工厂(工厂模式,用于获取负载均衡器对象)
 */
public class LoadBalancerFactory {
    static {
        SpiLoader.load(LoadBalancer.class);
    }
    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RandomLoadBalancer();

    /**
     * 获取实例
     */
    public static LoadBalancer getInstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
