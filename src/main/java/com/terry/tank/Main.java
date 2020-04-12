package com.terry.tank;

import com.terry.tank.net.Client;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        TankFrame tankFrame = TankFrame.INSTANCE;
        tankFrame.setVisible(true);

        /*int initTankCount = PropertyMgr.getAsInt("initTankCount");
        // init enemy tank
        for (int i = 0; i < initTankCount; i++) {
            tankFrame.tanks.add(new Tank(50 + i * 80, 200, Dir.DOWN, Group.BAD, tankFrame));
        }*/

        new Thread(() -> new Audio("audio/war1.wav").loop()).start();

        new Thread(()->{

            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                tankFrame.setVisible(true);
                tankFrame.repaint();
            }
        }).start();

        Client c = new Client();
        c.connect();
    }
}
