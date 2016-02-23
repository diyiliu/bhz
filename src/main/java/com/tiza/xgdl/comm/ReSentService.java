package com.tiza.xgdl.comm;

import com.tiza.xgdl.Main;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-10-10 15:38)
 * Version: 1.0
 * Updated:
 */
public class ReSentService implements Runnable {
    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run() {
        System.out.println("缓存数据 ："+Main.resentMap.size());
        for (Map.Entry<Integer, ConcurrentHashMap> entry : Main.resentMap.entrySet()) {
            long nowTime = System.currentTimeMillis();
            ConcurrentHashMap tempMap = entry.getValue();
            long sentTime = (Long) tempMap.get("sentTime");
            byte[] content = (byte[]) tempMap.get("sentContent");
            //重发
            if (((nowTime - sentTime) / 1000) > 10) {
                final ByteBuf buf = Main.comCTX.alloc().buffer(content.length);
                buf.writeBytes(content);
                Main.comCTX.writeAndFlush(buf);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    LOGGER.error("thread error", e);
                }

            }
        }
    }
}
