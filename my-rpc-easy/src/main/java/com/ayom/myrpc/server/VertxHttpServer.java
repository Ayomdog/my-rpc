package com.ayom.myrpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        //1.创建vert.x实例
        Vertx vertx = Vertx.vertx();
        //2.创建http服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();
        //3.监听端口 并处理请求
        //server.requestHandler(request -> {
        //    //3.1处理http请求
        //    System.out.println("Received request" + request.method() + " " + request.uri());
        //    //3.2发送http响应
        //    request.response()
        //            .putHeader("context-type","text/plain")
        //            .end("Hello from Vert.x Http serve!");
        //});
        HttpServerHandler httpServerHandler = new HttpServerHandler();
        server.requestHandler(httpServerHandler);
        //4.启动htt服务器并监听指定端口
        server.listen(port,result ->{
            if(result.succeeded()){
                System.out.println("Serve is now listening on port "+ port);
            }else{
                System.out.println("failed to start server:"+result.cause());
            }
        });
    }
}
