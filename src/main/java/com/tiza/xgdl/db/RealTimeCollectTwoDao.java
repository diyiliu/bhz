package com.tiza.xgdl.db;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.bean.CommonBean;
import com.tiza.xgdl.util.DlUtil;
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
 * Created:Wolf-(2014-10-08 16:01)
 * Version: 1.0
 * Updated:
 */
public class RealTimeCollectTwoDao implements Runnable {
    private Logger logger = LoggerFactory.getLogger(RealTimeCollectTwoDao.class);
    private String key;
    /**
     [生产时间] [datetime] NULL ,
     [骨料秤值] [float] NULL ,
     [粉料秤值] [float] NULL ,
     [沥青秤值] [float] NULL ,
     [配比名称] [nchar] (32) COLLATE Latin1_General_CI_AS NULL ,
     [生产批次] [float] NULL ,
     [批次小计] [float] NULL ,
     [单盘重量] [float] NULL ,
     [单盘实时值] [float] NULL ,
     [第一盘产量] [float] NULL ,
     [第二盘产量] [float] NULL ,
     [骨料1设定值] [float] NULL ,
     [骨料1稳定值] [float] NULL ,
     [骨料1第一盘值] [float] NULL ,
     [骨料1第二盘值] [float] NULL ,
     [骨料2设定值] [float] NULL ,
     [骨料2稳定值] [float] NULL ,
     [骨料2第一盘值] [float] NULL ,
     [骨料2第二盘值] [float] NULL ,
     [骨料3设定值] [float] NULL ,
     [骨料3稳定值] [float] NULL ,
     [骨料3第一盘值] [float] NULL ,
     [骨料3第二盘值] [float] NULL ,
     [骨料4设定值] [float] NULL ,
     [骨料4稳定值] [float] NULL ,
     [骨料4第一盘值] [float] NULL ,
     [骨料4第二盘值] [float] NULL ,
     [骨料5设定值] [float] NULL ,
     [骨料5稳定值] [float] NULL ,
     [骨料5第一盘值] [float] NULL ,
     [骨料5第二盘值] [float] NULL ,
     [骨料6设定值] [float] NULL ,
     [骨料6稳定值] [float] NULL ,
     [骨料6第一盘值] [float] NULL ,
     [骨料6第二盘值] [float] NULL ,
     [粉料1设定值] [float] NULL ,
     [粉料1稳定值] [float] NULL ,
     [粉料1第一盘值] [float] NULL ,
     [粉料1第二盘值] [float] NULL ,
     [粉料2设定值] [float] NULL ,
     [粉料2稳定值] [float] NULL ,
     [粉料2第一盘值] [float] NULL ,
     [粉料2第二盘值] [float] NULL ,
     [沥青设定值] [float] NULL ,
     [沥青稳定值] [float] NULL ,
     [沥青第一盘值] [float] NULL ,
     [沥青第二盘值] [float] NULL ,
     [搅拌倒记时] [float] NULL **/
    public RealTimeCollectTwoDao() {
        key = Main.digest("RealTimeCollectTwoDao");
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
        String sql = "SELECT  [生产时间] as compareField ,'realTimeCollectTwo,' + CONVERT(varchar(100), [生产时间], 121)+','+cast([骨料秤值] as nvarchar)+','+cast([粉料秤值] as nvarchar)+','+cast([沥青秤值] as nvarchar)+','+cast([配比名称] as nvarchar)+','+cast([生产批次] as nvarchar)+','+cast([批次小计] as nvarchar)+','+cast([单盘重量] as nvarchar)+','+cast([单盘实时值] as nvarchar)+','+cast([第一盘产量] as nvarchar)+','+cast([第二盘产量] as nvarchar)+','+cast([骨料1设定值] as nvarchar)+','+cast([骨料1稳定值] as nvarchar)+','+cast([骨料1第一盘值] as nvarchar)+','+cast([骨料1第二盘值] as nvarchar)+','+cast([骨料2设定值] as nvarchar)+','+cast([骨料2稳定值] as nvarchar)+','+cast([骨料2第一盘值] as nvarchar)+','+cast([骨料2第二盘值] as nvarchar)+','+cast([骨料3设定值] as nvarchar)+','+cast([骨料3稳定值] as nvarchar)+','+cast([骨料3第一盘值] as nvarchar)+','+cast([骨料3第二盘值] as nvarchar)+','+cast([骨料4设定值] as nvarchar)+','+cast([骨料4稳定值] as nvarchar)+','+cast([骨料4第一盘值] as nvarchar)+','+cast([骨料4第二盘值] as nvarchar)+','+cast([骨料5设定值] as nvarchar)+','+cast([骨料5稳定值] as nvarchar)+','+cast([骨料5第一盘值] as nvarchar)+','+cast([骨料5第二盘值] as nvarchar)+','+cast([骨料6设定值] as nvarchar)+','+cast([骨料6稳定值] as nvarchar)+','+cast([骨料6第一盘值] as nvarchar)+','+cast([骨料6第二盘值] as nvarchar)+','+cast([粉料1设定值] as nvarchar)+','+cast([粉料1稳定值] as nvarchar)+','+cast([粉料1第一盘值] as nvarchar)+','+cast([粉料1第二盘值] as nvarchar)+','+cast([粉料2设定值] as nvarchar)+','+cast([粉料2稳定值] as nvarchar)+','+cast([粉料2第一盘值] as nvarchar)+','+cast([粉料2第二盘值] as nvarchar)+','+cast([沥青设定值] as nvarchar)+','+cast([沥青稳定值] as nvarchar)+','+cast([沥青第一盘值] as nvarchar)+','+cast([沥青第二盘值] as nvarchar)+','+cast([搅拌倒记时] as nvarchar) as result FROM [UserDatabase].[dbo].[实时采集2] where [生产时间] > '" + timeStr + "'";
        BeanListHandler<CommonBean> handler = new BeanListHandler<CommonBean>(CommonBean.class);
        QueryRunner runner = new QueryRunner(Main.uDS);
        try {
            List<CommonBean> records = runner.query(sql, handler);
            for (Iterator<CommonBean> iterator = records.iterator(); iterator.hasNext(); ) {
                CommonBean record = iterator.next();
                //获取历史数据中最大的时间将其存放
                long tempDate = DlUtil.StringToDate(record.getCompareField(), "yyyy-MM-dd HH:mm:ss").getTime();
                mil = tempDate > mil ? tempDate : mil;
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
        Main.writeFile(key, String.valueOf(mil));
    }
}
