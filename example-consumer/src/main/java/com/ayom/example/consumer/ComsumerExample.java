package com.ayom.example.consumer;

import com.ayom.myrpc.config.RpcConfig;
import com.ayom.myrpc.utils.ConfigUtils;

public class ComsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
