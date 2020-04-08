package com.terry.tank.cor;

import com.terry.tank.GameObject;

public interface Collider {
    boolean collide(GameObject o1, GameObject o2);
}
