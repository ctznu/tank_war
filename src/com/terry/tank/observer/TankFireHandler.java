package com.terry.tank.observer;

import com.terry.tank.Tank;

public class TankFireHandler implements TankFireObserver {

    @Override
    public void actionOnFire(TankFireEvent event) {
        Tank t = event.getSource();
        t.fire();
    }
}
