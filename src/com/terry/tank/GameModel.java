package com.terry.tank;

import com.terry.tank.cor.ColliderChain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameModel {

    Tank myTank = new Tank(400, 400, Dir.DOWN, Group.GOOD, this);

    ColliderChain chain = new ColliderChain();

    private List<GameObject> objects = new ArrayList<>();

    public GameModel() {
        int initTankCount = PropertyMgr.getAsInt("initTankCount");
        // init enemy tank
        for (int i = 0; i < initTankCount; i++) {
            add(new Tank(50 + i * 100, 200, Dir.DOWN, Group.BAD, this));
        }
    }

    public void add(GameObject go) {
        this.objects.add(go);
    }

    public void remove(GameObject go) {
        this.objects.remove(go);
    }

    public int size() {
        return objects.size();
    }

    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
//        g.drawString("子弹的数量：" + bullets.size(), 10, 60);
//        g.drawString("敌人的数量：" + tanks.size(), 10, 80);
//        g.drawString("爆炸的数量：" + tanks.size(), 10, 100);
        g.setColor(c);

        myTank.paint(g);
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).paint(g);
        }

        for (int i = 0; i < objects.size(); i++) {
            chain.collide(myTank, objects.get(i));
            for (int j = i + 1; j < objects.size(); j++) {
                GameObject o1 = objects.get(i);
                GameObject o2 = objects.get(j);
                chain.collide(o1, o2);
            }
        }


        /*for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < tanks.size(); j++) {

                bullets.get(i).collideWith(tanks.get(j));
            }
        }*/
    }

    public Tank getMainTank() {
        return myTank;
    }

    public void gameOver(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);

        g.drawString("GAME OVER", TankFrame.GAME_WIDTH/2, TankFrame.GAME_HEIGHT/2);
        g.setColor(c);
    }
}
