package com.terry.tank;

import com.terry.tank.cor.ColliderChain;

import java.awt.*;
import java.io.*;
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

    private int tankCount, bulletCount;

    public static GameModel getInstance() {
        return INSTANCE;
    }

    private GameModel() {}

    private void init() {
        // init main tank
        myTank = new Tank(500, 800, Dir.UP, Group.GOOD);

        // init enemy tank
        int initTankCount = PropertyMgr.getAsInt("initTankCount");
        for (int i = 0; i < initTankCount; i++) {
            new Tank(50 + i * 100, 200, Dir.DOWN, Group.BAD);
        }

        // init wall
        add(new Wall(150, 150, 200, 50));
        add(new Wall(700, 150, 200, 50));
        add(new Wall(300, 300, 50, 200));
        add(new Wall(700, 300, 50, 200));
    }

    public void add(GameObject go) {
        if (go instanceof Tank && ((Tank) go).group == Group.BAD) tankCount++;
        if (go instanceof Bullet) bulletCount++;
        this.objects.add(go);
    }

    public void remove(GameObject go) {
        if (go instanceof Tank) tankCount--;
        if (go instanceof Bullet) bulletCount--;
        this.objects.remove(go);
    }

    public int size() {
        return objects.size();
    }

    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("子弹的数量：" + bulletCount, 10, 60);
        g.drawString("敌人的数量：" + tankCount, 10, 80);
//        g.drawString("爆炸的数量：" + tanks.size(), 10, 100);
        g.setColor(c);

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).paint(g);
        }

        // collide check
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                GameObject o1 = objects.get(i);
                GameObject o2 = objects.get(j);
                chain.collide(o1, o2);
            }
        }
        if (!myTank.living) {
            gameOver(g);
        }
    }

    public Tank getMainTank() {
        return myTank;
    }

    public void gameOver(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.ORANGE);
        g.setFont( new Font("TimesRoman" ,Font.PLAIN,40));
        g.drawString("GAME OVER", 400, 400);
        g.setColor(c);
    }

    public void explode(int x, int y) {
        int eX = x + (Tank.WIDTH - Explode.WIDTH)/2;
        int eY = y + (Tank.HEIGHT - Explode.HEIGHT)/2;
        add(new Explode(eX, eY));
    }

    public void save() {
        File f = new File("D:/IT/Java/projects/tank/tank.data");
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(myTank);
            oos.writeObject(objects);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load() {
        File f = new File("D:/IT/Java/projects/tank/tank.data");
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(f));
            myTank = (Tank) ois.readObject();
            objects = (List) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
