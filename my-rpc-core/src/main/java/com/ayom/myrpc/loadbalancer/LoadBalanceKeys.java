package com.ayom.myrpc.loadbalancer;

/**
 * 负载均衡器键名常量
 */
public interface LoadBalanceKeys {

    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";
    /**
     * 随机
     */
    String RANDOM = "random";
    /**
     * 一致性Hash
     */
    String CONSISTENT_HASH = "consistentHash";

}
