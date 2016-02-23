package com.tiza.xgdl.util;

import com.tiza.xgdl.listener.impl.CheckHeartBeatImpl;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-05-21 10:15)
 * Version: 1.0
 * Updated:
 */
public enum ListenerConfig {
    CHECK_HEART_BEAT(CheckHeartBeatImpl.class);
    private Class classType;

    ListenerConfig(Class baseCommand) {
        this.classType = baseCommand;
    }

    @Override
    public String toString() {
        return "class is :" + classType.getName();
    }

    public Class getValue() {
        return this.classType;
    }
}
