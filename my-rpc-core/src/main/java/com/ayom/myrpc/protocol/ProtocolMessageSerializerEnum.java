package com.ayom.myrpc.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议消息序列化器枚举
 */
@Getter
public enum ProtocolMessageSerializerEnum {
    JDK(0,"jdk"),
    JSON(1,"json"),
    KRYO(2,"kryo"),
    HESSIAN(3,"hessian");

    private final int key;
    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
    /**
     * 获取值列表
     */
    public static List<String> getValues(){
        List<String> values = Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
        return values;
    }

    /**
     * 根据key获取枚举
     */
    public static ProtocolMessageSerializerEnum getByKey(int key){
        ProtocolMessageSerializerEnum[] values = ProtocolMessageSerializerEnum.values();
        for (ProtocolMessageSerializerEnum value : values) {
            if(value.key == key){
                return value;
            }
        }
        return null;
    }

    /**
     * 根据value获取枚举
     */
    public static ProtocolMessageSerializerEnum getByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        ProtocolMessageSerializerEnum[] values = ProtocolMessageSerializerEnum.values();
        for (ProtocolMessageSerializerEnum protocolMessageSerializerEnum : values) {
            if(protocolMessageSerializerEnum.value == value){
                return protocolMessageSerializerEnum;
            }
        }
        return null;
    }
}
