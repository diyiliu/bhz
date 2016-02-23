package com.tiza.xgdl.command;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-07-15 15:05)
 * Version: 1.0
 * Updated:
 */
public abstract class BaseBeat implements Runnable {
    @Override
    public void run() {
        this.sendBeat();
    }

    public abstract void sendBeat();
}
