package de.henrik;

import de.henrik.engine.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class GameImage {
    public static BufferedImage getImage(String path) {
        System.out.println();
        try {
            return ImageIO.read(new File(Objects.requireNonNull(GameImage.class.getResource(path)).toURI()));
        } catch (NullPointerException | URISyntaxException | IOException e) {
            System.out.println("Warning! Could not find image " + path);
            return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        }
    }
}
