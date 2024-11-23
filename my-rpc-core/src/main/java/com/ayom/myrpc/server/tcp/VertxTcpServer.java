package com.ayom.myrpc.server.tcp;

import com.ayom.myrpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData){
        return "hello,client".getBytes();
    }

    @Override
    public void doStart(int port) {
        //创建Vertx实例
        Vertx vertx = Vertx.vertx();
        //创建TCP服务器
        NetServer server = vertx.createNetServer();

        //处理请求
        server.connectHandler(socket -> {
            //处理连接
            socket.handler(buffer -> {
                //处理接受到的字节数组
                byte[] bytes = buffer.getBytes();
                byte[] responseData = handleRequest(bytes);
                //发送响应
                socket.write(Buffer.buffer(responseData));
            });
        });

        //启动TCP服务器并监听指定端口
        server.listen(port,result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port " + port);
            }else{
                System.out.println("Failed to start TCP server:" + result.cause());
            }
        });

    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(80);
    }
}
