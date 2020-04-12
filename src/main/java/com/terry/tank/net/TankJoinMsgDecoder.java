package com.terry.tank.net;

import com.terry.tank.Dir;
import com.terry.tank.Group;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.UUID;

public class TankJoinMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8) return; // TCP 拆包粘包问题

        in.markReaderIndex();

        MsgType msgType = MsgType.values()[in.readInt()];
        int lenght = in.readInt();

        if (in.readableBytes() < lenght) {
            in.resetReaderIndex(); // 重置读指针
            return;
        }

        byte[] bytes = new byte[lenght];
        in.readBytes(bytes);

        switch (msgType) {
            case TankJoin:
                TankJoinMsg msg = new TankJoinMsg();
                msg.parse(bytes);
                out.add(msg);
                break;
            default:
                break;
        }

        /*TankJoinMsg msg = new TankJoinMsg();

        msg.x = in.readInt();
        msg.y = in.readInt();
        msg.dir = Dir.values()[in.readInt()];
        msg.moving = in.readBoolean();
        msg.group = Group.values()[in.readInt()];
        msg.id = new UUID(in.readLong(), in.readLong());

        out.add(msg);*/
    }
}
