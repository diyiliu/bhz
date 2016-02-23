package com.tiza.xgdl.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;


/**
 * @author:chen_gc@tiza.com.cn
 * @className:AlarmInfo
 * @description:故障信息对应ReportData中的AlarmInfo表
 * @date:2014/5/19 15:47
 */
@ToString
public class FailureInfo {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String AlmPosition;
    @Getter
    @Setter
    private Timestamp AlmTime;
    @Getter
    @Setter
    private String AlmInfo;
    @Getter
    @Setter
    private int AlmStatus;
    @Getter
    @Setter
    private int AlmClass;
    @Getter
    @Setter
    private float AlmValue;
    @Getter
    @Setter
    private int AlmNo;
    @Getter
    @Setter
    private String AlmMono;
}
