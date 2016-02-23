package com.tiza.xgdl.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Description: Common
 * Author: DIYILIU
 * Update: 2016-01-14 14:21
 */
public class Common {

    public static String getFilePath(String name) {

        String path = null;

        try {
            path = System.getProperty("user.dir") + File.separator + name;
            File file = new File(path);
            if (!file.exists()) {
                path = Common.class.getClassLoader().getResource(name).getPath();
            }

            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return path;
    }
}
