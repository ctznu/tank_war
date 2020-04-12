package com.terry.tank;

import com.terry.tank.net.Client;
import com.terry.tank.net.TankDirChangedMsg;
import com.terry.tank.net.TankStartMovingMsg;
import com.terry.tank.net.TankStopMsg;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

import static com.terry.tank.Dir.*;

public class TankFrame extends Frame {

    public static TankFrame INSTANCE = new TankFrame();

    Random r = new Random();

//    Tank myTank = new Tank(r.nextInt(GAME_WIDTH), r.nextInt(GAME_HEIGHT), Dir.DOWN, Group.GOOD, this);
    Tank myTank = new Tank(r.nextInt(1080), r.nextInt(720), Dir.DOWN, Group.GOOD, this);
    List<Bullet> bullets = new ArrayList<>();
    Map<UUID, Tank> tanks = new HashMap<>();
    List<Explode> explodes = new ArrayList<>();


    static final int GAME_WIDTH = PropertyMgr.getAsInt("gameWidth");
    static final int GAME_HEIGHT = PropertyMgr.getAsInt("gameHeight");

    private TankFrame() {
        setSize(1080, 720);
        setResizable(false);
        setTitle("Tank War");
//        setVisible(true);

        addKeyListener(new MyKeyListener());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // 解决闪烁
    Image offScreenImage = null;
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {

        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("子弹的数量：" + bullets.size(), 10, 60);
        g.drawString("敌人的数量：" + tanks.size(), 10, 80);
        g.drawString("爆炸的数量：" + explodes.size(), 10, 100);
        g.setColor(c);
        myTank.paint(g);
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }
//        bullets.values().stream().forEach(e->e.paint(g));
        // java8 stream api
        tanks.values().stream().forEach((e)->e.paint(g));

        for (int i = 0; i < explodes.size(); i++) {
            explodes.get(i).paint(g);
        }
        for (int i = 0; i < bullets.size(); i++) {
            for (Tank tank : tanks.values()) {
                bullets.get(i).collideWith(tank);
            }
        }

    }

    public Tank getMainTank() {
        return myTank;
    }

    public Tank findTankByUUID(UUID id) {
        return tanks.get(id);
    }

    public Bullet findBulletByUUID(UUID id) {
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getId().equals(id)) {
                return bullets.get(i);
            }
        }
        return null;
    }

    public void addTank(Tank t) {
        tanks.put(t.getId(), t);
    }

    public void addBullet(Bullet b) {
        bullets.add(b);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    class MyKeyListener extends KeyAdapter {
        boolean bL = false;
        boolean bU = false;
        boolean bR = false;
        boolean bD = false;

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    bL = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    break;
                case KeyEvent.VK_UP:
                    bU = true;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = true;
                    break;
                default:
                    break;
            }
            setMainTankDir();
            new Thread(()->new Audio("audio/tank_move.wav").play()).start();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    bL = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = false;
                    break;
                case KeyEvent.VK_UP:
                    bU = false;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = false;
                    break;
                case KeyEvent.VK_F:
                    myTank.fire();
                    break;
                default:
                    break;
            }
            setMainTankDir();
        }

        private void setMainTankDir() {
            // save prev dir
            Dir dir = myTank.getDir();

            if (!bL && !bU && !bR && !bD) {
                myTank.setMoving(false);

                // send tank stop msg
                Client.INSTANCE.send(new TankStopMsg(getMainTank()));
            } else {

                if (bL) myTank.setDir(LEFT);
                if (bU) myTank.setDir(UP);
                if (bR) myTank.setDir(RIGHT);
                if (bD) myTank.setDir(DOWN);

                // send tank moving msg
                if (!myTank.isMoving()) {
                    Client.INSTANCE.send(new TankStartMovingMsg(getMainTank()));
                }
                myTank.setMoving(true);
                if (dir != myTank.getDir()) {
                    Client.INSTANCE.send(new TankDirChangedMsg(myTank));
                }
            }
        }
    }
}
