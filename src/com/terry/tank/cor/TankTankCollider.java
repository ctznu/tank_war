package com.terry.tank.cor;

import com.terry.tank.*;

import java.util.Random;

public class TankTankCollider implements Collider {

    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Tank && o2 instanceof Tank) {
            Tank t1 = (Tank) o1;
            Tank t2 = (Tank) o2;
            if (t1.getRect().intersects(t2.getRect())) {
                /*System.out.println("new Tank!");
                if (t1.gm.size() <= 20) {
                    t1.gm.add(new Tank(new Random().nextInt(1000), new Random().nextInt(1000), Dir.DOWN, Group.BAD, t1.gm));
                }
                t1.dir = Dir.LEFT;
                t2.dir = Dir.RIGHT;*/
                if (t1.group != t2.group) {
                    t1.die();
                    t2.die();
                    GameModel.getInstance().explode(t1.getX(), t1.getY());
                    GameModel.getInstance().explode(t2.getX(), t2.getY());
                } else {
                    t1.back();
                    t2.back();
                }
            }
        }
        return true;
    }
}
