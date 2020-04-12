package com.terry.tank.net;

import com.terry.tank.Tank;
import com.terry.tank.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class Client {

    public static final Client INSTANCE = new Client();
    private Channel channel = null;

    private Client(){}
    public void connect() {
        // 线程池
        EventLoopGroup group = new NioEventLoopGroup(1);
        // 辅助启动类
        Bootstrap b = new Bootstrap();
        try {
            ChannelFuture f = b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer())
                .connect("localhost", 8888);
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        System.out.println("not connected");
                    } else {
                        channel = future.channel();
                        System.out.println("connected");
                    }
                }
            });
            try {
                f.sync(); // 阻塞 等待ChannelFuture的结束
                System.out.println("...");

                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } finally {
            group.shutdownGracefully();
        }
    }

    public void send(TankJoinMsg msg) {
        channel.writeAndFlush(msg);
    }

    public void closeConnect() {
//        this.send("_bye_");
    }
}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new TankJoinMsgEncoder())
                .addLast(new TankJoinMsgDecoder())
                .addLast(new ClientHandler());
    }
}

class ClientHandler extends SimpleChannelInboundHandler<TankJoinMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TankJoinMsg msg) throws Exception {

        msg.handle();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // channel 第一次连上可用，写出一个字符串 Direct Memory
//        ByteBuf buf = Unpooled.copiedBuffer("hello".getBytes());
//        ctx.writeAndFlush(buf); // buf 指向直接内存，所以必须释放，writeAndFlush 自动释放
        ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getMainTank()));
    }
}