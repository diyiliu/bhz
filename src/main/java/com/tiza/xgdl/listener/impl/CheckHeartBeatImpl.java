package com.tiza.xgdl.listener.impl;

import com.tiza.xgdl.listener.AbstractResponse;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-05-21 10:32)
 * Version: 1.0
 * Updated:
 */
public class CheckHeartBeatImpl extends AbstractResponse {
    private final long checkTime = 10*1000;
    @Override
    public long getCheckTime() {
        return checkTime;
    }

    @Override
    public void dealMessage() {
        System.out.println(123);
    }
}
