package com.terry.tank.abstractfactory;

import com.terry.tank.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class RectTank extends BaseTank {
    int x, y;
    Dir dir = Dir.DOWN;
    private static final int SPEED = PropertyMgr.getAsInt("tankSpeed");
    public static int WIDTH = ResourceMgr.goodTankU.getWidth(), HEIGHT = ResourceMgr.goodTankU.getHeight();

    private boolean moving = true;
    Group group = Group.BAD;

    TankFrame tf = null;
    private boolean living = true;
    Rectangle rect = new Rectangle();
    private Random random = new Random();

    FireStrategy fs;

    public RectTank(int x, int y, Dir dir, Group group, TankFrame tf) {
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

    @Override
    public void paint(Graphics g) {
        if (!living) tf.tanks.remove(this);

        Color c = g.getColor();
        g.setColor(group == Group.GOOD ? Color.RED : Color.BLUE);
        g.fillRect(x, y, 40, 40);
        g.setColor(c);

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
        if (this.x > TankFrame.GAME_WIDTH - RectTank.WIDTH -2) {
            x = TankFrame.GAME_WIDTH - RectTank.WIDTH -2;
        }
        if (this.y > TankFrame.GAME_HEIGHT - RectTank.HEIGHT -2) {
            y = TankFrame.GAME_HEIGHT - RectTank.HEIGHT -2;
        }
    }

    public void fire() {
        int bX = this.x + (Tank.WIDTH - Bullet.WIDTH)/2;
        int bY = this.y + (Tank.HEIGHT - Bullet.HEIGHT)/2;
        Dir[] dirs = Dir.values();
        for (Dir dir : dirs) {
            tf.gf.createBullet(bX, bY, dir, group, tf);
        }
        if (group == Group.GOOD) {
//            new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
        }
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
