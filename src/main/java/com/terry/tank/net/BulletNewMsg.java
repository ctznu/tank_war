package com.terry.tank.net;

import com.terry.tank.Bullet;
import com.terry.tank.Dir;
import com.terry.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class BulletNewMsg extends Msg{

    UUID playerID;
    UUID id;
    int x, y;
    Dir dir;

    public BulletNewMsg(Bullet bullet) {
        this.playerID = bullet.getPlayerID();
        this.id = bullet.getId();
        this.x = bullet.getX();
        this.y = bullet.getY();
        this.dir = bullet.getDir();
        TankFrame.INSTANCE.addBullet(bullet);
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

    @Override
    public void handle() {
        if (this.playerID.equals(TankFrame.INSTANCE.getMainTank().getId())) {
            return;
        }
        System.out.println(this);

        Bullet bullet = new Bullet(this);

        bullet.setId(this.id);
        TankFrame.INSTANCE.addBullet(bullet);

        // send a new TankJoinMsg to the new joined tank
//        Client.INSTANCE.send(new BulletNewMsg(bullet));
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
