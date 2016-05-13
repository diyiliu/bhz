package com.tiza.xgdl.util;

import com.tiza.xgdl.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:DlUtil
 * @description:工具类,提供通用的方法
 * @date:2014/6/16 15:34
 */
public class DlUtil {
    private static Logger logger = LoggerFactory.getLogger(DlUtil.class);
    public static byte[] getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        byte[] array = new byte[6];
        array[0] = (byte) (cal.get(Calendar.YEAR) - 2000);
        array[1] = (byte) (cal.get(Calendar.MONTH) + 1);
        array[2] = (byte) cal.get(Calendar.DAY_OF_MONTH);
        array[3] = (byte) cal.get(Calendar.HOUR_OF_DAY);
        array[4] = (byte) cal.get(Calendar.MINUTE);
        array[5] = (byte) cal.get(Calendar.SECOND);
        return array;
    }

    /**
     * 获取 日 时 分 秒
     * @return
     */
    public static byte[] getTime() {
        Calendar cal = Calendar.getInstance();
        byte[] array = new byte[4];
        array[0] = (byte) cal.get(Calendar.DAY_OF_MONTH);
        array[1] = (byte) cal.get(Calendar.HOUR_OF_DAY);
        array[2] = (byte) cal.get(Calendar.MINUTE);
        array[3] = (byte) cal.get(Calendar.SECOND);
        return array;
    }

    /**
     * 7转8数组算法
     *
     * @param content
     * @return
     */
    public static byte[] s2e(String content) {
        StringBuilder srcBuild = new StringBuilder();
        for (int i = 0, j = content.length(); i < j; i += 2) {
            String str = content.substring(i, i + 2);
            srcBuild.append(b2s((byte) Integer.parseInt(str, 16)).substring(1));
        }
        //System.out.println(srcBuild);
        byte[] dis = new byte[srcBuild.length() / 8];
        for (int i = 0; i < dis.length; i++) {
            String str = srcBuild.substring(i * 8, i * 8 + 8);
            dis[i] = (byte) Integer.parseInt(str, 2);
        }

        return dis ;
       /* StringBuilder builder = new StringBuilder();
        for (byte b : dis) {
            builder.append(String.format("%02X", getNoSin(b)));
        }
        return builder.toString();*/
    }


    /**
     * 8转7数组算法
     *
     * @param src
     * @return
     */
    public static byte[] e2s(byte[] src) {
        StringBuffer buf = new StringBuffer();
        for (byte b : src) {
            buf.append(b2s(b));
        }
        StringBuffer fer = new StringBuffer();
        for (int i = 0, j = buf.length(); i < j; ) {
            if (i + 7 < j) {
                fer.append("0").append(buf.substring(i, i + 7));
                i += 7;
            } else {
                fer.append("0").append(buf.substring(i));
                while (i + 7 - j > 0) {
                    fer.append("0");
                    j++;
                }
                break;
            }
        }
        byte[] dis = new byte[fer.length() / 8];
        for (int i = 0; i < dis.length; i++) {
            String str = fer.substring(8 * i, 8 * (i + 1));
            dis[i] = (byte) Integer.parseInt(str, 2);
        }
        return dis;
    }

    /**
     * 字节转换成二进制字符串
     *
     * @param b
     * @return
     */
    private static String b2s(byte b) {
        StringBuffer str = new StringBuffer(Integer.toBinaryString(getNoSin(b)));
        int length = 8 - str.length();
        while (length > 0) {
            str.insert(0, "0");
            length--;
        }
        return str.toString();
    }

    public static String bytes2String(byte[] b) {
        StringBuffer buf = new StringBuffer();
        for (byte a : b) {
            buf.append(String.format("%02X", getNoSin(a))).append(" ");
        }
        return buf.toString();
    }

    /**
     * 获取字节的无符号数字
     *
     * @param b
     * @return
     */
    public static int getNoSin(byte b) {
        if (b >= 0) {
            return b;
        } else {
            return 256 + b;
        }
    }

    private static int serial = 1;

    private static int getSerial() {
        serial++;
        if (serial > 0xFF) {
            serial = 1;
        }
        return serial;
    }
    /**
     * 将时间字节数组 转换 为毫秒
     *
     * @return
     */
    public static long getTime(byte[] aTime) {
        int year = getNoSin(aTime[0]) + 2000;
        int month = getNoSin(aTime[1]) - 1;
        int day = getNoSin(aTime[2]);
        int hour = getNoSin(aTime[3]);
        int minute = getNoSin(aTime[4]);
        int second = getNoSin(aTime[5]);
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute, second);
        return cal.getTimeInMillis();
    }
    /**
     * 将命令内容加上包头,并添加到发送队列
     *
     * @param array
     */
    public static void pack(byte[] array) {
        logger.info("pack 中发送的长度：" + array.length);
        byte[] content = new byte[array.length + 9];
        content[0] = 0x7E;
        content[1] = 0x08;
        content[2] = 0x05;
        int len = array.length - 1;

        content[3] = (byte) (len >> 8);
        content[4] = (byte) len;

        content[5] = (byte) getSerial();
        System.arraycopy(array, 0, content, 6, array.length);
        int sum = 0;
        for (int i = 1; i < 6 + array.length; i++) {
            sum += getNoSin(content[i]);
        }
        content[6 + array.length] = (byte) sum;
        content[7 + array.length] = 0x0D;
        content[8 + array.length] = 0x0A;
        Main.workQueue.add(content);


        /*byte[] content = new byte[array.length + 8];
        content[0] = 0x7E;
        content[1] = 0x08;
        content[2] = 0x05;
        byte length = (byte) (array.length - 1);
        //content[3] = (byte) (array.length >> 8);
        content[3] = length;

        content[4] = (byte) getSerial();

        System.arraycopy(array, 0, content, 5, array.length);
        int sum = 0;
        for (int i = 1; i < 5 + array.length; i++) {
            sum += getNoSin(content[i]);
        }
        content[5 + array.length] = (byte) sum;
        content[6 + array.length] = 0x0D;
        content[7 + array.length] = 0x0A;
        App.workQueue.add(content);*/

    }

    public static byte getByteTime(Date date)[] {
        ByteBuffer buf = ByteBuffer.allocate(6);
        Calendar value = Calendar.getInstance();
        value.setTime(date);
        byte b1 = (byte) (value.get(Calendar.YEAR) - 2000);
        byte b2 = (byte) (value.get(Calendar.MONTH) + 1);
        byte b3 = (byte) value.get(Calendar.DATE);
        byte b4 = (byte) value.get(Calendar.HOUR_OF_DAY);
        byte b5 = (byte) value.get(Calendar.MINUTE);
        byte b6 = (byte) value.get(Calendar.SECOND);
        buf.put(b1);
        buf.put(b2);
        buf.put(b3);
        buf.put(b4);
        buf.put(b5);
        buf.put(b6);
        return buf.array();
    }

    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static byte[] getRandomBytes(int len, int max) {
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            ret[i] = (byte) (Math.random() * max);
        }
        return ret;
    }

    public static byte[] gZip(String data){
        return gZip(data.getBytes());
    }
    /**
     * 压缩
     * @param data
     * @return
     */
   public static byte[] gZip(byte[] data){
       byte[] b = null;
       ByteArrayOutputStream bos = null;
       GZIPOutputStream gzip = null;
       try {
           bos = new ByteArrayOutputStream();
           gzip = new GZIPOutputStream(bos);
           gzip.write(data);
           gzip.finish();
           gzip.close();
           b = bos.toByteArray();
           bos.close();
       } catch (IOException ex) {
           logger.error("压缩失败：", ex);
       }finally {
           if(gzip != null){
               try {
                   gzip.close();
               } catch (IOException e) {
                   logger.error("关闭异常：", e);
               }
           }
           if(bos != null){
               try {
                   bos.close();
               } catch (IOException e) {
                   logger.error("关闭异常：", e);
               }
           }
       }
       return b;
   }

    /**
     * 解压缩
     * @param data
     * @return
     */
    public static byte[] unGZip(byte[] data) {
        byte[] b = null;
        ByteArrayInputStream bis = null;
        GZIPInputStream gzip = null ;
        ByteArrayOutputStream baos = null ;
        try {
            bis = new ByteArrayInputStream(data);
            gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num = -1;
            baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();
        } catch (IOException ex) {
            logger.error("解压缩失败异常：" ,ex);
        }finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    logger.error("关闭异常：", e);
                }
            }
            if(gzip != null){
                try {
                    gzip.close();
                } catch (IOException e) {
                    logger.error("关闭异常：", e);
                }
            }
            if(bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error("关闭异常：", e);
                }
            }
        }
        return b;
    }

    /**
     * 校验和 ，自然溢出
     * @return
     */
    public static int checkSum(byte[] bytes){
        int sum = 0;
        for (int i = 0,len = bytes.length; i < len; i++) {
            sum += getNoSin(bytes[i]);
        }
        return sum;
    }
}
