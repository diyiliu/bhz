package com.tiza.xgdl.util;

import com.tiza.xgdl.command.*;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-07-17 14:37)
 * Version: 1.0
 * Updated:
 */
public enum CommandConfig {
    COMMAN_HEARTBEAT(0xAA, CommonHeartBeat.class),
    SPECIAL_HEARTBEAT(0x1A, SpecialHeartBeat.class),
    LOCK_COMMAND(0x0B,LockCommand.class),
    SERIAL_COMMAND(0x0E,SerialCommand.class),
    COMM_RESPONSE_COMMAND(0x0C,CommResponseCommand.class),
    ;

    private Class classType;
    private int key;

    CommandConfig(int i, Class baseCommand) {
        this.key = i;
        this.classType = baseCommand;
    }

    @Override
    public String toString() {
        return "order id :" + this.key + ", class is :" + classType.getName();
    }

    public Class getValue() {
        return this.classType;
    }
    public int getKey(){
        return this.key;
    }
}
