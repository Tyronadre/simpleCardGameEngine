package de.henrik.engine.card;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.game.Game;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

abstract public class Card extends GameComponent {
    public static final int ARC_SIZE = 15;
    public final int ID;

    Image frontOfCardTemp, backOfCardTemp;
    private final Image frontOfCard;
    private final Image backOfCard;

    boolean paintFront;

    /**
     * CONSTRUCTOR
     */
    public Card(int ID, Image frontOfCard, Image backOfCard, int x, int y, int width, int height, boolean paintFront) {
        super(x, y, width, height);
        if (frontOfCard == null || backOfCard == null)
            throw new IllegalArgumentException();
        this.ID = ID;

        this.backOfCard = backOfCard;
        this.frontOfCard = frontOfCard;
        this.paintFront = paintFront;
    }

    public Card(int ID, Image frontOfCard, Image backOfCard, int x, int y, int width, int height) {
        this(ID, frontOfCard, backOfCard, x, y, width, height, false);
    }

    public Card(int ID, Image frontOfCard, Image backOfCard) {
        this(ID, frontOfCard, backOfCard, 0, 0, 0, 0, false);
    }

    public int getID() {
        return ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return ID == card.ID;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    @Override
    public void paint(GameGraphics g) {
        if (!Game.isRunning())
            return;

        int x = getX() + 1;
        int y = getY() + 1;
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        g.setColor(Color.DARK_GRAY);

        g.getGraphics().setClip(new RoundRectangle2D.Float(x, y, w, h, ARC_SIZE, ARC_SIZE));
        if (paintFront) {
                g.drawImage(frontOfCardTemp, x, y);
        } else
            g.drawImage(backOfCardTemp, x, y);

        g.setColor(Color.BLACK);
        g.getGraphics().drawRoundRect(x, y, w, h, ARC_SIZE, ARC_SIZE); //paint border
        g.dispose();
    }

    public void setPaintFront(boolean paintFront) {
        this.paintFront = paintFront;
    }

    @Override
    public void setSize(int width, int height) {
        backOfCardTemp = backOfCard.getScaledInstance(width - 1, height - 1, Image.SCALE_SMOOTH);
        frontOfCardTemp = frontOfCard.getScaledInstance(width - 1, height - 1, Image.SCALE_SMOOTH);
        super.setSize(width, height);
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
    }

    @Override
    public String toString() {
        return "Card{" +
                "ID=" + ID +
                ", paintFront=" + paintFront +
                ", pos=" + getPosition() +
                ", size=" + getSize() +
                '}';
    }

}
