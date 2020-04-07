package com.terry.tank;

public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Tank t) {
        int bX = t.x + (Tank.WIDTH - Bullet.WIDTH)/2;
        int bY = t.y + (Tank.HEIGHT - Bullet.HEIGHT)/2;
        new Bullet(bX, bY, t.dir, t.group, t.tf);
        if (t.group == Group.GOOD) {
//            new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
        }
    }
}
