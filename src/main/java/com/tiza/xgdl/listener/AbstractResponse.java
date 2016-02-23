package com.tiza.xgdl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-12-30 14:33)
 * Version: 1.0
 * Updated:
 */
public abstract class AbstractResponse implements ListernInter {
    private static Logger logger = LoggerFactory.getLogger(AbstractResponse.class);


    //@Override
    public void run() {
        while (true) {
            try {
                dealMessage();
                synchronized (this) {
                    this.wait(getCheckTime());
                }
            } catch (InterruptedException e) {
                logger.error("Thread to be interrupted ", e);
            } catch (Exception e) {
                logger.error("Storage failure", e);
            }
        }
    }

    public abstract long getCheckTime();



}
