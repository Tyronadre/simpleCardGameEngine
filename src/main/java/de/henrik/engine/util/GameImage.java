package de.henrik.engine.util;

import de.henrik.implementation.game.Options;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.util.Objects;

public class GameImage{
    BufferedImage image;

    /**
     * Creates the image with the given path, if it is in the resource folder. otherwise returns a green Image with the Path name on it.
     *
     * @param path the path to the image
     */
    public GameImage(String path) {
        try {
            image = ImageIO.read(new File(Objects.requireNonNull(GameImage.class.getResource(path)).toURI()));
        } catch (NullPointerException | URISyntaxException | IOException e) {
            System.err.println("Warning! Could not find image " + path);
            var i = new BufferedImage(Options.getWidth(), Options.getHeight(), BufferedImage.TYPE_INT_RGB);
            var ig = i.getGraphics();
            ig.setColor(Color.LIGHT_GRAY);
            ig.fillRect(0, 0, i.getWidth(), i.getHeight());
            ig.setColor(Color.BLACK);
            ig.setFont(new Font("serif", Font.PLAIN, 200));
            ig.drawString("image not found ", 200, 200);
            ig.drawString(path, 200, 400);

            image = i;
        }
    }

    public GameImage(BufferedImage image) {
        this.image = image;
    }


    public int getWidth() {
        return image.getWidth();
    }


    public int getHeight() {
        return image.getHeight();
    }

    public BufferedImage getImage () {
        return image;
    }


    public GameImage getScaledInstance(int width, int height, int hints) {
        Image toolkitImage = image.getScaledInstance(width, height, hints);
        int scaledWidth = toolkitImage.getWidth(null);
        int scaledHeight = toolkitImage.getHeight(null);
        BufferedImage newImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(toolkitImage, 0, 0, null);
        return new GameImage(newImage);
    }
}
