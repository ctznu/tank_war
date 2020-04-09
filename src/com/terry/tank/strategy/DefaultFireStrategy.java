package com.terry.tank.strategy;

import com.terry.tank.Audio;
import com.terry.tank.Bullet;
import com.terry.tank.Group;
import com.terry.tank.Tank;

public class DefaultFireStrategy implements FireStrategy {
    @Override
    public void fire(Tank t) {
        int bX = t.x + (Tank.WIDTH - Bullet.WIDTH)/2;
        int bY = t.y + (Tank.HEIGHT - Bullet.HEIGHT)/2;
        new Bullet(bX, bY, t.dir, t.group);
        if (t.group == Group.GOOD) {
//            new Thread(()->new Audio("audio/tank_fire.wav").play()).start();
        }
    }
}
