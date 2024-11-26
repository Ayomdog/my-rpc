//package com.ayom.myrpc.protocol;
//
//import com.ayom.myrpc.serializer.Serializer;
//import com.ayom.myrpc.serializer.SerializerFactory;
//import io.vertx.core.buffer.Buffer;
//
//import java.io.IOException;
//
///**
// * 编码
// *
// */
//public class ProtocolMessageEncoder {
//
//    /**
//     * 编码
//     * @param protocolMessage
//     * @return
//     */
//    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
//        //判断是否为空
//        if (protocolMessage == null || protocolMessage.getHeader() == null) {
//            return Buffer.buffer();
//        }
//        ProtocolMessage.Header header = protocolMessage.getHeader();
//        Buffer buffer = Buffer.buffer();
//        //向buffer中依次写入字节
//        buffer.appendByte(header.getMagic());
//        buffer.appendByte(header.getVersion());
//        buffer.appendByte(header.getSerializer());
//        buffer.appendByte(header.getType());
//        buffer.appendByte(header.getStatus());
//        buffer.appendLong(header.getRequestId());
//        //获取序列化器
//        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getByKey(header.getSerializer());
//        if (serializerEnum == null) {
//            throw new RuntimeException("序列化协议不存在");
//        }
//        String value = serializerEnum.getValue();
//        Serializer serializer = SerializerFactory.getInstance(value);
//        byte[] serialize = serializer.serialize(protocolMessage.getBody());
//        //写入body的长度和数据
//        buffer.appendInt(header.getBodyLength());
//        buffer.appendBytes(serialize);
//        return buffer;
//    }
//}
package com.ayom.myrpc.protocol;

import com.ayom.myrpc.model.RpcRequest;
import com.ayom.myrpc.model.RpcResponse;
import com.ayom.myrpc.serializer.Serializer;
import com.ayom.myrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

public class ProtocolMessageEncoder {
    /**
     * 编码
     * @param message
     * @return
     * @throws IOException
     */
    public static Buffer encode(ProtocolMessage<?> message) throws IOException {
        ProtocolMessage.Header header = message.getHeader();
        Object body = message.getBody();

        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化消息的协议不存在");
        }
        String value = serializerEnum.getValue();
        Serializer serializer = SerializerFactory.getInstance(value);

        // 序列化消息体
        byte[] bodyBytes = serializer.serialize(body);
        header.setBodyLength(bodyBytes.length);

        // 创建缓冲区
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());
        //写入body的长度和数据
        buffer.appendInt(header.getBodyLength());
        buffer.appendBytes(bodyBytes);

        return buffer;
    }
}