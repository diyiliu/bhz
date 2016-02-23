package com.tiza.xgdl.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:ShowRecord
 * @description:显示记录
 * @date:2014/5/22 17:05
 */
@ToString
public class ShowRecord {
    @Getter
    @Setter
    private Timestamp recordTime;
    @Getter
    @Setter
    private float idfElect;
    @Getter
    @Setter
    private float stirrer1Elect;
    @Getter
    @Setter
    private float stirrer2Elect;
    @Getter
    @Setter
    private float riddler1Elect;
    @Getter
    @Setter
    private float riddler2Elect;
    @Getter
    @Setter
    private float hotRiseElect;
    @Getter
    @Setter
    private float dryDrum1Elect;
    @Getter
    @Setter
    private float dryDrum2Elect;
    @Getter
    @Setter
    private float dryDrum3Elect;
    @Getter
    @Setter
    private float dryDrum4Elect;
    @Getter
    @Setter
    private float airDoorOpen;
    @Getter
    @Setter
    private float dedustPress1;
    @Getter
    @Setter
    private float dedustPress2;
    @Getter
    @Setter
    private float flueTemp;
    @Getter
    @Setter
    private float coldMeFc1;
    @Getter
    @Setter
    private float coldMeFc2;
    @Getter
    @Setter
    private float coldMeFc3;
    @Getter
    @Setter
    private float coldMeFc4;
    @Getter
    @Setter
    private float coldMeFc5;
    @Getter
    @Setter
    private float coldMeFc6;
    @Getter
    @Setter
    private float coldMeFcTotal;
    @Getter
    @Setter
    private float coldMe1Set;
    @Getter
    @Setter
    private float coldMe2Set;
    @Getter
    @Setter
    private float coldMe3Set;
    @Getter
    @Setter
    private float coldMe4Set;
    @Getter
    @Setter
    private float coldMe5Set;
    @Getter
    @Setter
    private float coldMe6Set;
    @Getter
    @Setter
    private float powderScales;
    @Getter
    @Setter
    private float aggregateScales;
    @Getter
    @Setter
    private float pitchScales;
    @Getter
    @Setter
    private float dryDrumPress;
    @Getter
    @Setter
    private float pitchTemp;
    @Getter
    @Setter
    private float sandTemp;
    @Getter
    @Setter
    private float fuelDoor;
    @Getter
    @Setter
    private float airDoor;
    @Getter
    @Setter
    private float outletTemp;
    @Getter
    @Setter
    private float countDown;
    @Getter
    @Setter
    private float finiProdTemp;
    @Getter
    @Setter
    private float port1ChanSpeed;
    @Getter
    @Setter
    private float port1GoStop;
    @Getter
    @Setter
    private float port1BackStop;
    @Getter
    @Setter
    private float port2ChanSpeed;
    @Getter
    @Setter
    private float port2GoStop;
    @Getter
    @Setter
    private float port2BackStop;
    @Getter
    @Setter
    private float heavyOilTemp;
    @Getter
    @Setter
    private float tailGasTemp2;
    @Getter
    @Setter
    private float oilPress;
    @Getter
    @Setter
    private float materialTempSet;
    @Getter
    @Setter
    private float standbyTime;
    @Getter
    @Setter
    private String matchName;
    @Getter
    @Setter
    private int batch;
    @Getter
    @Setter
    private float singleDiskWeigth;
    @Getter
    @Setter
    private Timestamp produceTime;
    @Getter
    @Setter
    private Timestamp currentTime;


}
