package com.tiza.xgdl.listener;


import com.tiza.xgdl.util.ListenerConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-04-21 10:59)
 * Version: 1.0
 * Updated:
 */
public class ListenerStart {

    private static ExecutorService pool = null;
    public static void start() {
        ListenerConfig[] config = ListenerConfig.values();
        pool = Executors.newFixedThreadPool(config.length);
        for (ListenerConfig val : config) {
            try {
                ListernInter listernInter = (ListernInter) val.getValue().newInstance();
                pool.submit(listernInter);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
