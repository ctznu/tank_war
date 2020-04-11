package com.terry.tank.strategy;

import com.terry.tank.Tank;

import java.io.Serializable;

public interface FireStrategy extends Serializable {
    void fire(Tank tank);
}
