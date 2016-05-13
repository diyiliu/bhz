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
 * Created:Wolf-(2014-10-08 16:00)
 * Version: 1.0
 * Updated:
 */
public class RealTimeCollectOneDao implements Runnable {
    private Logger logger = LoggerFactory.getLogger(RealTimeCollectOneDao.class);
    private String key;

    /**
     * [生产时间] [datetime] NULL ,
     * [引风机电流] [float] NULL ,
     * [搅拌器1电流] [float] NULL ,
     * [搅拌器2电流] [float] NULL ,
     * [振动筛1电流] [float] NULL ,
     * [振动筛2电流] [float] NULL ,
     * [热提电流] [float] NULL ,
     * [烘干筒1电流] [float] NULL ,
     * [烘干筒2电流] [float] NULL ,
     * [烘干筒3电流] [float] NULL ,
     * [烘干筒4电流] [float] NULL ,
     * [称量模式] [float] NULL ,
     * [称量开始] [float] NULL ,
     * [称量锁定] [float] NULL ,
     * [秤门锁定] [float] NULL ,
     * [搅拌器门锁定] [float] NULL ,
     * [风门开度] [float] NULL ,
     * [除尘差压] [float] NULL ,
     * [烘干筒负压] [float] NULL ,
     * [烟道温度] [float] NULL ,
     * [沥青温度] [float] NULL ,
     * [骨料温度] [float] NULL ,
     * [成品料温度] [float] NULL ,
     * [冷料总调] [float] NULL ,
     * [冷料1频率] [float] NULL ,
     * [冷料2频率] [float] NULL ,
     * [冷料3频率] [float] NULL ,
     * [冷料4频率] [float] NULL ,
     * [冷料5频率] [float] NULL ,
     * [冷料6频率] [float] NULL ,
     * [燃风门设定] [float] NULL ,
     * [燃风门调节] [float] NULL ,
     * [烘干筒出料温度] [float] NULL ,
     * [编码器实时值] [float] NULL
     **/
    public RealTimeCollectOneDao() {
        key = Main.digest("RealTimeCollectOneDao");
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
        // mil = DlUtil.StringToDate("2013-06-04 15:18:06", "yyyy-MM-dd HH:mm:ss").getTime();
        String timeStr = Main.long2String(mil);
        String sql = "SELECT [生产时间] as compareField, 'realTimeCollectOne,' + CONVERT(varchar(100), [生产时间], 121)+','+ cast([引风机电流]as nvarchar) +','+cast([搅拌器1电流]as nvarchar) +','+cast([搅拌器2电流]as nvarchar) +','+cast([振动筛1电流]as nvarchar) +','+cast([振动筛2电流]as nvarchar) +','+cast([热提电流]as nvarchar) +','+cast([烘干筒1电流]as nvarchar) +','+cast([烘干筒2电流]as nvarchar) +','+cast([烘干筒3电流]as nvarchar) +','+cast([烘干筒4电流]as nvarchar) +','+cast([称量模式]as nvarchar) +','+cast([称量开始]as nvarchar) +','+cast([称量锁定]as nvarchar) +','+cast([秤门锁定]as nvarchar) +','+cast([搅拌器门锁定]as nvarchar) +','+cast([风门开度]as nvarchar) +','+cast([除尘差压]as nvarchar) +','+cast([烘干筒负压]as nvarchar) +','+cast([烟道温度]as nvarchar) +','+cast([沥青温度]as nvarchar) +','+cast([骨料温度]as nvarchar) +','+cast([成品料温度]as nvarchar) +','+cast([冷料总调]as nvarchar) +','+cast([冷料1频率]as nvarchar) +','+cast([冷料2频率]as nvarchar) +','+cast([冷料3频率]as nvarchar) +','+cast([冷料4频率]as nvarchar) +','+cast([冷料5频率]as nvarchar) +','+cast([冷料6频率]as nvarchar) +','+cast([燃风门设定]as nvarchar) +','+cast([燃风门调节]as nvarchar) +','+cast([烘干筒出料温度]as nvarchar) +','+cast([编码器实时值] as nvarchar) as result FROM [UserDatabase].[dbo].[实时采集1] where [生产时间] > '" + timeStr + "'";
        logger.debug(sql);
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
                    if (record.getResult() != null) {
                        record.sendNomArray(record.getResult());
                    }else {
                        logger.warn("[实时采集1]数据异常！[生产时间：{}, 编码器实时值：{}]", record.getCompareField(), record.getResult());
                    }
                    //Thread.sleep(2000);
                } catch (Exception e) {
                    logger.error("send historyRecord error:", e);
                    //e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            logger.error("查询数据库失败", e);
        }
        Main.writeFile(key, String.valueOf(mil));
    }
}
