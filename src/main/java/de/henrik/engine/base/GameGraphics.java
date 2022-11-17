package de.henrik.engine.base;

import java.awt.*;

public class GameGraphics {
    Graphics2D graphics2D;

    public GameGraphics(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
    }

    public GameGraphics setClip(Rectangle clip){
        graphics2D.setClip(clip);
        return this;
    }

    public GameGraphics create() {
        graphics2D.create();
        graphics2D.setClip(graphics2D.getClip());

        return this;
    }

    public Graphics2D getGraphics() {
        return graphics2D;
    }

    public Shape getClip() {
        return graphics2D.getClip();
    }

    public GameGraphics setClip(int x, int y, int width, int height) {
        graphics2D.setClip(x, y, width, height);
        return this;
    }

    public void dispose() {
        graphics2D.dispose();
    }

    public void setColor(Color color) {
        graphics2D.setColor(color);
    }

    public void drawImage(Image image, int x, int y) {
        graphics2D.drawImage(image,x,y,null);
    }

    public void drawString(String string, int x, int y) {
        graphics2D.drawString(string,x,y);
    }

}
