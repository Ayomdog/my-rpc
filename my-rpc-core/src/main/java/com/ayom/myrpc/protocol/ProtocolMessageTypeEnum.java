package com.ayom.myrpc.protocol;

import lombok.Getter;

/**
 * 协议消息类型枚举
 */
@Getter
public enum ProtocolMessageTypeEnum {
    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据key进行枚举
     * @param key
     * @return
     */
    public static ProtocolMessageTypeEnum getByKey(int key){
        ProtocolMessageTypeEnum[] values = ProtocolMessageTypeEnum.values();
        for (ProtocolMessageTypeEnum value : values) {
            if(value.key == key){
                return value;
            }
        }
        return null;
    }
}
