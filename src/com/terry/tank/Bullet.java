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

    public void die() {
        this.living = false;
    }
}
