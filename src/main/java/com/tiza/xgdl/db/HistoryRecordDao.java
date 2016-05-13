package com.tiza.xgdl.db;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.bean.HistoryRecord;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:HistoryRecordDao
 * @description:查询历史记录的数据访问类
 * @date:2014/5/23 15:46
 */
public class HistoryRecordDao implements Runnable {
    private Logger logger = LoggerFactory.getLogger(HistoryRecordDao.class);
    private String key;

    public HistoryRecordDao() {
        key = Main.digest("HistoryRecordDao");
    }

    @Override
    public void run() {
        String val = Main.readFile(key);
        long mil = 0;
        if (null == val || val.trim().equals("")) {
            mil = System.currentTimeMillis() - 600000;
        } else {
            mil = Long.parseLong(val);
        }
        //mil = DlUtil.StringToDate("2013-06-04 15:18:06","yyyy-MM-dd HH:mm:ss").getTime();
        String timeStr = Main.long2String(mil);
        //TODO 需要删除 TOP 1
        String sql = "SELECT  [批次] as batch, [生产时间] as prodTime, [生产日期] as prodDate, [定单号] as orderNo, [骨1重量] as bone1Weight, [骨2重量] as bone2Weight, [骨3重量] as bone3Weight, [骨4重量] as bone4Weight, [骨5重量] as bone5Weight, [骨6重量] as bone6Weight, [粉1重量] as dust1Weight, [粉2重量] as dust2Weight, [沥青重量] as pitchWeight, [骨1误差] as bone1Error, [骨2误差] as bone2Error, [骨3误差] as bone3Error, [骨4误差] as bone4Error, [骨5误差] as bone5Error, [骨6误差] as bone6Error, [粉1误差] as dust1Error, [粉2误差] as dust2Error, [沥青误差] as pitchError, [单盘重量] as singleWeight, [配比名称] as matchName, [骨1目标] as bone1Goal, [骨2目标] as bone2Goal, [骨3目标] as bone3Goal, [骨4目标] as bone4Goal, [骨5目标] as bone5Goal, [骨6目标] as bone6Goal, [粉1目标] as dust1Goal, [粉2目标] as dust2Goal, [沥青目标] as pitchGoal, [成品料温] as finProTemp, [联网标志] as netFlag, [干预标志] as interFlag, [骨料1补料] as bone1Feed, [骨料2补料] as bone2Feed, [骨料3补料] as bone3Feed, [骨料4补料] as bone4Feed, [骨料5补料] as bone5Feed FROM [UserDatabase].[dbo].[历史记录] where [生产日期] > '" + timeStr + "'";
        logger.debug(sql);
        BeanListHandler<HistoryRecord> handler = new BeanListHandler<HistoryRecord>(HistoryRecord.class);
        QueryRunner runner = new QueryRunner(Main.uDS);
        try {
            List<HistoryRecord> historyRecords = runner.query(sql, handler);
            for (Iterator<HistoryRecord> iterator = historyRecords.iterator(); iterator.hasNext(); ) {
                HistoryRecord historyRecord = iterator.next();
                //获取历史数据中最大的时间将其存放
                mil = historyRecord.getProdTime().getTime() > mil ? historyRecord.getProdTime().getTime() : mil;
                try {
                    historyRecord.sendArray();
                } catch (Exception e) {
                    logger.error("send historyRecord error:" ,e);
                    //e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            logger.error("查询数据库失败", e);
        }
        Main.writeFile(key, String.valueOf(mil));
    }
}
