package com.tiza.xgdl.command.todb;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.util.DlUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.SQLException;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:DbHeartJob
 * @description:与监控程序保持心跳(通过数据库)
 * @date:2014/6/16 11:07
 */
public class SoftHeartJob implements Runnable {
    private Logger logger = LoggerFactory.getLogger(SoftHeartJob.class);

    @Override
    public void run() {
        Long dev = Long.parseLong(Main.readFile("deviceId"));

        ByteBuffer buf = ByteBuffer.allocate(16);
        buf.putLong(dev);
        buf.put(DlUtil.getCurrentTime());
        buf.putShort((short) 0);
        buf.clear();
        byte[] array = buf.array();
        logger.info("");
        byte[] dest = DlUtil.e2s(array);
        String content = DlUtil.bytes2String(dest);

        //执行插入操作
        QueryRunner runner = new QueryRunner(Main.rDS);
        String sql = "insert into exchangeinfo (direction,command,content) values (?,?,?)";
        try {
            runner.update(sql, 0, 0, content);
        } catch (SQLException e) {
            logger.error("插入心跳数据失败," + sql, e.getMessage());
        }
    }
}