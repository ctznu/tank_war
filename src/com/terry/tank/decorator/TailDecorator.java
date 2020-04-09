package com.terry.tank.decorator;

import com.terry.tank.GameObject;

import java.awt.*;

public class TailDecorator extends GODecorator {

    public TailDecorator(GameObject go) {
        super(go);
    }

    @Override
    public void paint(Graphics g) {
        this.x = go.x;
        this.y = go.y;
        go.paint(g);

        Color c = g.getColor();
        g.setColor(Color.CYAN);
        g.drawLine(go.x + go.getWidth() / 2, go.y, go.x + go.getWidth() / 2, go.y + go.getHeigth());
        g.setColor(c);
    }

    @Override
    public int getWidth() {
        return go.getWidth();
    }

    @Override
    public int getHeigth() {
        return go.getHeigth();
    }
}
