package com.ayom.myrpc.protocol;

import lombok.Getter;

/**
 * 协议消息的状态枚举
 */
@Getter
public enum ProtocolMessageStatusEnum {
    OK("ok",20),
    BAD_REQUEST("badRequest",40),
    BAD_RESPONSE("badResponse",50);

    private final String text;
    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举类
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getByValue(int value){
        ProtocolMessageStatusEnum[] values = ProtocolMessageStatusEnum.values();
        for (ProtocolMessageStatusEnum protocolMessageStatusEnum : values) {
            if(protocolMessageStatusEnum.value == value){
                return protocolMessageStatusEnum;
            }
        }
        return null;
    }
}
