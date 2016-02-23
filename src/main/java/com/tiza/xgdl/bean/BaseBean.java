package com.tiza.xgdl.bean;

import com.tiza.xgdl.util.DlUtil;

import java.nio.ByteBuffer;

/**
 * Description: 实时采集1  实时采集2 配比单 AlarmInfo
 * Author: Wolf
 * Created:Wolf-(2014-09-29 15:35)
 * Version: 1.0
 * Updated:
 */
public abstract class BaseBean {
    public abstract void sendArray() throws Exception;

    public void sendNomArray(String val){
        byte[] zipData = DlUtil.gZip(val);
        ByteBuffer buffer = ByteBuffer.allocate(2+zipData.length);
        buffer.put((byte) 0x0C);
        buffer.put(zipData);
        buffer.put((byte)DlUtil.checkSum(zipData));
        buffer.flip();
        DlUtil.pack(buffer.array());
    }
}
