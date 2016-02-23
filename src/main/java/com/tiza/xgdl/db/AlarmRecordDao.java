package com.tiza.xgdl.db;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.bean.AlarmRecord;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:AlarmRecordDao
 * @description:类的描述
 * @date:2014/7/2 14:55
 */
public class AlarmRecordDao implements Runnable {
    private Logger logger = LoggerFactory.getLogger(FailureInfoDao.class);
    private String key;

    public AlarmRecordDao() {
        key = Main.digest("AlarmRecordDao");
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
        String sql = "SELECT [报警时间] as alarmTime, [报警号] as alarmNo FROM [UserDatabase].[dbo].[报警记录] and [报警时间] > '" + timeStr + "'";
        BeanListHandler<AlarmRecord> handler = new BeanListHandler<AlarmRecord>(AlarmRecord.class);
        QueryRunner runner = new QueryRunner(Main.rDS);
        try {
            List<AlarmRecord> alarmInfoList = runner.query(sql, handler);
            for (Iterator<AlarmRecord> iterator = alarmInfoList.iterator(); iterator.hasNext(); ) {
                AlarmRecord alarmRecord = iterator.next();
                long almTime = alarmRecord.getAlarmTime().getTime();
                if (almTime > mil) {
                    mil = almTime;
                }
            }
        } catch (SQLException e) {
            logger.error("查询数据库失败", e.getMessage());
        }
        Main.writeFile(key, String.valueOf(mil));
    }
}
