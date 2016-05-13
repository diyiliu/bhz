package com.tiza.xgdl.comm;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.util.DlUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:SendDataCom
 * @description:发送数据到COM端口
 * @date:2014/6/27 17:19
 */
public class SendDataCom implements Runnable {
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run() {
        while (true) {
            while (!Main.workQueue.isEmpty()) {
                final byte[] array = Main.workQueue.poll();
                LOGGER.debug("test ::: " + DlUtil.bytes2String(array));
                if (null == array || array.length == 0) {
                    continue;
                }
                if (Main.comCTX == null || !Main.comCTX.channel().isActive()) {
                    continue;
                }
                final int serial = array[5];
                final int orderId = array[6];
                final ByteBuf buf = Main.comCTX.alloc().buffer(array.length);
                buf.writeBytes(array);
                Main.comCTX.writeAndFlush(buf).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {

                        if (future.isSuccess()) {
                            LOGGER.debug("send info is ：" + DlUtil.bytes2String(array));
                            if (orderId == 0x0C) {
                                ConcurrentHashMap map = new ConcurrentHashMap();
                                map.put("sentTime", System.currentTimeMillis());
                                map.put("sentContent", array);
                                Main.resentMap.put(serial, map);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    LOGGER.error("thread error", e);
                                }
                            }
                        } else {
                            LOGGER.error("send info failed: {}", future.cause().getStackTrace());
                        }
                    }
                });

            }
            Thread.currentThread();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                LOGGER.error("thread error", e);
            }
        }
    }
}
