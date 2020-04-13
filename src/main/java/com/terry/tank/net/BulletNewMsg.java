package com.terry.tank.net;

import com.terry.tank.Bullet;
import com.terry.tank.Dir;
import com.terry.tank.Group;
import com.terry.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class BulletNewMsg extends Msg{

    UUID playerID;
    UUID id;
    int x, y;
    Dir dir;
    Group group;

    public BulletNewMsg(Bullet bullet) {
        this.playerID = bullet.getPlayerID();
        this.id = bullet.getId();
        this.x = bullet.getX();
        this.y = bullet.getY();
        this.dir = bullet.getDir();
        this.group = bullet.getGroup();
    }

    public BulletNewMsg() {
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public UUID getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Dir getDir() {
        return dir;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "BulletNewMsg{" +
                "playerID=" + playerID +
                ", id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", group=" + group +
                '}';
    }

    @Override
    public void handle() {
        System.out.println("new bullet: " + this);
        if (this.playerID.equals(TankFrame.INSTANCE.getMainTank().getId())) {
            return;
        }

        Bullet bullet = new Bullet(this.playerID, x, y, dir, group, TankFrame.INSTANCE);

        bullet.setId(this.id);
        TankFrame.INSTANCE.addBullet(bullet);

    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);

            dos.writeLong(this.playerID.getMostSignificantBits());
            dos.writeLong(this.playerID.getLeastSignificantBits());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeInt(group.ordinal());
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
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            this.playerID = new UUID(dis.readLong(), dis.readLong());
            this.id = new UUID(dis.readLong(), dis.readLong());
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
            this.group = Group.values()[dis.readInt()];
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
    public MsgType getMsgType() {
        return MsgType.BulletNew;
    }
}
