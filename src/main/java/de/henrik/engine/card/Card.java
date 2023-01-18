package de.henrik.engine.card;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.game.Game;
import de.henrik.engine.base.GameImage;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

abstract public class Card extends GameComponent {
    public static final int ARC_SIZE = 15;
    public final int ID;
    private GameImage frontOfCard;
    private GameImage backOfCard;

    boolean paintFront;

    /**
     * Creates a new Card.
     * @param ID the CardID
     * @param frontOfCard the image for the front of this Card
     * @param backOfCard
     * @param x
     * @param y
     * @param width
     * @param height
     * @param paintFront
     */
    public Card(int ID, GameImage frontOfCard, GameImage backOfCard, int x, int y, int width, int height, boolean paintFront) {
        super(x, y, width, height);
        if (frontOfCard == null || backOfCard == null)
            throw new IllegalArgumentException();
        this.ID = ID;

        this.backOfCard = backOfCard;
        this.frontOfCard = frontOfCard;
        this.paintFront = paintFront;
    }

    public Card(int ID, GameImage frontOfCard, GameImage backOfCard, int x, int y, int width, int height) {
        this(ID, frontOfCard, backOfCard, x, y, width, height, false);
    }

    public Card(int ID, GameImage frontOfCard, GameImage backOfCard) {
        this(ID, frontOfCard, backOfCard, 0, 0, 0, 0, false);
    }

    public int getID() {
        return ID;
    }

    /**
     * Tests if another Object is equals to this card.
     * This test for ID and not for a unique Object.
     * @param o the other Card
     * @return {@code TRUE} if the other Object is a card and has the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return ID == card.ID;
    }

    /**
     * Always paints the whole card
     *
     * @param g the Graphics
     */
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
            g.drawImage(frontOfCard.getImage(), x, y);
        } else
            g.drawImage(backOfCard.getImage(), x, y);

        g.setColor(Color.BLACK);
        g.getGraphics().drawRoundRect(x, y, w, h, ARC_SIZE, ARC_SIZE); //paint border
    }

    public void setPaintFront(boolean paintFront) {
        this.paintFront = paintFront;
    }

    @Override
    public void setSize(int width, int height) {
        backOfCard = backOfCard.getScaledInstance(width - 1, height - 1);
        frontOfCard = frontOfCard.getScaledInstance(width - 1, height - 1);
        super.setSize(width, height);
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
