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
            /*tankL = ImageIO.read(classLoader.getResourceAsStream("images/tankL.gif"));
            tankR = ImageIO.read(classLoader.getResourceAsStream("images/tankR.gif"));
            tankU = ImageIO.read(classLoader.getResourceAsStream("images/tankU.gif"));
            tankD = ImageIO.read(classLoader.getResourceAsStream("images/tankD.gif"));

            bulletL = ImageIO.read(classLoader.getResourceAsStream("images/bulletL.gif"));
            bulletR = ImageIO.read(classLoader.getResourceAsStream("images/bulletR.gif"));
            bulletU = ImageIO.read(classLoader.getResourceAsStream("images/bulletU.gif"));
            bulletD = ImageIO.read(classLoader.getResourceAsStream("images/bulletD.gif"));*/
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
