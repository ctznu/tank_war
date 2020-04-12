package com.terry.tank;

import com.terry.tank.net.BulletNewMsg;
import com.terry.tank.net.Client;
import com.terry.tank.net.TankDieMsg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public class Bullet {
    private static final int SPEED = PropertyMgr.getAsInt("bulletSpeed");

    public static int WIDTH = ResourceMgr.bulletD.getWidth(), HEIGHT = ResourceMgr.bulletD.getHeight();
    private int x, y;
    private Dir dir;

    private boolean living = true;

    private UUID id = UUID.randomUUID();
    private UUID playerID;

    TankFrame tf = null;
    Rectangle rect = new Rectangle();
    private Group group = Group.BAD;

    public Bullet(BulletNewMsg msg) {
        this.playerID = msg.getPlayerID();
        this.id = msg.getId();
        this.x = msg.getX();
        this.y = msg.getY();
        this.dir = msg.getDir();
        this.living = true;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public UUID getId() {
        return id;
    }

    public Dir getDir() {
        return dir;
    }

    public boolean isLiving() {
        return living;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public Bullet(UUID playerID, int x, int y, Dir dir, Group group, TankFrame tf) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }
    public void paint(Graphics g) {
        if (!living) {
            tf.bullets.remove(this);
        }

        BufferedImage img = null;
        switch (dir) {
            case LEFT:
                img = ResourceMgr.bulletL;
                break;
            case UP:
                img = ResourceMgr.bulletU;
                break;
            case RIGHT:
                img = ResourceMgr.bulletR;
                break;
            case DOWN:
                img = ResourceMgr.bulletD;
                break;
        }

        g.drawImage(img, x, y, null);
        move();


    }

    private void move() {
        switch (dir) {
            case LEFT:
                x -= SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
        }

        // update rect
        rect.x = this.x;
        rect.y = this.y;
        if (x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT) {
            living = false;
        }
    }

    // 碰撞检测
    public void collideWith(Tank tank) {
        if (this.playerID.equals(tank.getId())) return;

        if (this.living && tank.isLiving() && this.rect.intersects(tank.rect)) {
            tank.die();
            this.die();
            Client.INSTANCE.send(new TankDieMsg(this.id, tank.getId()));
        }
    }

    public void die() {
        this.living = false;
    }
}
