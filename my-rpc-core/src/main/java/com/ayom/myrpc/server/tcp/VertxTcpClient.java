package com.ayom.myrpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

public class VertxTcpClient {

    public void start(){
        //创建Vertx实例
        Vertx vertx = Vertx.vertx();
        vertx.createNetClient().connect(80,"localhost",result ->{
            if(result.succeeded()){
                System.out.println("Connnected to TCP Server");
                NetSocket socket = result.result();
                //发送数据
//                socket.write("hello,server!");
                for(int i =0;i < 100;i++){
                    socket.write("hello,server!hello,server!hello,server!");
                }
                //接受响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server:" + buffer.toString());
                });
            }else{
                System.out.println("Failed to connect to TCP Server");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
