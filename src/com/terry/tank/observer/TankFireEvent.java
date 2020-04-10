package com.terry.tank.observer;

import com.terry.tank.Tank;

public class TankFireEvent {
    Tank tank;

    public Tank getSource() {
        return tank;
    }

    public TankFireEvent(Tank tank) {
        this.tank = tank;
    }
}
