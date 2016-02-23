package com.tiza.xgdl.command;

import java.nio.ByteBuffer;

/**
 * Description: 指令接口
 * Author: Wolf
 * Created:Wolf-(2014-07-17 14:09)
 * Version: 1.0
 * Updated:
 */
public abstract class BaseCommand<T> {
    /**
     *
     * @param buffer
     */
    public abstract void decodeMessage(ByteBuffer buffer);

    /**
     *
     * @param buffer
     * @return
     */
    public abstract T decodeMessageRet(ByteBuffer buffer , byte[] head ,int length ,int serial);
}
