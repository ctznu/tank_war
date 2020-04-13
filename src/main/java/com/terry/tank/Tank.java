package com.terry.tank;

import com.terry.tank.net.BulletNewMsg;
import com.terry.tank.net.Client;
import com.terry.tank.net.TankJoinMsg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;

public class Tank {
    private int x, y;
    private Dir dir = Dir.DOWN;
    private static final int SPEED = PropertyMgr.getAsInt("tankSpeed");
    public static int WIDTH = ResourceMgr.goodTankU.getWidth(), HEIGHT = ResourceMgr.goodTankU.getHeight();

    private boolean moving = false;
    private Group group = Group.BAD;

    private TankFrame tf = null;
    private boolean living = true;
    Rectangle rect = new Rectangle();
    private Random random = new Random();

    private UUID id = UUID.randomUUID();

    public Tank(int x, int y, Dir dir, Group group, TankFrame tf) {
        super();
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
//        TankFrame.INSTANCE.addTank(this);
    }

    public Tank(TankJoinMsg msg) {
        this.x = msg.x;
        this.y = msg.y;
        this.dir = msg.dir;
        this.moving = msg.moving;
        this.group = msg.group;
        this.id = msg.id;

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public boolean isLiving() {
        return living;
    }

    public void setLiving(boolean living) {
        this.living = living;
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

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void paint(Graphics g) {
        if (!living) {
            TankFrame.INSTANCE.tanks.remove(this.getId());
            return;
        }
        //uuid on head
        Color c = g.getColor();
        g.setColor(Color.orange);
        g.drawString(id.toString(), x, y - 20);
        g.drawString("live=" + living, x, y - 10);
        g.setColor(c);

        BufferedImage img = null;
        boolean isGood = this.group == Group.GOOD;
        switch (dir) {
            case LEFT:
                img = isGood ? ResourceMgr.goodTankL : ResourceMgr.badTankL;
                break;
            case UP:
                img = isGood ? ResourceMgr.goodTankU : ResourceMgr.badTankU;
                break;
            case RIGHT:
                img = isGood ? ResourceMgr.goodTankR : ResourceMgr.badTankR;
                break;
            case DOWN:
                img = isGood ? ResourceMgr.goodTankD : ResourceMgr.badTankD;
                break;
        }

        g.drawImage(img, x, y, null);
        move();


    }

    private void move() {
        if(!moving || !living) return;
        // 根据方向进行移动
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

        if (this.group == Group.BAD && random.nextInt(50) > 48 ) {
            this.fire();
            randomDir();
        }

        boundsCheck();

        // update rect
        rect.x = this.x;
        rect.y = this.y;
    }

    private void boundsCheck() {
        if (this.x < 2) {
            x = 2;
        }
        if (this.y < 28) {
            y = 28;
        }
        if (this.x > TankFrame.GAME_WIDTH - Tank.WIDTH -2) {
            x = TankFrame.GAME_WIDTH - Tank.WIDTH -2;
        }
        if (this.y > TankFrame.GAME_HEIGHT - Tank.HEIGHT -2) {
            y = TankFrame.GAME_HEIGHT - Tank.HEIGHT -2;
        }
    }

    public void fire() {
        int bX = this.x + (Tank.WIDTH - Bullet.WIDTH)/2;
        int bY = this.y + (Tank.HEIGHT - Bullet.HEIGHT)/2;

        Bullet bullet = new Bullet(this.id, bX, bY, this.dir, this.group, this.tf);
        tf.bullets.add(bullet);

        Client.INSTANCE.send(new BulletNewMsg(bullet));

        if (this.group == Group.GOOD) {
            new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
        }
    }

    public void die() {
        this.living =false;
        int eX = this.getX() + Tank.WIDTH/2 - Explode.WIDTH/2;
        int eY = this.getY() + Tank.HEIGHT/2 - Explode.HEIGHT/2;
        TankFrame.INSTANCE.explodes.add(new Explode(eX, eY));
    }

    private void randomDir() {
        int r = random.nextInt(4);
        this.dir = Dir.values()[r];
    }

}
