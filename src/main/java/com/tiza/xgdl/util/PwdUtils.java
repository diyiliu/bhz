package com.tiza.xgdl.util;

/**
 * Description: 半字节进行加密
 * Author: Wolf
 * Created:Wolf-(2014-07-12 16:09)
 * Version: 1.0
 * Updated:
 */
public class PwdUtils {
    private static final int mod = 16;

    /**
     * @param pwd
     * @param key1
     * @param key2
     * @param key3
     * @return
     */
    public static byte[] encode(byte[] pwd, int key1, int key2, int key3) {
        int len = pwd.length;
        byte[] encodePwd = new byte[len];
        for (int i = 0; i < len; i++) {
            encodePwd[i] = encodByte(pwd[i], key1, key2, key3);
        }
        return encodePwd;
    }

    private static byte encodByte(byte code, int key1, int key2, int key3) {
        byte high = (byte) ((((code >> 4) * key1 + key2) % mod));
        byte low = (byte) (((code & 0xF) * key1 + key2) % mod);
        byte val = (byte) ((high << 4) | low);
        return val;
    }

    private static byte decodByte(byte code, int key1, int key2, int key3) {
        byte highTemp = (byte) ((code >> 4) - key2);
        byte high = (byte) (key3 * highTemp % mod);
        if (highTemp < 0) {
            high = (byte) ((byte) Math.IEEEremainder(highTemp, mod));
        }
        byte lowTemp = (byte) ((code & 0xF) - key2);
        //byte high = (byte) ((key3 * Math.abs(((code >> 4) - key2)) % mod) & 0xF0);
        byte low = (byte) (key3 * lowTemp % mod);
        if (low < 0) {
            low = (byte) ((Math.IEEEremainder(key3 * lowTemp, mod)));
        }
        byte val = (byte) ((high << 4) | low);
        return val;
    }
    /*private static byte encodByte(byte code, int key1, int key2, int key3) {
        byte high = (byte) (((byte) Math.IEEEremainder((code >> 4) * key1 + key2, mod)) & 0xF0);
        byte low = (byte) Math.IEEEremainder((code & 0xF) * key1 + key2, mod);
        byte val = (byte) (high | low);
        return val;
    }

    private static byte decodByte(byte code, int key1, int key2, int key3) {
        byte high = (byte) (((byte) Math.IEEEremainder(key3 * ((code >> 4) - key2), mod)) & 0xF0);
        byte low = (byte) Math.IEEEremainder(key3 * ((code & 0xF) - key2), mod);
        byte val = (byte) (high | low);
        return val;
    }*/
    //private static byte sub

    /**
     * @param enpwd
     * @param key1
     * @param key2
     * @param key3
     * @return
     */
    public static byte[] decode(byte[] enpwd, int key1, int key2, int key3) {
        int len = enpwd.length;
        byte[] decodePwd = new byte[len];
        for (int i = 0; i < len; i++) {
            decodePwd[i] = decodByte(enpwd[i], key1, key2, key3);
        }
        return decodePwd;
    }
}
