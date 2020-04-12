package com.terry.tank.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class Client {

    private Channel channel = null;

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

    public void send(String msg) {
        ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes());
        channel.writeAndFlush(buf);
    }

    public static void main(String[] args) {
        Client c = new Client();
        c.connect();
    }

    public void closeConnect() {
        this.send("_bye_");
    }
}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new TankJoinMsgEncoder())
                .addLast(new ClientHandler());
    }
}

class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = null;
        try {
            buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            System.out.println("client: " + new String(bytes));
//            ClientFrame.INSTANCE.updateText(new String(bytes));
        } finally {
            if (buf != null) {
                ReferenceCountUtil.release(buf);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // channel 第一次连上可用，写出一个字符串 Direct Memory
//        ByteBuf buf = Unpooled.copiedBuffer("hello".getBytes());
//        ctx.writeAndFlush(buf); // buf 指向直接内存，所以必须释放，writeAndFlush 自动释放
//        ctx.writeAndFlush(new TankJoinMsg(5, 8));
    }
}