package com.terry.tank;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ResourceMgr {

    public static BufferedImage goodTankL, goodTankR, goodTankU, goodTankD;
    public static BufferedImage badTankL, badTankR, badTankU, badTankD;
    public static BufferedImage bulletL, bulletR, bulletU, bulletD;
    public static BufferedImage[] explodes = new BufferedImage[16];

    static  {
        ClassLoader classLoader = ResourceMgr.class.getClassLoader();
        try {
            goodTankU = ImageIO.read(classLoader.getResourceAsStream("images/GoodTank1.png"));
            goodTankL = ImageUtil.rotateImage(goodTankU, -90);
            goodTankR = ImageUtil.rotateImage(goodTankU, 90);
            goodTankD = ImageUtil.rotateImage(goodTankU, 180);

            badTankU = ImageIO.read(classLoader.getResourceAsStream("images/BadTank1.png"));
            badTankL = ImageUtil.rotateImage(badTankU, -90);
            badTankR = ImageUtil.rotateImage(badTankU, 90);
            badTankD = ImageUtil.rotateImage(badTankU, 180);

            bulletU = ImageIO.read(classLoader.getResourceAsStream("images/bulletU.png"));
            bulletL = ImageUtil.rotateImage(bulletU, -90);
            bulletR = ImageUtil.rotateImage(bulletU, 90);
            bulletD = ImageUtil.rotateImage(bulletU, 180);

            for (int i = 0; i < 16; i++) {
                explodes[i] = ImageIO.read(classLoader.getResourceAsStream("images/e" + (i + 1) + ".gif"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
