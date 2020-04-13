package com.terry.tank.net;

import com.terry.tank.Dir;
import com.terry.tank.Tank;
import com.terry.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankDirChangedMsg extends Msg {
    UUID id;
    int x, y;
    Dir dir;

    public TankDirChangedMsg(UUID id, int x, int y, Dir dir) {
        super();
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public TankDirChangedMsg(Tank tank) {
        this.id = tank.getId();
        this.x = tank.getX();
        this.y = tank.getY();
        this.dir = tank.getDir();
    }

    public TankDirChangedMsg() {
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
        return "TankDirChangedMsg{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                '}';
    }

    @Override
    public void handle() {
        if (this.id.equals(TankFrame.INSTANCE.getMainTank().getId())) {
            return;
        }

        Tank t = TankFrame.INSTANCE.findTankByUUID(this.id);
        if (t != null) {
            t.setMoving(true);
            t.setX(x);
            t.setY(y);
            t.setDir(dir);
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
            dos.writeLong(id.getMostSignificantBits()); // 高64位
            dos.writeLong(id.getLeastSignificantBits()); // 低64位
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
        return MsgType.TankDirChanged;
    }
}
