package com.ayom.myrpc.protocol;

import com.ayom.myrpc.model.RpcRequest;
import com.ayom.myrpc.model.RpcResponse;
import com.ayom.myrpc.serializer.Serializer;
import com.ayom.myrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * 解码
 */
public class ProtocolMessageDecoder {

    /**
     * 解码
     * @return
     */
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        if(magic != ProtocolConstant.PROTOCOL_MAGIC){
            throw new RuntimeException("消息magic非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));
        //解决粘包问题，只读指定长度的数据
        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
        //解析消息体
        //a.获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getByKey(header.getSerializer());
        if(serializerEnum == null){
            throw new RuntimeException("序列化消息的协议不存在");
        }
        String value = serializerEnum.getValue();
        Serializer serializer = SerializerFactory.getInstance(value);
        //b.获取解析的消息类型
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getByKey(header.getType());
        if(messageTypeEnum == null){
            throw new RuntimeException("序列化消息的类型不存在");
        }
        switch (messageTypeEnum){
            case REQUEST :
                RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header,rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header,rpcResponse);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("暂不支持该类型的消息");
        }
    }
    
}
