package com.ayom.myrpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.ayom.myrpc.RpcApplication;
import com.ayom.myrpc.model.RpcRequest;
import com.ayom.myrpc.model.RpcResponse;
import com.ayom.myrpc.model.ServiceMetaInfo;
import com.ayom.myrpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class VertxTcpClient {

    public void start(){
        //创建Vertx实例
        Vertx vertx = Vertx.vertx();
        vertx.createNetClient().connect(80,"localhost",result ->{
            if(result.succeeded()){
                System.out.println("Connnected to TCP Server");
                NetSocket socket = result.result();
                //发送数据
                //socket.write("hello,server!");
                //接受响应
                for (int i = 0; i < 1000; i++) {
                    // 发送数据
                    socket.write("Hello, server!Hello, server!Hello, server!Hello, server!");
                }
                // 接收响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server:" + buffer.toString());
                });
            }else{
                System.out.println("Failed to connect to TCP Server");
            }
        });
    }

    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(),serviceMetaInfo.getServiceHost(),result ->{
            if(!result.succeeded()){
                System.err.println("Failed to connect to TCP server");
                return;
            }
            NetSocket socket = result.result();
            // 构造消息
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            //生成全局请求ID
            header.setRequestId(IdUtil.getSnowflakeNextId());
            protocolMessage.setHeader(header);
            protocolMessage.setBody(rpcRequest);
            //编码请求
            try{
                Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误");
            }
            //接受响应
            TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                //解码
                try {
                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                } catch (IOException e) {
                    throw new RuntimeException("协议消息解码错误");
                }
            });
            socket.handler(tcpBufferHandlerWrapper);
        });
        RpcResponse rpcResponse = responseFuture.get();
        netClient.close();
        return rpcResponse;
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
