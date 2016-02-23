package com.tiza.xgdl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-09-02 11:25)
 * Version: 1.0
 * Updated:
 */
public class PropertiesUtils {
    private static Map<String, Properties> PROPERTIES = new ConcurrentHashMap<String, Properties>();
    private static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);
    private static final String PATH = PropertiesUtils.class.getResource("/").getPath();
    static ReadWriteLock lock = new ReentrantReadWriteLock();
    final static Lock readLock = lock.readLock();
    final static Lock writeLock = lock.writeLock();

    /**
     * 设置属性到文本缓存
     *
     * @param key
     * @param value
     * @param filePath
     * @return
     */
    public static void setProperty(String key, String key1, String value, String filePath) {
        filePath = PATH + filePath;
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.error("properties can not find:" + filePath);
            System.exit(1);
        }
        Properties properties = new Properties();
        FileInputStream fis = null;
        FileOutputStream out = null;
        try {
            fis = new FileInputStream(file);
            out = new FileOutputStream(file);
            properties.load(fis);
            properties.setProperty(key1, value);
            PROPERTIES.put(key, properties);
            properties.store(out, "");
        } catch (FileNotFoundException e) {
            LOGGER.error("properties can not find :" + filePath);
            System.exit(1);
        } catch (IOException e) {
            LOGGER.error("properties read exception:" + filePath);
            System.exit(1);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error("FileInputStream fis close error:", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("FileOutputStream out close error:", e);
                }
            }
        }
    }

    public static void setProperty(String key, String value, String filePath) {
        readLock.lock();
        filePath = PATH + filePath;
        File file = new File(filePath);
        if (!file.exists()) {
            LOGGER.error("properties can not find:" + filePath);
            System.exit(1);
        }
        Properties properties = new Properties();
        FileInputStream fis = null;
        FileOutputStream out = null;
        try {
            fis = new FileInputStream(file);
            out = new FileOutputStream(file);
            properties.load(fis);
            properties.setProperty(key, value);
            properties.store(out, "");
        } catch (FileNotFoundException e) {
            LOGGER.error("properties can not find :" + filePath);
            System.exit(1);
        } catch (IOException e) {
            LOGGER.error("properties read exception:" + filePath);
            System.exit(1);
        } finally {
            readLock.unlock();
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error("FileInputStream fis close error:", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("FileOutputStream out close error:", e);
                }
            }
        }
    }

    /**
     * @param key
     * @param key1
     * @return
     */
    public static String getPropery(String key, String key1, String filePath) {
        filePath = PATH + filePath;
        Properties properties = PROPERTIES.get(key);
        FileInputStream fis = null;
        try {
            //先从内存中获取
            if (null == properties) {
                properties = new Properties();
                File file = new File(filePath);
                if (!file.exists()) {
                    LOGGER.error("properties can not find" + filePath);
                    System.exit(1);
                }
                fis = new FileInputStream(file);
                properties.load(fis);
                PROPERTIES.put(key, properties);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("properties can not find :" + filePath);
            System.exit(1);
        } catch (IOException e) {
            LOGGER.error("properties read exception:" + filePath);
            System.exit(1);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.error("FileInputStream fis close error:", e);
                }
            }
        }
        return properties.getProperty(key1);
    }


}
