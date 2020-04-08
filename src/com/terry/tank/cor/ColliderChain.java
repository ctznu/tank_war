package com.terry.tank.cor;

import com.terry.tank.GameObject;
import com.terry.tank.PropertyMgr;
import com.terry.tank.ResourceMgr;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class ColliderChain implements Collider{
    private List<Collider> colliders = new LinkedList<>();

    public ColliderChain() {
//        add(new BulletTankCollider());
//        add(new TanksCollider());
        String colliders = PropertyMgr.getAsString("colliders");
        try {
            String[] colliderClazz = colliders.split(",");
            for (String s : colliderClazz) {
                add((Collider) Class.forName(s).getDeclaredConstructor().newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void add(Collider c) {
        colliders.add(c);
    }

    public boolean collide(GameObject o1, GameObject o2) {
        for (int i = 0; i < colliders.size(); i++) {
            if (!colliders.get(i).collide(o1, o2)) {
                return false;
            }
        }
        return true;
    }
}
