package com.tiza.xgdl.db;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.bean.MatchBill;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:MatchBillDao
 * @description:配比单的数据库访问类
 * @date:2014/6/13 15:49
 */
public class MatchBillDao implements Runnable {
    private Logger logger = LoggerFactory.getLogger(HistoryRecordDao.class);
    private String key;

    public MatchBillDao() {
        key = Main.digest("MatchBillDao");
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
        String sql = "SELECT [配比名称] as name, [油石比] as oilSto, [骨料1] as bone1, [骨料2] as bone2, [骨料3] as bone3, [骨料4] as bone4, [骨料5] as bone5, [骨料6] as bone6, [粉料1] as dust1, [粉料2] as dust2, [沥青] as pitch, [单盘重量] as singleWeight, [手动设空沥青] as setNonPitch, [比例类型] as scaleType FROM [UserDatabase].[dbo].[配比单]";
        BeanListHandler<MatchBill> handler = new BeanListHandler<MatchBill>(MatchBill.class);
        QueryRunner runner = new QueryRunner(Main.uDS);
        try {
            List<MatchBill> matchBills = runner.query(sql, handler);
            for (Iterator<MatchBill> iterator = matchBills.iterator(); iterator.hasNext(); ) {
                MatchBill matchBill = iterator.next();
                System.out.println(matchBill.toString());
            }
        } catch (SQLException e) {
            logger.error("查询数据库失败", e.getMessage());
        }
        Main.writeFile(key, String.valueOf(mil));
    }
}