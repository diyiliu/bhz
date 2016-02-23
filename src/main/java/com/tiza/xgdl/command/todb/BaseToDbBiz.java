package com.tiza.xgdl.command.todb;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.bean.ExchangeInfoBean;
import com.tiza.xgdl.util.DlUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-11-04 17:11)
 * Version: 1.0
 * Updated:
 */
public abstract class BaseToDbBiz{
    private Logger logger = LoggerFactory.getLogger(BaseToDbBiz.class);
    private QueryRunner runner ;
    protected BaseToDbBiz(QueryRunner runner) {
        this.runner = runner;
    }

    public  int insertToDb(){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null ;
        try {
            connection = runner.getDataSource().getConnection();
            ps = connection.prepareStatement(this.getSql(), PreparedStatement.RETURN_GENERATED_KEYS);
            runner.fillStatement(ps, this.getParams());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        } catch (SQLException e) {
            logger.error("query error " ,e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(connection);
        }
        return -1 ;
    }

    /**
     *
     * @param id 上次插入的ID ，命令类型
     * @param cmdId
     * @return 0 继续，1 退出  2 完成
     */
    public int getResp(int id , int cmdId) {
        String sql = "SELECT top 1 id, direction, Command, Content FROM [UserDatabase].[dbo].[exchangeinfo] where command=? order by id desc";
        ExchangeInfoBean bean = null;
        try {
            bean = this.runner.query(sql ,new BeanHandler<ExchangeInfoBean>(ExchangeInfoBean.class),cmdId);
        } catch (SQLException e) {
            logger.error("query error :" ,e);
            return 1;
        }
        if(null == bean){
            return 0;
        }
        String content = bean.getContent();
        byte[] bytes = DlUtil.s2e(content);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.getLong();
        byte[] times = new byte[6];
        buffer.get(times);
        long responseTime = DlUtil.getTime(times);
        int responseInsertId = buffer.getInt();
        //应答结果
        byte isOk = buffer.get();

        if((System.currentTimeMillis() - responseTime) > 10 * 60 * 1000){
            return 1 ;
        }
        if(responseInsertId != id){
            return 0;
        }
        if(1 == isOk){
            return 2 ;
        }
        return 1;
    }


    public String getContent(){
        String devId= Main.readFile("deviceId");
        devId = devId == null ? "0" : devId;
        Long dev = Long.parseLong(devId);
        ByteBuffer buf = ByteBuffer.allocate(19);
        buf.putLong(dev);
        buf.put(DlUtil.getCurrentTime());
        buf.putInt(0);
        buf.put((byte) 0);
        buf.clear();
        byte[] array = buf.array();

        byte[] dest = DlUtil.e2s(array);
        String content = DlUtil.bytes2String(dest);
        return content ;
    }

    public abstract String getSql();
    public abstract Object[] getParams();

    /**
     * 响应终端
     */
    public void sendToTerminal(byte[] array){
        DlUtil.pack(array);
    }

    //public abstract void setStop();

}
