package de.henrik.engine.util;

import de.henrik.implementation.game.Options;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class GameImage {

    /**
     * Returns the image with the given path, if it is in the resource folder. otherwise returns a green Image with the Path name on it.
     * @param path the path to the image
     * @return the image
     */
    public static BufferedImage getImage(String path) {
        try {
            return ImageIO.read(new File(Objects.requireNonNull(GameImage.class.getResource(path)).toURI()));
        } catch (NullPointerException | URISyntaxException | IOException e) {
            System.err.println("Warning! Could not find image " + path);
            var i = new BufferedImage(Options.getWidth(), Options.getHeight(), BufferedImage.TYPE_INT_RGB);
            var ig = i.getGraphics();
            ig.setColor(Color.LIGHT_GRAY);
            ig.fillRect(0, 0, i.getWidth(), i.getHeight());
            ig.setColor(Color.BLACK);
            ig.setFont(new Font("serif", Font.PLAIN, 200));
            ig.drawString("image not found ",200,200);
            ig.drawString(path,200,400);

            return i;
        }
    }
}
