package com.terry.tank.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Server {

    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void main(String[] args) {

    }

    public void serverStart() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);

        ServerBootstrap b = new ServerBootstrap();
        try {
            ChannelFuture f = b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pl = ch.pipeline();
                        pl.addLast(new TankJoinMsgDecoder())
                                .addLast(new ServerChildHandler());
                    }
                })
                .bind(8888)
                .sync();

            ServerFrame.INSTANCE.updateServerMsg("Server Started!");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

class ServerChildHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.clients.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
        try {
            TankJoinMsg tm = (TankJoinMsg) msg;
            System.out.println(tm);
        } finally {
            ReferenceCountUtil.release(msg);
        }

        /*ByteBuf buf = null;
        try {
            buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String s = new String(bytes);

            ServerFrame.INSTANCE.updateClientMsg(s);

            if ("_bye_".equals(s)) {
                ServerFrame.INSTANCE.updateServerMsg("客户端要求退出");
                Server.clients.remove(ctx.channel());
                ctx.close();
            } else {
                Server.clients.writeAndFlush(msg);
            }
        } finally {
//            if (buf != null) {
//                ReferenceCountUtil.release(buf);
//            }
        }*/

    }
}
