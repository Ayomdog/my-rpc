package com.ayom.myrpc.server.tcp;

import com.ayom.myrpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
                String testMessage = "hello,server!hello,server!hello,server!";
                int length = testMessage.getBytes().length;

                //构造Parser
                RecordParser parser = RecordParser.newFixed(length);
                parser.setOutput(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        String str = new String(buffer.getBytes());
                        System.out.println(str);
                        if(testMessage.equals(str)){
                            System.out.println("good");
                        }
                    }
                });
                socket.handler(parser);
//                if(buffer.getBytes().length < length){
////                    System.out.println("半包，length=" + buffer.getBytes().length);
//                    log.info("半包，length={}",buffer.getBytes().length);
//                    return;
//                }
//                if(buffer.getBytes().length > length){
////                    System.out.println("粘包，length=" + buffer.getBytes().length);
//                    log.info("粘包，length={}",buffer.getBytes().length);
//                    return;
//                }
//                String str = new String(buffer.getBytes(0,length));
//                System.out.println(str);
//                if(testMessage.equals(str)){
////                    System.out.println("good");
//                    log.info("good");
//                }
//                //处理接受到的字节数组
//                byte[] bytes = buffer.getBytes();
//                byte[] responseData = handleRequest(bytes);
//                //发送响应
//                socket.write(Buffer.buffer(responseData));
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
