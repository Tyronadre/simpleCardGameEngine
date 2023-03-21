package de.henrik.implementation.card.stack;

import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.card.CardStack;

import java.awt.*;

public class EmptyCardStack extends CardStack {
    private final Image image;
    private Image scaledImage;

    public EmptyCardStack(String name, Image image) {
        super(name, RP_ALL_CARDS_TURNED, e -> false, 0);
        this.image = image;
    }

    @Override
    public void setCardSize(int width, int height) {
        setWidth(width);
        setHeight(height);
        if (image != null)
            scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    @Override
    public void paint(GameGraphics g) {
        g.drawImage(scaledImage, getX(), getY());
    }

    @Override
    public int getWidth() {
        return getCardSize().width;
    }

    @Override
    public int getHeight() {
        return getCardSize().height;
    }
}
