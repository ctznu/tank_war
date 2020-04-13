package com.terry.tank.net;

import com.terry.tank.Dir;
import com.terry.tank.Group;
import com.terry.tank.Tank;
import com.terry.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankJoinMsg extends Msg{
    public int x, y; // 8 bytes
    public Dir dir; // 4 b
    public boolean moving; // 1
    public Group group; // 4
    public UUID id; // 16

    public TankJoinMsg(Tank t) {
        this.x = t.getX();
        this.y = t.getY();
        this.dir = t.getDir();
        this.group = t.getGroup();
        this.id = t.getId();
        this.moving = t.isMoving();
    }

    public TankJoinMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
        super();
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.moving = moving;
        this.group = group;
        this.id = id;
    }

    public TankJoinMsg() {

    }

    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
            this.moving = dis.readBoolean();
            this.group = Group.values()[dis.readInt()];
            this.id = new UUID(dis.readLong(), dis.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeBoolean(moving);
            dos.writeInt(group.ordinal());
            dos.writeLong(id.getMostSignificantBits()); // 高64位
            dos.writeLong(id.getLeastSignificantBits()); // 低64位
            dos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    @Override
    public String toString() {
        return "TankJoinMsg{" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", moving=" + moving +
                ", group=" + group +
                ", id=" + id +
                '}';
    }

    @Override
    public void handle() {
        if (this.id.equals(TankFrame.INSTANCE.getMainTank().getId()) ||
                TankFrame.INSTANCE.findTankByUUID(this.id) != null) {
            return;
        }
        System.out.println(this);
        Tank t = new Tank(this);
        TankFrame.INSTANCE.addTank(t);

        // send a new TankJoinMsg to the new joined tank
        Client.INSTANCE.send(new TankJoinMsg(TankFrame.INSTANCE.getMainTank()));
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankJoin;
    }
}
