package com.tiza.xgdl.command.todb;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 模拟心跳 到 数据库
 * Author: Wolf
 * Created:Wolf-(2014-11-04 14:46)
 * Version: 1.0
 * Updated:*/
public class HeartBeatToDb extends BaseToDbBiz implements Runnable  {
    private Logger LOGGER = LoggerFactory.getLogger(HeartBeatToDb.class);
    private String sql ;
    private Object[] params;
    private static final int cmdId = 0x00;
    public HeartBeatToDb(QueryRunner runner) {
        super(runner);
    }

    @Override
    public int insertToDb() {
        this.sql = "insert into [UserDatabase].[dbo].[exchangeinfo] (direction,command,content,flag) values (?,?,?,?)";
        Object[] params = new Object[4];
        params[0] = 0;
        params[1] = this.cmdId;
        params[2] = this.getContent();
        params[3] = 1;
        this.params = params;
        return super.insertToDb();
    }

    @Override
    public void run() {
        //do nothing
       /* Long dev = Long.parseLong(Main.readFile("deviceId"));

        ByteBuffer buf = ByteBuffer.allocate(19);
        buf.putLong(dev);
        buf.put(DlUtil.getCurrentTime());
        buf.putInt(0);
        buf.put((byte) 0);
        buf.clear();
        byte[] array = buf.array();

        byte[] dest = DlUtil.e2s(array);
        String content = DlUtil.bytes2String(dest);

        //执行插入操作
        QueryRunner runner = new QueryRunner(Main.rDS);
        String sql = "insert into [exchangeinfo] (direction,command,content) values (?,?,?)";
        try {
            runner.update(sql, 0, 0, content);
        } catch (SQLException e) {
            logger.error("插入心跳数据失败," + sql, e.getMessage());
        }*/
    }

    @Override
    public String getSql() {
        return this.sql;
    }

    @Override
    public Object[] getParams() {
        return this.params;
    }
}
