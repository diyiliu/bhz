package com.tiza.xgdl;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.tiza.xgdl.comm.ClientConnector;
import com.tiza.xgdl.command.CommandFactory;
import com.tiza.xgdl.gui.MainView;
import com.tiza.xgdl.util.Common;
import gnu.io.CommPortIdentifier;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Description: Main
 * Author: DIYILIU
 * Update: 2016-01-13 16:48
 */
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static DataSource rDS;//reportData
    public static DataSource uDS;//userDatabase
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(7);
    public static ChannelHandlerContext comCTX;
    public static String COM_NAME = "COM2";
    public static final ConcurrentLinkedQueue<byte[]> workQueue = new ConcurrentLinkedQueue<byte[]>();

    public static final ConcurrentHashMap<Integer, ConcurrentHashMap> resentMap = new ConcurrentHashMap<Integer, ConcurrentHashMap>();

    static ReadWriteLock lock = new ReentrantReadWriteLock();
    final static Lock readLock = lock.readLock();
    final static Lock writeLock = lock.writeLock();


    public static void main(String[] args) {
        // 监听串口
        listPort();
        // 显示配置面板
        MainView.show();
    }


    public static void run(){

        startLog();
        COM_NAME = getConfig("com");//读取串口的配置

        if (null == COM_NAME || COM_NAME.equals("")) {
            logger.error("[串口错误]--没有获取到程序监听的串口名称,请在vehicle.propertis中设置\"com=COM2\"类似的配置");
        } else {
            logger.info("[配置信息]--程序将监听串口" + COM_NAME + "... ...");
        }
        initDataSource();
        //加载监听
        //ListenerStart.start();

        //加载指令
        CommandFactory.init();

        /*
        executor.scheduleAtFixedRate(new SoftHeartJob(), 0, 30, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new FailureInfoDao(), 0, 30, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new HistoryRecordDao(), 0, 30, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new MatchBillDao(), 0, 30, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new ShowRecordDao(), 0, 30, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new AlarmRecordDao(), 0, 30, TimeUnit.MINUTES);
        */

        startRxtx();
    }

    public static void startRxtx() {
        ClientConnector clientConnector = new ClientConnector(new RxtxDeviceAddress(COM_NAME));
        clientConnector.run();
    }

    public static void listPort() {
        CommPortIdentifier cpid;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();

        logger.info("list all Port of this PC：");
        while (en.hasMoreElements()) {
            cpid = (CommPortIdentifier) en.nextElement();
            if (cpid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                logger.info("Port :{}, Being used: {}", cpid.getName(), cpid.isCurrentlyOwned());
            }
        }
    }

    public static void startLog() {
        try {
            ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
            LoggerContext loggerContext = (LoggerContext) loggerFactory;
            loggerContext.reset();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            configurator.doConfigure(Common.getFilePath("logback.xml"));
            logger.info("启动日志记录成功");
            logger.info("[日志信息]--程序记录日志模块启动成功... ...");
        } catch (Exception e) {
            System.out.println("启动日志记录器时发生错误:" + e.getMessage());
            logger.error("[日志错误]--程序记录日志模块启动失败,请检查原因");
            System.exit(0);
        }
    }

    public static void initDataSource() {
        Properties rProperties = new Properties();

        String path = Common.getFilePath("report.properties");
        try {

            FileInputStream fis = new FileInputStream(path);
            rProperties.load(fis);
            fis.close();
            logger.info("[配置信息]--读取数据库ReportData配置成功... ...");
        } catch (FileNotFoundException e) {
            logger.error("数据库配置文件[" + path + "report.properties]不存在", e);
            logger.error("[文件错误]--数据库ReportData配置文件读取错误,请检查report.properties");
            System.exit(0);
        } catch (IOException e) {
            logger.error("读取数据库配置文件失败", e);
            logger.error("[解析错误]--数据库ReportData配置文件解析错误,请检查report.properties");
            System.exit(0);
        }

        Properties uProperties = new Properties();

        path = Common.getFilePath("user.properties");
        try {
            FileInputStream fis = new FileInputStream(path);
            uProperties.load(fis);
            fis.close();
            logger.info("[配置信息]--读取数据库UserDatabase配置成功... ...");
        } catch (FileNotFoundException e) {
            logger.error("数据库配置文件[" + path + "user.properties]不存在", e);
            logger.error("[文件错误]--数据库UserDatabase配置文件读取错误,请检查user.properties");
            System.exit(0);
        } catch (IOException e) {
            logger.error("读取数据库配置文件失败", e);
            logger.error("[解析错误]--数据库UserDatabase配置文件解析错误,请检查user.properties");
            System.exit(0);
        }

        try {
            rDS = DruidDataSourceFactory.createDataSource(rProperties);
            rDS.getConnection().close();
            logger.info("[连接信息]--数据库ReportData连接测试成功...");
        } catch (Exception e) {
            logger.error("创建数据库ReportData连接池失败", e.getMessage());
            //showView.appendContent("[连接错误]--连接数据库ReportData错误,请检查数据库配置文件report.properties");
        }
        try {
            uDS = DruidDataSourceFactory.createDataSource(uProperties);
            uDS.getConnection().close();
            logger.info("[连接信息]--数据库UserDatabase连接测试成功...");
        } catch (Exception e) {
            logger.error("创建数据库UserDatabase连接池失败", e.getMessage());
            //showView.appendContent("[连接错误]--连接数据库UserDatabase错误,请检查数据库配置文件user.properties");
        }
    }

    public static String getConfig(String key) {
        String name = Common.getFilePath("config.properties");

        File file = new File(name);
        if (!file.exists()) {
            logger.error("config.properties can not find" + name);
            System.exit(1);
        }
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(file);
            properties.load(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            logger.error("config.properties can not find :" + name);
            System.exit(1);
        } catch (IOException e) {
            logger.error("config.properties read exception:" + name);
            System.exit(1);
        }
        return properties.getProperty(key);
    }


    public static String readFile(String key) {
        writeLock.lock();
        String name = Common.getFilePath("vehicle.properties");
        File file = new File(name);
        if (!file.exists()) {
            return "";
        }
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(file);
            properties.load(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            logger.error("无法找到文件" + name);
        } catch (IOException e) {
            logger.error("读取文件失败" + name);
        } finally {
            writeLock.unlock();
        }
        return properties.getProperty(key);
    }

    public static void writeFile(String key, String value) {
        readLock.lock();
        String name = Common.getFilePath("vehicle.properties");
        File file = new File(name);
        if (!file.exists()) {
            file.mkdir();
        }
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(file);
            properties.load(fis);
            fis.close();

            //file.delete();

            if (properties.containsKey(key)) {
                properties.remove(key);
            }
            properties.setProperty(key, value);
            FileOutputStream fos = new FileOutputStream(name);
            properties.store(fos, null);

        } catch (FileNotFoundException e) {
            logger.error("无法找到文件" + name);
        } catch (IOException e) {
            logger.error("读取文件失败" + name);
        } finally {
            readLock.unlock();
        }
    }

    public static String digest(String src) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return src;
        }
        byte[] array = digest.digest(src.getBytes());
        StringBuffer buf = new StringBuffer();
        for (byte b : array) {
            buf.append(String.format("%20X", b & 0xFF).trim());
        }
        return buf.toString();
    }

    public static String long2String(long mills) {
        Date date = new Date(mills);
        return String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", date);
    }
}
