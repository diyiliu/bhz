package com.tiza.xgdl.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:MatchBill
 * @description:配比单
 * @date:2014/6/13 15:38
 */
@ToString
public class MatchBill {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int oilSto;
    @Getter
    @Setter
    private float bone1;
    @Getter
    @Setter
    private  float bone2;
    @Getter
    @Setter
    private float bone3;
    @Getter
    @Setter
    private float bone4;
    @Getter
    @Setter
    private float bone5;
    @Getter
    @Setter
    private float bone6;
    @Getter
    @Setter
    private float dust1;
    @Getter
    @Setter
    private float dust2;
    @Getter
    @Setter
    private float pitch;
    @Getter
    @Setter
    private float singleWeight;
    @Getter
    @Setter
    private int setNonPitch;
    @Getter
    @Setter
    private String scaleType;

}
