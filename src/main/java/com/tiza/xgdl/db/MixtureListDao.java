package com.tiza.xgdl.db;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-10-08 16:02)
 * Version: 1.0
 * Updated:
 */
public class MixtureListDao implements Runnable {
    @Override
    public void run() {
        String sql = "SELECT [配比名称], [油石比], [骨料1], [骨料2], [骨料3], [骨料4], [骨料5], [骨料6], [粉料1], [粉料2], [沥青], [单盘重量], [手动设空沥青], [比例类型] FROM [UserDatabase].[dbo].[配比单]";
    }
}
