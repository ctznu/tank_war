package com.terry.tank;

import com.terry.tank.abstractfactory.BaseBullet;
import com.terry.tank.abstractfactory.BaseTank;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet extends BaseBullet {
    private static final int SPEED = PropertyMgr.getAsInt("bulletSpeed");

    public static int WIDTH = ResourceMgr.bulletD.getWidth(), HEIGHT = ResourceMgr.bulletD.getHeight();
    private int x, y;
    private Dir dir;

    private boolean living = true;
    TankFrame tf = null;
    Rectangle rect = new Rectangle();
    private Group group = Group.BAD;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Bullet(int x, int y, Dir dir, Group group, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
        tf.bullets.add(this);
    }

    @Override
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
    public void collideWith(BaseTank tank) {
        if (this.group == tank.getGroup()) return;

        if (rect.intersects(tank.rect)) {
            tank.die();
            this.die();
            int eX = tank.getX() + (Tank.WIDTH - Explode.WIDTH)/2;
            int eY = tank.getY() + (Tank.HEIGHT - Explode.HEIGHT)/2;
            tf.explodes.add(tf.gf.createExplode(eX, eY, tf));
        }
    }

    private void die() {
        this.living = false;
    }
}
