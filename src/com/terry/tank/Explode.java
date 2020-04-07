package com.terry.tank;

import com.terry.tank.abstractfactory.BaseBullet;
import com.terry.tank.abstractfactory.BaseExplode;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Explode extends BaseExplode {

    public static int WIDTH = ResourceMgr.explodes[0].getWidth(), HEIGHT = ResourceMgr.explodes[0].getHeight();
    private int x, y;

    private boolean living = true;
    TankFrame tf = null;

    private int step = 0;

    public Explode(int x, int y, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.tf = tf;
//        new Thread(()->new Audio("audio/explode.wav").play()).start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.explodes[step++], x, y, null);
        if (step >= ResourceMgr.explodes.length) {
            tf.explodes.remove(this);
        }

    }

}
