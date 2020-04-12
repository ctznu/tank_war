package com.terry.tank.net;

import com.terry.tank.Bullet;
import com.terry.tank.Tank;
import com.terry.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankDieMsg extends Msg {
    UUID bulletID; // who killed me
    UUID id;

    public TankDieMsg(UUID bulletID, UUID id) {
        this.bulletID = bulletID;
        this.id = id;
    }

    public TankDieMsg(Tank tank) {
        this.id = tank.getId();
    }

    public TankDieMsg() {
    }

    public UUID getId() {
        return id;
    }

    public void setBulletID(UUID bulletID) {
        this.bulletID = bulletID;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBulletID() {
        return bulletID;
    }

    @Override
    public String toString() {
        return "TankDieMsg{" +
                "bulletID=" + bulletID +
                ", id=" + id +
                '}';
    }

    @Override
    public void handle() {
        System.out.println("we got a tank die: " + id);
        System.out.println("and my tank is: " + TankFrame.INSTANCE.getMainTank().getId());
        Tank tt = TankFrame.INSTANCE.findTankByUUID(id);
        System.out.println("i found a tank with this id: " + tt);

        Bullet b = TankFrame.INSTANCE.findBulletByUUID(bulletID);
        if (b != null) {
            b.die();
        }

        if (this.id.equals(TankFrame.INSTANCE.getMainTank().getId())) {
            TankFrame.INSTANCE.getMainTank().die();
        } else {
            if (tt != null) {
                tt.die();
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
            dos.writeLong(bulletID.getMostSignificantBits());
            dos.writeLong(bulletID.getLeastSignificantBits());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
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
            this.bulletID = new UUID(dis.readLong(), dis.readLong());
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
    public MsgType getMsgType() {
        return MsgType.TankDie;
    }
}
