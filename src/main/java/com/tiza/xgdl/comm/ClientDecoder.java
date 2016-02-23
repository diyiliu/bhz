package com.tiza.xgdl.comm;

import com.tiza.xgdl.util.DlUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:ClientDecoder
 * @description:类的描述
 * @date:2014/5/13 14:30
 */
public class ClientDecoder extends ByteToMessageDecoder {
    Logger LOGGER = LoggerFactory.getLogger(ClientDecoder.class);
    private static final int HEAD_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            LOGGER.info("长度小于 4");
            return;
        }
        in.markReaderIndex();
        //校验包是否正常
        int firstIndex = in.indexOf(in.readerIndex(), in.writerIndex(), (byte) 0x7E);
        if (-1 == firstIndex) {
            LOGGER.info("未找到包头");
            return;
        }
        in.readerIndex(firstIndex);
        byte[] temp = new byte[3];
        in.readBytes(temp);
        int length = in.readUnsignedShort();
        int canRead = in.readableBytes();
        /*if (canRead <= 0) {
            LOGGER.info("长度小于 0 ");
            in.clear();
            return;
        }*/
        int tempLen = length + 5;
        if (canRead < tempLen) {
            LOGGER.info("读取长度不够");
            in.resetReaderIndex();
            return;
        }
        in.resetReaderIndex();
        in.readerIndex(firstIndex);

        int allLen = length + 10;
        byte[] array = new byte[allLen];
        in.readBytes(array);
        if (packIsllegal(array)) {
            ByteBuf buf = ctx.alloc().buffer(allLen);
            buf.writeBytes(array);
            out.add(buf);
        }
    }

    private boolean packIsllegal(byte[] bytes) {
        int length = bytes.length;
        int sumEnd = length - 3;
        int sum = 0;
        //判断校验和是否合法
        for (int i = 1; i < sumEnd; i++) {
            sum += DlUtil.getNoSin(bytes[i]);
        }

        if (DlUtil.getNoSin((byte) sum) != DlUtil.getNoSin(bytes[sumEnd])) {
            LOGGER.info("校验和非法");
            return false;
        }
        if (DlUtil.getNoSin(bytes[length - 1]) != 0x0A || DlUtil.getNoSin(bytes[length - 2]) != 0x0D) {
            LOGGER.info("包结尾非法");
            return false;
        }
        return true;
    }
}
