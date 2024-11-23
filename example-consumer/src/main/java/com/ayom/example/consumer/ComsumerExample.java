package com.ayom.example.consumer;

import com.ayom.example.common.model.User;
import com.ayom.example.common.service.UserService;
import com.ayom.myrpc.config.RpcConfig;
import com.ayom.myrpc.proxy.ServiceProxyFactory;
import com.ayom.myrpc.utils.ConfigUtils;

public class ComsumerExample {
    public static void main(String[] args) {
        //RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        //System.out.println(rpc);
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("hejinjie");
        User user3 = new UserServiceProxy().getUser(user);
        for(int i = 0;i < 3;i++){
            User user1 = userService.getUser(user);
        }
        User user2 = userService.getUser(user);
        if(user3 != null){
            System.out.println(user3.getName());
        }else{
            System.out.println("user == null");
        }
        short number = userService.getNumber();
        System.out.println(number);
    }
}
