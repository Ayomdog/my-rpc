package com.ayom.myrpc.fault.tolerant;

/**
 * 容错策略键名
 */
public interface TolerantStrategyKeys {

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 故障转移
     */
    String FAIL_OVER = "failOver";

    /**
     * 静默处理
     */
    String FAIL_SAFE = "fail_safe";

    /**
     * 故障恢复
     */
    String FAIL_BACK = "failBack";

}
