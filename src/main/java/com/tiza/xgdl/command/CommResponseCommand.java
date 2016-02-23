package com.tiza.xgdl.command;

import com.tiza.xgdl.Main;

import java.nio.ByteBuffer;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-10-10 15:48)
 * Version: 1.0
 * Updated:
 */
public class CommResponseCommand extends BaseCommand {
    @Override
    public void decodeMessage(ByteBuffer buffer) {

    }

    @Override
    public Boolean decodeMessageRet(ByteBuffer buffer, byte[] oth, int length, int serial) {
        byte status = buffer.get();
        //接收成功 ，删除缓存中的内容
        if (status == (byte)0xFF) {
            if (Main.resentMap.containsKey(serial)) {
                Main.resentMap.remove(serial);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}