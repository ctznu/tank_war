package com.terry.tank;

import com.terry.tank.strategy.FireStrategy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class Tank extends GameObject {
    public int x, y, preX, preY;
    public Dir dir = Dir.DOWN;
    private static final int SPEED = PropertyMgr.getAsInt("tankSpeed");
    public static int WIDTH = ResourceMgr.goodTankU.getWidth(), HEIGHT = ResourceMgr.goodTankU.getHeight();

    private boolean moving = true;
    public Group group = Group.BAD;


    private boolean living = true;
    public Rectangle rect = new Rectangle();

    public Rectangle getRect() {
        return rect;
    }

    private Random random = new Random();

    FireStrategy fs;

    public Tank(int x, int y, Dir dir, Group group) {
        super();
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
        String fsp;
        if (group == Group.GOOD) {
            fsp = PropertyMgr.getAsString("goodFs");
        } else {
            fsp = PropertyMgr.getAsString("badFs");
        }
        try {
            fs = (FireStrategy) Class.forName(fsp).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        GameModel.getInstance().add(this);
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
        if (!living) {
            GameModel.getInstance().remove(this);
            if (this.group == Group.GOOD) {
                System.out.println("game over");
                GameModel.getInstance().gameOver(g);
                return;
            }
        }
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
        if (!moving) return;
        preX = x;
        preY = y;
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
        if (this.group == Group.BAD && random.nextInt(50) > 48) {
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
        if (this.x > TankFrame.GAME_WIDTH - Tank.WIDTH - 2) {
            x = TankFrame.GAME_WIDTH - Tank.WIDTH - 2;
        }
        if (this.y > TankFrame.GAME_HEIGHT - Tank.HEIGHT - 2) {
            y = TankFrame.GAME_HEIGHT - Tank.HEIGHT - 2;
        }
    }

    public void fire() {
        fs.fire(this);
    }

    public void die() {
        this.living = false;
    }

    private void randomDir() {
        int r = random.nextInt(4);
        this.dir = Dir.values()[r];
    }

    public void back() {
        x = preX;
        y = preY;
    }
}
