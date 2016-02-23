package com.tiza.xgdl.command;

import java.nio.ByteBuffer;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-07-31 15:09)
 * Version: 1.0
 * Updated:
 */
public class SerialCommand extends BaseCommand {
    class SendBeat extends BaseBeat {

        public boolean isNeedSend() {
            return needSend;
        }

        public void setNeedSend(boolean needSend) {
            this.needSend = needSend;
        }

        private boolean needSend = false;

        @Override
        public void run() {
            sendBeat();
        }

        @Override
        public void sendBeat() {

        }

    }

    @Override
    public void decodeMessage(ByteBuffer buffer) {

    }

    @Override
    public Object decodeMessageRet(ByteBuffer buffer, byte[] oth ,int length ,int serial) {
        return null;
    }
}
