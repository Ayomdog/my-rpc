package com.ayom.myrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ayom.myrpc.RpcApplication;
import com.ayom.myrpc.config.RpcConfig;
import com.ayom.myrpc.constant.RpcConstant;
import com.ayom.myrpc.model.RpcRequest;
import com.ayom.myrpc.model.RpcResponse;
import com.ayom.myrpc.model.ServiceMetaInfo;
import com.ayom.myrpc.protocol.*;
import com.ayom.myrpc.registry.Registry;
import com.ayom.myrpc.registry.RegistryFactory;
import com.ayom.myrpc.serializer.Serializer;
import com.ayom.myrpc.serializer.SerializerFactory;
import com.ayom.myrpc.server.tcp.VertxTcpClient;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 服务代理（JDK 动态代理）
 *
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            // 发送 TCP 请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
            return rpcResponse.getData();
            //    Vertx vertx = Vertx.vertx();
        //    NetClient netClient = vertx.createNetClient();
        //    CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        //    netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(),
        //            result -> {
        //                if (result.succeeded()) {
        //                    System.out.println("Connected to TCP server");
        //                    io.vertx.core.net.NetSocket socket = result.result();
        //                    // 发送数据
        //                    // 构造消息
        //                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
        //                    ProtocolMessage.Header header = new ProtocolMessage.Header();
        //                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        //                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        //                    header.setSerializer((byte) ProtocolMessageSerializerEnum.getByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
        //                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        //                    header.setRequestId(IdUtil.getSnowflakeNextId());
        //                    protocolMessage.setHeader(header);
        //                    protocolMessage.setBody(rpcRequest);
        //                    // 编码请求
        //                    try {
        //                        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
        //                        socket.write(encodeBuffer);
        //                    } catch (IOException e) {
        //                        throw new RuntimeException("协议消息编码错误");
        //                    }
        //
        //                    // 接收响应
        //                    socket.handler(buffer -> {
        //                        try {
        //                            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
        //                            responseFuture.complete(rpcResponseProtocolMessage.getBody());
        //                        } catch (IOException e) {
        //                            throw new RuntimeException("协议消息解码错误");
        //                        }
        //                    });
        //                } else {
        //                    System.err.println("Failed to connect to TCP server");
        //                }
        //            });
        //
        //    RpcResponse rpcResponse = responseFuture.get();
        //    // 记得关闭连接
        //    netClient.close();
        //    return rpcResponse.getData();
        } catch (IOException e) {
            throw new RuntimeException("调用失败");
        }
    }
}
