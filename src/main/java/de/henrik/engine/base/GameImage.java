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
    final String path;
    Boolean cropImage;


    /**
     * Creates the image with the given path, if it is in the resource folder. otherwise returns a green Image with the Path name on it.
     *
     * @param path the path to the image
     */
    public GameImage(String path) {
        this.path = path;
        this.cropImage = false;
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

    /**
     * If crop Image is true, the default {@link GameImage#getScaledInstance(int, int)} method will use {@link GameImage#getCroppedInstance(int, int, int, int)} instead, where x and y are 0.
     * This can be used if this image should be cropped instead of scaled in every situation. Note that this will not use the internal dataclass for saving the image, so resizing might be slower.
     * @param path The path to the image
     * @param cropImage if this image should be cropped instead of scaled
     */
    public GameImage(String path, boolean cropImage) {
        this(path);
        this.cropImage = cropImage;
    }

    private GameImage(BufferedImage image, String path, boolean cropImage) {
        this.image = image;
        this.path = path;
        this.cropImage = cropImage;
    }

    public GameImage(Color color) {
        this.path = String.valueOf(color);
        this.cropImage = false;
        if (loadedImages.containsKey(path)) {
            image = loadedImages.get(path).get(new Dimension(Options.getWidth(), Options.getHeight()));
            return;
        }
        image = GFX_CONFIG.createCompatibleImage(Options.getWidth(), Options.getHeight(), Transparency.TRANSLUCENT);
        Graphics2D ig = (Graphics2D) image.getGraphics();
        ig.setColor(color);
        ig.fillRect(0, 0, image.getWidth(), image.getHeight());
        loadedImages.put(path, new HashMap<>());
        loadedImages.get(path).put(defaultImageDim, image);
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
        if (cropImage) {
            return getCroppedInstance(0, 0, width, height);
        }
        if (loadedImages.get(path).containsKey(new Dimension(width, height)))
            return new GameImage(loadedImages.get(path).get(new Dimension(width, height)), this.path, this.cropImage);

        if (width == 0 || height == 0) {
            loadedImages.get(path).put(new Dimension(width, height), null);
            return new GameImage(null, this.path, this.cropImage);
        } else {
            BufferedImage new_image = GFX_CONFIG.createCompatibleImage(width, height, loadedImages.get(path).get(defaultImageDim).getTransparency());
            Graphics2D g2d = new_image.createGraphics();
            g2d.drawImage(loadedImages.get(path).get(defaultImageDim).getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            loadedImages.get(path).put(new Dimension(width, height), new_image);
            return new GameImage(new_image, this.path, this.cropImage);
        }
    }

    public GameImage getCroppedInstance(int x, int y, int width, int height) {
        if (width == 0 || height == 0) {
            throw new IllegalArgumentException("width and height must be greater than 0");
        }
        BufferedImage base = loadedImages.get(path).get(defaultImageDim);
        if (base.getWidth() < x + width || base.getHeight() < y + height) {
            throw new IllegalArgumentException("try to get an area outside of the base image");
        }
        BufferedImage new_image = GFX_CONFIG.createCompatibleImage(width, height, loadedImages.get(path).get(defaultImageDim).getTransparency());
        Graphics2D g2d = new_image.createGraphics();
        g2d.drawImage(base.getSubimage(0, 0, width, height), 0, 0, null);
        return new GameImage(new_image, this.path, this.cropImage);
    }

    public String getPath() {
        return path;
    }
}
