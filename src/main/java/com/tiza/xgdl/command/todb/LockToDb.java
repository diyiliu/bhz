package com.tiza.xgdl.command.todb;

import com.tiza.xgdl.util.DlUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Description: 锁机指令 到 数据库
 * Author: Wolf
 * Created:Wolf-(2014-11-04 14:51)
 * Version: 1.0
 * Updated:
 */
public class LockToDb extends BaseToDbBiz implements Runnable {

    private Logger logger = LoggerFactory.getLogger(LockToDb.class);

    private boolean stop = false;

    private int errorInsertId = -1;

    private int insertId;

    private long sleepTime = 5000;

    private static final int cmdId = 0x01;

    private static final int respCmdId = 0x11;

    private String sql;

    private Object[] params;

    public LockToDb(QueryRunner runner) {
        super(runner);
        this.stop = false;
    }

    @Override
    public String getSql() {
        return this.sql;
    }

    @Override
    public Object[] getParams() {
        return this.params;
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

        this.insertId = super.insertToDb();
        //插入之后，开始线程
        if (this.insertId != this.errorInsertId) {
            new Thread(this, "LockToDb--" + this.insertId).start();
        }
        return this.insertId;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        while (!this.stop) {
            //查询数据库，是否有锁机相应
            int responseStatus = this.getResp(this.insertId, this.respCmdId);
            if (responseStatus == 2 || responseStatus == 1) {
                this.stop = true;
                //正确响应，向终端 发送响应
                if(responseStatus == 2){
                    ByteBuffer buffer = ByteBuffer.allocate(2);
                    buffer.put((byte) 0x0B);
                    buffer.put((byte) 0x00);
                    buffer.flip();
                    DlUtil.pack(buffer.array());
                }

            }
            if (responseStatus == 0) {
                try {
                    Thread.sleep(this.sleepTime);
                } catch (InterruptedException e) {
                    logger.error("sleep error :", e);
                }
            }

        }
    }
}
