package de.henrik.engine.util;

import com.sun.tools.javac.Main;
import de.henrik.implementation.game.Options;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.util.Objects;

public class GameImage{
    Image image;

    /**
     * Creates the image with the given path, if it is in the resource folder. otherwise returns a green Image with the Path name on it.
     *
     * @param path the path to the image
     */
    public GameImage(String path) {
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            if (image.getHeight(null) <= 0 || image.getWidth(null) <= 0)
                throw new NullPointerException();
            System.out.println("Loaded image: " + path);
        } catch (NullPointerException e) {
            System.err.println("Warning! Could not find image " + path);
            var i = new BufferedImage(200, 300, BufferedImage.TYPE_INT_RGB);
            var ig = i.getGraphics();
            ig.setColor(Color.LIGHT_GRAY);
            ig.fillRect(0, 0, i.getWidth(), i.getHeight());
            ig.setColor(Color.BLACK);
//            ig.setFont(new Font("serif", Font.PLAIN, 10));
            ig.drawString("image not found ", 10, 50);
            ig.drawString(path, 10, 100);

            image = i;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GameImage(Image image) {
        this.image = image;
    }


    public int getWidth() {
        return image.getWidth(null);
    }


    public int getHeight() {
        return image.getHeight(null);
    }

    public Image getImage () {
        return image;
    }


    public GameImage getScaledInstance(int width, int height, int hints) {
        return new GameImage(image.getScaledInstance(width, height, hints));
    }
}
