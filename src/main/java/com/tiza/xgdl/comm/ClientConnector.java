package com.tiza.xgdl.comm;

import com.tiza.xgdl.Main;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.channel.rxtx.RxtxChannelOption;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @author:chen_gc@tiza.com.cn
 * @className:ClientConnector
 * @description:类的描述
 * @date:2014/6/27 14:58
 */
public class ClientConnector {
    private final ClientHandler handler = new ClientHandler(this);
    private final SocketAddress address;
    protected Logger LOGGER = LoggerFactory.getLogger(ClientConnector.class);

    public ClientConnector(SocketAddress address) {
        this.address = address;
    }

    private Bootstrap configureBootstrap(Bootstrap b) {
        return configureBootstrap(b, new OioEventLoopGroup());
    }

    public void run() {
        try {
            ChannelFuture f = configureBootstrap(new Bootstrap()).connect().sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("串口连接失败[{}]:", ((RxtxDeviceAddress) address).value(), e);
        }
    }

    public Bootstrap configureBootstrap(Bootstrap b, EventLoopGroup g) {
        b.group(g).channel(RxtxChannel.class).remoteAddress(address)
                .option(RxtxChannelOption.AUTO_READ, true)
                .option(RxtxChannelOption.RTS, true)
                .option(RxtxChannelOption.BAUD_RATE, 9600)
                .option(RxtxChannelOption.PARITY_BIT, RxtxChannelConfig.Paritybit.NONE)//无奇偶校验
                .option(RxtxChannelOption.DATA_BITS, RxtxChannelConfig.Databits.DATABITS_8)
                .option(RxtxChannelOption.STOP_BITS, RxtxChannelConfig.Stopbits.STOPBITS_1)
                .handler(new ChannelInitializer<RxtxChannel>() {
                    @Override
                    protected void initChannel(RxtxChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", new ClientDecoder());
                        // 心跳控制
                        //pipeline.addLast("heartBeat", new IdleStateHandler(30, 0, 0));
                        pipeline.addLast("handler", handler);
                        pipeline.addLast("encoder", new ClientEncoder());
                    }
                });
        return b;
    }

    public static void main(String[] args) {
        ClientConnector connector = new ClientConnector(new RxtxDeviceAddress(Main.getConfig("com")));
        connector.run();
    }
}
