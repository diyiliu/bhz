package com.tiza.xgdl.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-10-09 14:00)
 * Version: 1.0
 * Updated:
 */
@ToString
public class CommonBean extends BaseBean {
    @Getter
    @Setter
    private String result;
    @Getter
    @Setter
    private String compareField;
    @Override
    public void sendArray() throws Exception {

    }
}
