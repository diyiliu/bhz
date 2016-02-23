package com.tiza.xgdl.command;

import com.tiza.xgdl.util.DlUtil;
import com.tiza.xgdl.util.PwdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-07-31 11:13)
 * Version: 1.0
 * Updated:
 */
public class SpecialHeartBeat extends BaseCommand {
    protected static Logger LOGGER = LoggerFactory.getLogger(SpecialHeartBeat.class);
    private static final int key1 = 21;
    private static final int key2 = 7;
    private static final int key3 = 13;
    //保存上次发送的
    public static ConcurrentLinkedQueue<byte[]> queue = new ConcurrentLinkedQueue();

     static class SendBeat extends BaseBeat {
        protected Logger LOGGER = LoggerFactory.getLogger(SendBeat.class);
        @Override
        public void sendBeat() {
            ByteBuffer buffer = ByteBuffer.allocate(7);
            buffer.put((byte) 0x1A);
            byte[] bytes = DlUtil.getRandomBytes(6, 15);
            LOGGER.info("pwd is:" + DlUtil.bytes2String(bytes));
            addPwd(bytes);
            //加密
            byte[] enBytes = PwdUtils.encode(bytes, key1, key2, key3);
            buffer.put(enBytes);
            LOGGER.info("sent heart beat is:" + DlUtil.bytes2String(enBytes));
            //App.comCTX.writeAndFlush(buffer);
            DlUtil.pack(buffer.array());
        }
    }

    public static SendBeat getSendBeat(){
        return new SendBeat();
    }

    private static void addPwd(byte[] bytes) {
        if (!queue.isEmpty()) {
            queue.clear();
        }
        queue.offer(bytes);
    }

    public static byte[] getPwd() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.poll();
    }

    public static boolean comparePwd(byte[] bytes) {
        boolean isEqual = true;
        byte[] lastPwd = getPwd();
        if (null == lastPwd || null == bytes) {
            return false;
        }

        LOGGER.info("compare pwd ,lastPwd is:" + DlUtil.bytes2String(lastPwd) + ",get pwd is :" + DlUtil.bytes2String(bytes));
        for (int i = 0, len = bytes.length; i < len; i++) {
            if (bytes[i] != lastPwd[i]) {
                isEqual = false;
                break;
            }
        }
        return isEqual;
    }

    @Override
    public void decodeMessage(ByteBuffer buffer) {

    }

    @Override
    public Boolean decodeMessageRet(ByteBuffer buffer, byte[] oth ,int length ,int serial) {
        byte[] bytes = buffer.array();
        boolean isEqual = true;
        byte[] lastPwd = this.getPwd();
        if (null == lastPwd || null == bytes) {
            return false;
        }

        LOGGER.info("compare pwd ,lastPwd is:" + DlUtil.bytes2String(lastPwd) + ",get pwd is :" + DlUtil.bytes2String(bytes));
        for (int i = 0, len = bytes.length; i < len; i++) {
            if (bytes[i] != lastPwd[i]) {
                isEqual = false;
                break;
            }
        }
        LOGGER.info("校验心跳的结果为：" + isEqual);
        return isEqual;
    }
}
