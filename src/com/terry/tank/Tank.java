package com.terry.tank;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Tank {
    private int x, y;
    private Dir dir = Dir.DOWN;
    private static final int SPEED = PropertyMgr.getAsInt("tankSpeed");
    public static int WIDTH = ResourceMgr.goodTankU.getWidth(), HEIGHT = ResourceMgr.goodTankU.getHeight();

    private boolean moving = true;
    private Group group = Group.BAD;

    private TankFrame tf = null;
    private boolean living = true;
    Rectangle rect = new Rectangle();
    private Random random = new Random();

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

    public void paint(Graphics g) {
        if (!living) tf.tanks.remove(this);
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
        if(!moving) return;
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
        tf.bullets.add(new Bullet(bX, bY, this.dir, this.group, this.tf));
    }

    public void die() {
        this.living =false;
    }

    private void randomDir() {
        int r = random.nextInt(4);
        this.dir = Dir.values()[r];
    }

    private boolean onEdge() {
        return x <= 0 || x >= tf.GAME_WIDTH - WIDTH || y <= 0 || y >= tf.GAME_HEIGHT - HEIGHT;
    }
}
