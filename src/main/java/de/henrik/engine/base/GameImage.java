package de.henrik.engine.base;

import de.henrik.implementation.game.Options;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class GameImage {
    private static final HashMap<String, HashMap<Dimension, BufferedImage>> loadedImages = new HashMap<>();
    /**
     * This is not the actual size, but we need to be able to identify the default Image
     */
    private static final Dimension defaultImageDim = new Dimension(-1, -1);
    private static final GraphicsConfiguration GFX_CONFIG = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    BufferedImage image;
    String path;


    /**
     * Creates the image with the given path, if it is in the resource folder. otherwise returns a green Image with the Path name on it.
     *
     * @param path the path to the image
     */
    public GameImage(String path) {
        this.path = path;
        if (loadedImages.containsKey(path)) {
            image = loadedImages.get(path).get(defaultImageDim);
            return;
        }
        try {
            //LOAD NEW IMG//
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            if (image.getHeight(null) <= 0 || image.getWidth(null) <= 0) throw new NullPointerException();
            BufferedImage new_image = GFX_CONFIG.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
            Graphics2D g2d = (Graphics2D) new_image.getGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            this.image = new_image;
            System.out.println("Loaded image: " + path);
        } catch (NullPointerException e) {
            System.err.println("Warning! Could not find image " + path);
            image = GFX_CONFIG.createCompatibleImage(Options.getWidth(), Options.getHeight());
            Graphics2D ig = (Graphics2D) image.getGraphics();
            ig.setColor(Color.LIGHT_GRAY);
            ig.fillRect(0, 0, image.getWidth(), image.getHeight());
            ig.setColor(Color.BLACK);
            ig.setFont(new Font("serif", Font.PLAIN, 150));
            ig.drawString("image not found ", 10, 200);
            ig.drawString(path, 10, 400);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //SAVE IMAGE
        loadedImages.put(path, new HashMap<>());
        loadedImages.get(path).put(defaultImageDim, image);
    }

    private GameImage(BufferedImage image, String path) {
        this.image = image;
        this.path = path;
    }

    public GameImage(Color color) {
        this.path = String.valueOf(color);
        if (loadedImages.containsKey(path)) {
            image = loadedImages.get(path).get(new Dimension(Options.getWidth(), Options.getHeight()));
            return;
        }
        image = GFX_CONFIG.createCompatibleImage(Options.getWidth(), Options.getHeight());
        Graphics2D ig = (Graphics2D) image.getGraphics();
        ig.setColor(color);
        ig.fillRect(0, 0, image.getWidth(), image.getHeight());
        loadedImages.put(path, new HashMap<>());
        loadedImages.get(path).put(new Dimension(Options.getWidth(), Options.getHeight()), image);
    }


    public int getWidth() {
        return image.getWidth(null);
    }


    public int getHeight() {
        return image.getHeight(null);
    }

    public Image getImage() {
        return image;
    }


    public GameImage getScaledInstance(int width, int height) {
        if (loadedImages.get(path).containsKey(new Dimension(width, height)))
            return new GameImage(loadedImages.get(path).get(new Dimension(width, height)), this.path);


        BufferedImage new_image = GFX_CONFIG.createCompatibleImage(width, height, image.getTransparency());
        Graphics2D g2d = new_image.createGraphics();
        g2d.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        loadedImages.get(path).put(new Dimension(width, height), new_image);
        return new GameImage(new_image, this.path);
    }
}
