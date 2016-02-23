package com.tiza.xgdl.bean;

import com.tiza.xgdl.util.DlUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.ByteBuffer;
import java.sql.Timestamp;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:HisRecord
 * @description:历史记录
 * @date:2014/5/20 16:41
 */
@ToString
public class HistoryRecord extends BaseBean {
    private static final int X = 1000;
    private static final short CONTROLLER_ID = 0x1104;
    @Getter
    @Setter
    private int batch;
    @Getter
    @Setter
    private Timestamp prodTime;
    @Getter
    @Setter
    private Timestamp prodDate;
    @Getter
    @Setter
    private int orderNo;
    @Getter
    @Setter
    private float bone1Weight;
    @Getter
    @Setter
    private float bone2Weight;
    @Getter
    @Setter
    private float bone3Weight;
    @Getter
    @Setter
    private float bone4Weight;
    @Getter
    @Setter
    private float bone5Weight;
    @Getter
    @Setter
    private float bone6Weight;
    @Getter
    @Setter
    private float dust1Weight;
    @Getter
    @Setter
    private float dust2Weight;
    @Getter
    @Setter
    private float pitchWeight;
    @Getter
    @Setter
    private float bone1Error;
    @Getter
    @Setter
    private float bone2Error;
    @Getter
    @Setter
    private float bone3Error;
    @Getter
    @Setter
    private float bone4Error;
    @Getter
    @Setter
    private float bone5Error;
    @Getter
    @Setter
    private float bone6Error;
    @Getter
    @Setter
    private float dust1Error;
    @Getter
    @Setter
    private float dust2Error;
    @Getter
    @Setter
    private float pitchError;
    @Getter
    @Setter
    private float singleWeight;
    @Getter
    @Setter
    private String matchName;
    @Getter
    @Setter
    private float bone1Goal;
    @Getter
    @Setter
    private float bone2Goal;
    @Getter
    @Setter
    private float bone3Goal;
    @Getter
    @Setter
    private float bone4Goal;
    @Getter
    @Setter
    private float bone5Goal;
    @Getter
    @Setter
    private float bone6Goal;
    @Getter
    @Setter
    private float dust1Goal;
    @Getter
    @Setter
    private float dust2Goal;
    @Getter
    @Setter
    private float pitchGoal;
    @Getter
    @Setter
    private float finProTemp;
    @Getter
    @Setter
    private byte netFlag;
    @Getter
    @Setter
    private byte interFlag;
    @Getter
    @Setter
    private float bone1Feed;
    @Getter
    @Setter
    private float bone2Feed;
    @Getter
    @Setter
    private float bone3Feed;
    @Getter
    @Setter
    private float bone4Feed;
    @Getter
    @Setter
    private float bone5Feed;

    @Override
    public void sendArray() throws Exception {//158
        ByteBuffer buffer = ByteBuffer.allocate(205);
        //ByteBuffer buffer = ByteBuffer.allocate(205 - 20);
        buffer.clear();
        //设置命令ID
        buffer.put((byte) 0xAD);
        //控制系统ID
        buffer.putShort(CONTROLLER_ID);
        //长度
        buffer.putShort((short) 200);

        /*buffer.putShort((short)0x1100);
        buffer.putShort((short)0x04B4);*/

        //长度头
        buffer.putShort((short) 0x0001);
        //订单号
        buffer.putInt(this.getOrderNo());
        //批次
        buffer.putInt(this.getBatch());

        buffer.putShort((short) 0x0002);
        //日期
        buffer.put(new byte[2]);
        buffer.put(DlUtil.getByteTime(this.getProdDate()));
        //骨1 ~ 6 重量
        buffer.putShort((short) 0x0003);
        buffer.putInt((int) (this.getBone2Weight() * X));
        buffer.putInt((int) (this.getBone1Weight() * X));
        buffer.putShort((short) 0x0004);
        buffer.putInt((int) (this.getBone4Weight() * X));
        buffer.putInt((int) (this.getBone3Weight() * X));
        buffer.putShort((short) 0x0005);
        buffer.putInt((int) (this.getBone6Weight() * X));
        buffer.putInt((int) (this.getBone5Weight() * X));
        //粉1 ~ 2 重量
        buffer.putShort((short) 0x0006);
        buffer.putInt((int) (this.getDust2Weight() * X));
        buffer.putInt((int) (this.getDust1Weight() * X));
        //沥青重量 骨1误差
        buffer.putShort((short) 0x0007);
        buffer.putInt((int) ((this.getBone1Error()+10) * X));
        buffer.putInt((int) (this.getPitchWeight() * X));
        //骨2误差 骨3误差
        buffer.putShort((short) 0x0008);
        buffer.putInt((int) ((this.getBone3Error()+10) * X));
        buffer.putInt((int) ((this.getBone2Error()+10) * X));
        //骨4误差 骨5误差
        buffer.putShort((short) 0x0009);
        buffer.putInt((int) ((this.getBone5Error()+10) * X));
        buffer.putInt((int) ((this.getBone4Error()+10) * X));
        //骨6误差 粉1误差
        buffer.putShort((short) 0x000A);
        buffer.putInt((int) ((this.getDust1Error()+10) * X));
        buffer.putInt((int) ((this.getBone6Error()+10) * X));
        //粉2误差 沥青误差
        buffer.putShort((short) 0x000B);
        buffer.putInt((int) ((this.getPitchError()+10) * X));
        buffer.putInt((int) ((this.getDust2Error()+10) * X));
        //单盘重量 骨1目标
        buffer.putShort((short) 0x000C);
        buffer.putInt((int) (this.getBone1Goal() * X));
        buffer.putInt((int) (this.getSingleWeight() * X));
        //骨2目标 ~ 5
        buffer.putShort((short) 0x000D);
        buffer.putInt((int) (this.getBone3Goal() * X));
        buffer.putInt((int) (this.getBone2Goal() * X));
        buffer.putShort((short) 0x000E);
        buffer.putInt((int) (this.getBone5Goal() * X));
        buffer.putInt((int) (this.getBone4Goal() * X));
        //骨6目标 粉1目标
        buffer.putShort((short) 0x000F);
        buffer.putInt((int) (this.getDust1Goal() * X));
        buffer.putInt((int) (this.getBone6Goal() * X));
        //粉2目标 沥青目标
        buffer.putShort((short) 0x0010);
        buffer.putInt((int) (this.getPitchGoal() * X));
        buffer.putInt((int) (this.getDust2Goal() * X));
        //成品料温 联网标志(意义未知) 干预标志(意义未知)
        buffer.putShort((short) 0x0011);
        buffer.put(new byte[2]);
        buffer.put(this.getInterFlag());
        buffer.put(this.getNetFlag());
        buffer.putInt((int) (this.getFinProTemp() * X));
        //骨1补料 ~ 骨5补料
        buffer.putShort((short) 0x0012);
        buffer.putInt((int) (this.getBone2Feed() * X));
        buffer.putInt((int) (this.getBone1Feed() * X));
        buffer.putShort((short)0x0013);
        buffer.putInt((int) (this.getBone4Feed() * X));
        buffer.putInt((int) (this.getBone3Feed() * X));
        buffer.putShort((short)0x0014);
        buffer.put(new byte[4]);
        buffer.putInt((int) (this.getBone5Feed() * X));
        buffer.flip();
        DlUtil.pack(buffer.array());
    }
}
