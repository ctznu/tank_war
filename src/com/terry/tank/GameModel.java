package com.terry.tank;

import com.terry.tank.cor.ColliderChain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private static final GameModel INSTANCE = new GameModel();

    static {
        INSTANCE.init();
    }
    Tank myTank;

    ColliderChain chain = new ColliderChain();

    private List<GameObject> objects = new ArrayList<>();

    public static GameModel getInstance() {
        return INSTANCE;
    }

    private GameModel() {}

    private void init() {
        // init main tank
        myTank = new Tank(500, 600, Dir.DOWN, Group.GOOD);

        // init enemy tank
        int initTankCount = PropertyMgr.getAsInt("initTankCount");
        for (int i = 0; i < initTankCount; i++) {
            new Tank(50 + i * 100, 200, Dir.DOWN, Group.BAD);
        }

        // init wall
        add(new Wall(150, 150, 200, 50));
        add(new Wall(600, 150, 200, 50));
        add(new Wall(300, 300, 50, 200));
        add(new Wall(600, 300, 50, 200));
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

        // collide check
        for (int i = 0; i < objects.size(); i++) {
//            chain.collide(myTank, objects.get(i));
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

    public void explode(int x, int y) {
        int eX = x + (Tank.WIDTH - Explode.WIDTH)/2;
        int eY = y + (Tank.HEIGHT - Explode.HEIGHT)/2;
        add(new Explode(eX, eY));
    }

}
