package com.terry.tank;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{
    private static final int SPEED = PropertyMgr.getAsInt("bulletSpeed");

    public static int WIDTH = ResourceMgr.bulletD.getWidth(), HEIGHT = ResourceMgr.bulletD.getHeight();
    public int x, y;
    private Dir dir;

    private boolean living = true;
    GameModel gm = null;
    Rectangle rect = new Rectangle();
    public Group group = Group.BAD;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Bullet(int x, int y, Dir dir, Group group, GameModel gm) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.gm = gm;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
        gm.add(this);
    }
    public void paint(Graphics g) {
        if (!living) {
            gm.remove(this);
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
//    public boolean collideWith(Tank tank) {
//        if (this.group == tank.getGroup()) return false;
//
//        // TODO: 用一个rect来记录子弹的位置
//        Rectangle rect1 = new Rectangle(this.x, this.y, WIDTH, HEIGHT);
//        Rectangle rect2 = new Rectangle(tank.getX(), tank.getY(), Tank.WIDTH, Tank.HEIGHT);
//        if (rect1.intersects(rect2)) {
//            tank.die();
//            this.die();
//            int eX = tank.getX() + (Tank.WIDTH - Explode.WIDTH)/2;
//            int eY = tank.getY() + (Tank.HEIGHT - Explode.HEIGHT)/2;
//            gm.add(new Explode(eX, eY, gm));
//            return true;
//        }
//        return false;
//    }

    public void die() {
        this.living = false;
    }
}
