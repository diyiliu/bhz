package com.tiza.xgdl.command;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.command.todb.HeartBeatToDb;
import com.tiza.xgdl.util.DlUtil;
import com.tiza.xgdl.util.PwdUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description: 普通心跳，校验时间，需要对key进行存储
 * Author: Wolf
 * Created:Wolf-(2014-07-17 14:18)
 * Version: 1.0
 * Updated:2015/6/4 当正确接收到心跳后，想数据库中插入一条相关格式的信息，用于上位机判断
 *
 */
public class CommonHeartBeat extends BaseCommand {
    protected static Logger LOGGER = LoggerFactory.getLogger(CommonHeartBeat.class);
    public static final int COMMAND_ORDER = 0xAA;
    private static final ConcurrentLinkedQueue<Map<String, Integer>> keyStore = new ConcurrentLinkedQueue<Map<String, Integer>>();

    /**
     * 解析常规心跳
     *
     * @param buffer
     */
    @Override
    public void decodeMessage(ByteBuffer buffer) {

    }

    @Override
    public Boolean decodeMessageRet(ByteBuffer buffer, byte[] oth ,int length ,int deviceSerial) {
        //普通心跳
        LOGGER.info("接收到的常规心跳内容为：" + DlUtil.bytes2String(buffer.array()));

        Map keys = keyStore.peek();
        int key1 = buffer.get();
        int key2 = buffer.get();

        if (null == keys) {
            keys = new ConcurrentHashMap();
            keys.put("key1", key1);
            keys.put("key2", key2);
            keyStore.offer(keys);
            LOGGER.info("未能获取上次的key");
            return false;
        }

       /* int day = buffer.get();
        int hour = buffer.get();
        int min = buffer.get();
        int sec = buffer.get();*/

        byte[] times = new byte[4];
        buffer.get(times);
        int lastKey1 = (Integer) keys.get("key1");
        int lastKey2 = (Integer) keys.get("key2");

        keys.put("key1", key1);
        keys.put("key2", key2);

        boolean isEqual = this.compareTime(PwdUtils.encode(DlUtil.getTime(), lastKey1, lastKey2, 0), times, 1);
        if (!isEqual) {
            LOGGER.info("GPS 发送的时间和系统时间不匹配");
            return false;
        }
        try {
            byte[] serial = new byte[14];
            buffer.get(serial);
        } catch (BufferUnderflowException e) {
            //如果无法获取足够的序列号的话，发送 0x0E 指令
            LOGGER.error("未能获取到设备序列号");
            return false ;

        }

       /* byte[] serial = new byte[14];
        buffer.get(serial);*/
        HeartBeatToDb toDb = new HeartBeatToDb(new QueryRunner(Main.rDS));
        int insertId = toDb.insertToDb();

        if(insertId == -1){
            LOGGER.error("心跳插入失败！");
            return false;
        }
        return true;
    }

    /**
     * 比较时间是否和系统时间一致
     *
     * @param sys 系统时间
     * @param rec 接收到的时间
     * @param len 比较几个字节
     * @return
     */
    private boolean compareTime(byte[] sys, byte[] rec, int len) {
        boolean isEqual = true;
        for (int i = 0; i < len; i++) {
            if (sys[i] != rec[i]) {
                isEqual = false;
                break;
            }
        }
        return isEqual;
    }
}
