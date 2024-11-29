package com.ayom.myrpc.fault.retry;

/**
 * 重试策略键名
 */
public interface RetryStrategyKeys {
    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定时间间隔重试
     */
    String FIXED_INTERVAL = "fixedInterval";

}
