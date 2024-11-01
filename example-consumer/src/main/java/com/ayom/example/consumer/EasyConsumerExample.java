package com.ayom.example.consumer;

import com.ayom.example.common.model.User;
import com.ayom.example.common.service.UserService;
import com.ayom.myrpc.proxy.ServiceProxyFactory;

/**
 * 简易消费者示例
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        // 需要获取UserService的实现类对象
        //UserService userService = new UserServiceProxy();
        //通过代理工厂创建对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("jack");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        }else{
            System.out.println("user == null");
        }
    }
}
