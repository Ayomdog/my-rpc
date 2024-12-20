package com.ayom.myrpc.server.tcp;

import com.ayom.myrpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;


/**
 * 装饰者模式(使用RecordParser,对原有的buffer处理能力进行增强)
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }
    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        //构造Parser
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<Buffer>() {
            //初始化
            int size = -1;
            Buffer resultBuffer = Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                if(-1 == size){
                    size = buffer.getInt(13);
                    parser.fixedSizeMode(size);
                    //写入头消息到结果
                    resultBuffer.appendBuffer(buffer);
                }else{
                    //写入体信息到结果
                    resultBuffer.appendBuffer(buffer);

                    bufferHandler.handle(resultBuffer);
                    //重置
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    resultBuffer = Buffer.buffer();

                }
            }
        });
        return parser;
    }


}
