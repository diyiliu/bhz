package com.tiza.xgdl.db;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.bean.CommonBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-10-08 16:03)
 * Version: 1.0
 * Updated:
 */
public class AlarmInfoDao implements Runnable {
    private Logger logger = LoggerFactory.getLogger(AlarmInfoDao.class);
    private String key;

    public AlarmInfoDao() {
        key = Main.digest("AlarmInfoDao");
    }

    @Override
    public void run() {
        String val = Main.readFile(key);
        int compareId = 1;
        if(null == val || val.trim().equals("")){
            compareId = 1;
        }else{
            compareId = Integer.parseInt(val);
        }
        String sql = "SELECT [id] as compareField , 'alarmInfo,' + CAST([id] AS nvarchar)+','+[AlmPosition]+','+ [AlmPoint]+','+CONVERT(varchar(100), [AlmTime], 121)+','+[AlmInfo]+','+CAST([AlmStatus] AS nvarchar)+','+CAST([AlmClass] AS nvarchar)+','+ CAST([AlmValue] as nvarchar)+','+CAST([AlmNo] AS nvarchar)+','+[AlmMono]+','+CAST([sendFlag] as nvarchar) as result FROM [ReportData].[dbo].[AlarmInfo] where [id]> '" + compareId + "'";
        logger.debug(sql);
        BeanListHandler<CommonBean> handler = new BeanListHandler<CommonBean>(CommonBean.class);
        QueryRunner runner = new QueryRunner(Main.rDS);
        try {
            List<CommonBean> records = runner.query(sql, handler);
            for (Iterator<CommonBean> iterator = records.iterator(); iterator.hasNext(); ) {
                CommonBean record = iterator.next();
                //获取历史数据中最大的时间将其存放
                int tempId = Integer.parseInt(record.getCompareField());
                compareId = tempId > compareId ? tempId : compareId;
                try {
                    record.sendNomArray(record.getResult());
                } catch (Exception e) {
                    logger.error("send historyRecord error:" ,e);
                    //e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            logger.error("查询数据库失败", e);
        }
        Main.writeFile(key, String.valueOf(compareId));
    }
}
/**
 * [id] [bigint] IDENTITY (1, 1) NOT NULL ,
 [AlmPosition] [nvarchar] (50) COLLATE Chinese_PRC_CI_AS NULL ,
 [AlmPoint] [nvarchar] (50) COLLATE Chinese_PRC_CI_AS NULL ,
 [AlmTime] [datetime] NULL ,
 [AlmInfo] [nvarchar] (50) COLLATE Chinese_PRC_CI_AS NULL ,
 [AlmStatus] [tinyint] NULL ,
 [AlmClass] [tinyint] NULL ,
 [AlmValue] [real] NULL ,
 [AlmNo] [smallint] NULL ,
 [AlmMono] [nvarchar] (50) COLLATE Chinese_PRC_CI_AS NULL ,
 [sendFlag] [tinyint] NULL CONSTRAINT [DF_AlarmInfo_sendFlag] DEFAULT (0),
 CONSTRAINT [PK_AlarmInfo] PRIMARY KEY  NONCLUSTERED
 (
 */
