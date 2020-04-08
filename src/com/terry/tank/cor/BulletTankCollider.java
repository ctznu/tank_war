package com.terry.tank.cor;

import com.terry.tank.Bullet;
import com.terry.tank.Explode;
import com.terry.tank.GameObject;
import com.terry.tank.Tank;

import java.awt.*;

public class BulletTankCollider implements Collider {

    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Bullet && o2 instanceof Tank) {
            Bullet b = (Bullet) o1;
            Tank t = (Tank) o2;
            if (collideWith(b, t)) {
                return false;
            }
        } else if (o1 instanceof Tank && o2 instanceof Bullet) {
            return collide(o2, o1);
        }
        return false;
    }

    private boolean collideWith(Bullet bullet, Tank tank) {
        if (bullet.group == tank.getGroup()) return false;

        // TODO: 用一个rect来记录子弹的位置
        Rectangle rect1 = new Rectangle(bullet.x, bullet.y, bullet.WIDTH, bullet.HEIGHT);
        Rectangle rect2 = new Rectangle(tank.getX(), tank.getY(), Tank.WIDTH, Tank.HEIGHT);
        if (rect1.intersects(rect2)) {
            tank.die();
            bullet.die();
            int eX = tank.getX() + (Tank.WIDTH - Explode.WIDTH)/2;
            int eY = tank.getY() + (Tank.HEIGHT - Explode.HEIGHT)/2;
            tank.gm.add(new Explode(eX, eY, tank.gm));
            return true;
        }
        return false;
    }
}
