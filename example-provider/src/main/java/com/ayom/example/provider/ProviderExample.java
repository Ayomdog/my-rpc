package com.ayom.example.provider;

import com.ayom.example.common.service.UserService;
import com.ayom.myrpc.RpcApplication;
import com.ayom.myrpc.config.RegistryConfig;
import com.ayom.myrpc.config.RpcConfig;
import com.ayom.myrpc.model.ServiceMetaInfo;
import com.ayom.myrpc.registry.LocalRegistry;
import com.ayom.myrpc.registry.Registry;
import com.ayom.myrpc.registry.RegistryFactory;
import com.ayom.myrpc.server.VertxHttpServer;
import com.ayom.myrpc.server.tcp.VertxTcpServer;

public class ProviderExample {
    public static void main(String[] args) {
        //rpc框架初始化
        RpcApplication.init();

        //注册服务
        //LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName,UserServiceImpl.class);
        //注册服务到服务中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try{
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //启动web服务
//        VertxHttpServer server = new VertxHttpServer();
//        server.doStart(RpcApplication.getRpcConfig().getServerPort());

        //启动TPC服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(80);
    }
}
