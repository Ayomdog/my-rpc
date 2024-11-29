package com.ayom.example.consumer;

import com.ayom.example.common.model.User;
import com.ayom.example.common.service.UserService;
import com.ayom.myrpc.bootstrap.ConsumerBootstrap;
import com.ayom.myrpc.config.RpcConfig;
import com.ayom.myrpc.proxy.ServiceProxy;
import com.ayom.myrpc.proxy.ServiceProxyFactory;
import com.ayom.myrpc.utils.ConfigUtils;

public class ComsumerExample {
    public static void main(String[] args) {
        //服务提供者初始化
        ConsumerBootstrap.init();

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("hejinjie");
        User user1 = userService.getUser(user);
        if(user1 != null){
            System.out.println(user1.getName());
        }else{
            System.out.println("user == null");
        }

    }
}
