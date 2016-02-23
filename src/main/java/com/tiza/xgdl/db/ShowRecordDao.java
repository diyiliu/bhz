package com.tiza.xgdl.db;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.bean.ShowRecord;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:ShowRecordDao
 * @description:查询显示记录的数据访问类
 * @date:2014/5/23 15:45
 */
public class ShowRecordDao implements Runnable {
    private Logger logger = LoggerFactory.getLogger(ShowRecordDao.class);
    private String key;

    public ShowRecordDao() {
        key = Main.digest("ShowRecordDao");
    }

    @Override
    public void run() {
        String val = Main.readFile(key);
        long mil = 0;
        if (null == val || val.trim().equals("")) {
            mil = System.currentTimeMillis() - 300000;
        } else {
            mil = Long.parseLong(val);
        }
        String timeStr = Main.long2String(mil);
        String sql = "SELECT [记录时间] as recordTimeas , [引风机电流] as idfElect, [搅拌器1电流] as stirrer1Elect, [搅拌器2电流] as stirrer2Elect, [振动筛1电流] as riddler1Elect, [振动筛2电流] as riddler2Elect, [热提电流] as hotRiseElect, [烘干筒1电流] as dryDrum1Elect, [烘干筒2电流] as dryDrum2Elect, [烘干筒3电流] as dryDrum3Elect, [烘干筒4电流] as dryDrum4Elect, [风门开度] as airDoorOpen, [除尘压差1] as dedustPress1, [除尘压差2] as dedustPress2, [烟道温度] as flueTemp, [冷料变频器1] as coldMeFc1, [冷料变频器2] as coldMeFc2, [冷料变频器3] as coldMeFc3, [冷料变频器4] as coldMeFc4, [冷料变频器5] as coldMeFc5, [冷料变频器6] as coldMeFc6, [冷料变频总调] as coldMeFcTotal, [冷料1设定] as coldMe1Set, [冷料2设定] as coldMe2Set, [冷料3设定] as coldMe3Set, [冷料4设定] as coldMe4Set, [冷料5设定] as coldMe5Set, [冷料6设定] as coldMe6Set, [粉料秤] as powderScales, [骨料秤] as aggregateScales, [沥青秤] as pitchScales, [烘干筒负压] as dryDrumPress, [沥青温度] as pitchTemp, [沙仓温度] as sandTemp, [燃油门] as fuelDoor, [燃风门] as airDoor, [下料口温度] as outletTemp, [倒计时] as countDown, [成品料温] as finiProdTemp, [车位1变速] as port1ChanSpeed, [车位1去停] as port1GoStop, [车位1回停] as port1BackStop, [车位2变速] as port2ChanSpeed, [车位2去停] as port2GoStop, [车位2回停] as port2BackStop, [重油温度] as heavyOilTemp, [尾气温度2] as tailGasTemp2, [燃油压力] as oilPress, [料温设定] as materialTempSet, [待机时间] as standbyTime, [配比名称] as matchName, [批次] as batch, [单盘重量] as singleDiskWeigth, [生产时间] as produceTime, [当前时间] as currentTime FROM [UserDatabase].[dbo].[显示记录] where [当前时间] > '" + timeStr + "'";
        BeanListHandler<ShowRecord> handler = new BeanListHandler<ShowRecord>(ShowRecord.class);
        QueryRunner runner = new QueryRunner(Main.uDS);
        try {
            List<ShowRecord> showRecords = runner.query(sql, handler);
            for (Iterator<ShowRecord> iterator = showRecords.iterator(); iterator.hasNext(); ) {
                ShowRecord showRecord = iterator.next();
                System.out.println(showRecord.toString());
            }
        } catch (SQLException e) {
            logger.error("查询数据库失败", e.getMessage());
        }
        Main.writeFile(key, String.valueOf(mil));
    }
}