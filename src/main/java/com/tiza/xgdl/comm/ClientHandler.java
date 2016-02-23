package com.tiza.xgdl.comm;

import com.tiza.xgdl.Main;
import com.tiza.xgdl.command.BaseCommand;
import com.tiza.xgdl.command.CommandFactory;
import com.tiza.xgdl.command.SpecialHeartBeat;
import com.tiza.xgdl.db.AlarmInfoDao;
import com.tiza.xgdl.db.HistoryRecordDao;
import com.tiza.xgdl.db.RealTimeCollectOneDao;
import com.tiza.xgdl.db.RealTimeCollectTwoDao;
import com.tiza.xgdl.util.DlUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:ClientHandler
 * @description:类的描述
 * @date:2014/5/14 16:11
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private ClientConnector connector;
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(7);

    public ClientHandler(ClientConnector connector) {
        this.connector = connector;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("--------连接串口成功----------");
        Main.comCTX = ctx;
        new Thread(new SendDataCom()).start();
        //开启心跳定时发送
        executor.scheduleAtFixedRate(SpecialHeartBeat.getSendBeat(), 0, 12, TimeUnit.HOURS);

        executor.scheduleAtFixedRate(new ReSentService(), 0, 10, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new AlarmInfoDao(), 0, 30, TimeUnit.SECONDS);

        executor.scheduleAtFixedRate(new HistoryRecordDao(), 0, 30, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new RealTimeCollectOneDao(), 0, 30, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new RealTimeCollectTwoDao(), 0, 30, TimeUnit.SECONDS);

    }

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {

        //心跳处理
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                //读超时
                System.out.println("READER_IDLE 读超时");
                //ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                //写超时
                System.out.println("WRITER_IDLE 写超时");
            } else if (event.state() == IdleState.ALL_IDLE) {
                //总超时
                System.out.println("ALL_IDLE 总超时");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //解析响应
        ByteBuf buf = (ByteBuf) msg;
        byte[] testBytes = new byte[buf.readableBytes()];
        buf.readBytes(testBytes);
        logger.info("最终获取的信息为：" + DlUtil.bytes2String(testBytes));

        buf.resetReaderIndex();
        byte[] temp = new byte[3];
        buf.readBytes(temp);
        int length = buf.readUnsignedShort();
        int serial = buf.readByte();
        int orderId = buf.readUnsignedByte();
        byte[] content = new byte[length];
        buf.readBytes(content);
        BaseCommand command = CommandFactory.getCommand(orderId);
        if (null == command) {
            logger.info("未能获取解析类，命令ID为:" + orderId);
            return;
        }
        command.decodeMessageRet(ByteBuffer.wrap(content),temp,length,serial);


        /*switch (orderId) {
            case 0x1A:
                boolean isOk = HeartBeat.comparePwd(content);
                System.out.println("校验心跳的结果为：" + isOk);
                ;
                break;
            case 0xAA:
                System.out.println("nothing now...");
                ;
                break;
            default:
                System.out.println("nothing now...");
                ;
                break;
        }*/
       /* ByteBuf buf = (ByteBuf) msg;
        int length = buf.readInt();
        byte[] content = new byte[length - 4];
        buf.readBytes(content);*/
       /* StringBuffer sbuf = new StringBuffer();
        for (int i = 0; i < content.length; i++) {
            sbuf.append(String.format("%02X", content[i] & 0xFF));
            sbuf.append(" ");
        }

        System.out.println("长度:" + length + ",内容:" + sbuf.toString());*/
       /* ByteBuf snd = ctx.alloc().buffer(5);
        snd.writeInt(5);
        snd.writeByte(2);
        ctx.writeAndFlush(snd);
        System.out.println("发送了一条应答信息");*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String address = ctx.channel().remoteAddress().toString();
        cause.printStackTrace();
        logger.error(address + " Socket连接异常:", cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                logger.info("串口连接断开,正在重连中...");
                connector = new ClientConnector(new RxtxDeviceAddress(Main.getConfig("com")));
                connector.run();
            }
        }, 5, TimeUnit.SECONDS);
    }
}
