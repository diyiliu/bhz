package com.tiza.xgdl.command;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.command.todb.BaseToDbBiz;
import com.tiza.xgdl.command.todb.LockToDb;
import com.tiza.xgdl.command.todb.UnLockToDb;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Description: 收到锁机 指令 下发 到数据库
 * Author: Wolf
 * Created:Wolf-(2014-07-31 15:07)
 * Version: 1.0
 * Updated:
 */
public class LockCommand extends BaseCommand {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void decodeMessage(ByteBuffer buffer) {

    }

    @Override
    public Object decodeMessageRet(ByteBuffer buffer, byte[] oth ,int length ,int serial) {
        buffer.clear();
        //用来模拟测试
        int isLock = buffer.get();

        BaseToDbBiz baseToDbBiz = null;
        if(isLock == 0xFF){
            baseToDbBiz = new LockToDb(new QueryRunner(Main.rDS));
        }else if(isLock == 0x00){
            baseToDbBiz = new UnLockToDb(new QueryRunner(Main.rDS));
        }

        int insertId = baseToDbBiz.insertToDb();

        if(insertId == -1){
            logger.error("插入失败 , 未能成功插入");
            return "";  //TODO
        }
        return true;
    }
}
