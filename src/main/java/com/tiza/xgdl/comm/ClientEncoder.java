package com.tiza.xgdl.comm;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:ClientEncoder
 * @description:消息编码
 * @date:2014/5/13 14:04
 */
public class ClientEncoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (null == msg) {
            return;
        }
        out.writeBytes(msg);
    }
}
