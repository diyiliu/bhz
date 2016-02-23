package com.tiza.xgdl.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-11-05 10:18)
 * Version: 1.0
 * Updated:
 */
@ToString
public class ExchangeInfoBean {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private int command;
    @Getter
    @Setter
    private int direction;
    @Getter
    @Setter
    private String content;

}
