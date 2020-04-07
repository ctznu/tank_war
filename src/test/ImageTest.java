package test;

import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class ImageTest {

    @Test
    public void test() {
        try {
            BufferedImage image = ImageIO.read(new File("D:/IT/Java/projects/tank/src/images/th.jfif"));
            Assert.assertNotNull(image);
            BufferedImage image1 = ImageIO.read(ImageTest.class.getClassLoader().getResourceAsStream("images/th.jfif"));
            Assert.assertNotNull(image1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRandom() {
        Random r = new Random();
        System.out.println(r.nextInt(4));
    }

}
