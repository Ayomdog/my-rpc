package com.ayom.example.provider;

import com.ayom.example.common.service.UserService;
import com.ayom.myrpc.registry.LocalRegistry;
import com.ayom.myrpc.server.VertxHttpServer;

public class EasyProviderExample {
    public static void main(String[] args) {
        //VertxHttpServe serve = new VertxHttpServe();
        //serve.doStart(80);
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        VertxHttpServer server = new VertxHttpServer();
        server.doStart(80);
    }
}
