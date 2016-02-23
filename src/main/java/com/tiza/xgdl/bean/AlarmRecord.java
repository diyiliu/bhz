package com.tiza.xgdl.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:AlarmRecord
 * @description:报警记录对应UserDatabase的"报警记录"表
 * @date:2014/7/2 14:53
 */
@ToString
public class AlarmRecord {
    @Getter
    @Setter
    private Timestamp alarmTime;
    @Getter
    @Setter
    private int alarmNo;
}
