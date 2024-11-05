package com.ayom.example.provider;

import com.ayom.example.common.service.UserService;
import com.ayom.myrpc.RpcApplication;
import com.ayom.myrpc.config.RpcConfig;
import com.ayom.myrpc.registry.LocalRegistry;
import com.ayom.myrpc.server.VertxHttpServer;

public class ProviderExample {
    public static void main(String[] args) {
        //rpc框架初始化
        RpcApplication.init();

        //注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        //启动web服务
        VertxHttpServer server = new VertxHttpServer();
        server.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
