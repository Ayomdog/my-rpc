package com.ayom.myrpc.bootstrap;

import com.ayom.myrpc.RpcApplication;
import com.ayom.myrpc.config.RegistryConfig;
import com.ayom.myrpc.config.RpcConfig;
import com.ayom.myrpc.model.ServiceMetaInfo;
import com.ayom.myrpc.model.ServiceRegisterInfo;
import com.ayom.myrpc.registry.LocalRegistry;
import com.ayom.myrpc.registry.Registry;
import com.ayom.myrpc.registry.RegistryFactory;
import com.ayom.myrpc.server.VertxHttpServer;

import java.util.List;

public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        //RPC框架初始化
        RpcApplication.init();

        //全局配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            //本地注册
            LocalRegistry.register(serviceName,serviceRegisterInfo.getImplClass());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + "服务注册失败",e);
            }

        }
        //启动服务器
        VertxHttpServer server = new VertxHttpServer();
        server.doStart(rpcConfig.getServerPort());
    }
}
