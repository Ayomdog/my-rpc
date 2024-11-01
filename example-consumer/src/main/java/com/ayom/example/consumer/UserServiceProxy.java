package com.ayom.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ayom.example.common.model.User;
import com.ayom.example.common.service.UserService;
import com.ayom.myrpc.model.RpcRequest;
import com.ayom.myrpc.model.RpcResponse;
import com.ayom.myrpc.serializer.JdkSerializer;
import com.ayom.myrpc.serializer.Serializer;

import java.io.IOException;

public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        //1.指定序列化器
        Serializer serializer = new JdkSerializer();
        //2.发请求
        //2.1封装一个RpcRequest对象
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .parameterTypes(new Class[]{User.class})
                .methodName("getUser")
                .args(new Object[]{user})
                .build();
        //2.2序列化RpcRequest
        try {
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            //2.3发送请求
            try(HttpResponse response = HttpRequest.post("http://localhost:80")
                    .body(bytes)
                    .execute()){
                result = response.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
