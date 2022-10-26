package de.henrik.implementation;

import de.henrik.engine.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class BasicCard extends Card {

    static BufferedImage frontOfCard, backOfCard;

    public BasicCard(int type) {
        super(loadImage(type),backOfCard);
    }

    private static Image loadImage(int type) {
        try {
            backOfCard = ImageIO.read(Objects.requireNonNull(BasicCard.class.getResourceAsStream("/cards/c1_bg.png")));
            frontOfCard = ImageIO.read(Objects.requireNonNull(BasicCard.class.getResource("/cards/c1_" + type + ".png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return frontOfCard;
    }


}
