package com.terry.tank.net;

import com.terry.tank.Dir;
import com.terry.tank.Group;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.UUID;

public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8) return; // TCP 拆包粘包问题

        in.markReaderIndex();

        MsgType msgType = MsgType.values()[in.readInt()];
        int length = in.readInt();

        if (in.readableBytes() < length) {
            in.resetReaderIndex(); // 重置读指针
            return;
        }

        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        Msg msg = null;
        /*switch (msgType) {
            case TankJoin:
                msg = new TankJoinMsg();
                break;
            case TankStartMoving:
                msg = new TankStartMovingMsg();
                break;
            case TankStop:
                msg = new TankStopMsg();
                break;
            default:
                break;
        }*/
        msg = (Msg) Class.forName("com.terry.tank.net." + msgType.toString() + "Msg")
                .getDeclaredConstructor().newInstance();
        msg.parse(bytes);
        out.add(msg);

    }
}
