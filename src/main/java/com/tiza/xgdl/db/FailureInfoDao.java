package com.tiza.xgdl.db;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.bean.FailureInfo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:AlarmInfoDao
 * @description:查询报警信息的数据访问类
 * @date:2014/5/19 14:00
 */
public class FailureInfoDao implements Runnable {
    private Logger logger = LoggerFactory.getLogger(FailureInfoDao.class);
    private String key;

    public FailureInfoDao() {
        key = Main.digest("FailureInfoDao");
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
        String sql = "SELECT id, AlmPosition, AlmTime, AlmInfo, AlmStatus, AlmClass, AlmValue, AlmNo, AlmMono FROM AlarmInfo where sendFlag = 1 and AlmTime > '" + timeStr + "'";
        BeanListHandler<FailureInfo> handler = new BeanListHandler<FailureInfo>(FailureInfo.class);
        QueryRunner runner = new QueryRunner(Main.rDS);
        try {
            List<FailureInfo> alarmInfoList = runner.query(sql, handler);
            for (Iterator<FailureInfo> iterator = alarmInfoList.iterator(); iterator.hasNext(); ) {
                FailureInfo alarmInfo = iterator.next();

                long almTime = alarmInfo.getAlmTime().getTime();
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
