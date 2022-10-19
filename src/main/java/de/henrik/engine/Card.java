package de.henrik.engine;

import de.henrik.engine.base.Component;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

abstract public class Card extends Component {
    public static final int ARC_SIZE = 15;
    private static int IDCount = 0;
    public final int ID;

    Image frontOfCardTemp, backOfCardTemp;
    private Image frontOfCard, backOfCard;

    boolean paintFront;

    /**
     * CONSTRUCTOR
     */
    public Card(Image frontOfCard, Image backOfCard, int x, int y, int width, int height, boolean paintFront) {
        super(x,y,width,height);
        if (frontOfCard == null || backOfCard == null)
            throw new IllegalArgumentException();

        this.ID = IDCount++;

        this.backOfCard = backOfCard;
        this.frontOfCard = frontOfCard;
        this.paintFront = paintFront;
    }

    public Card(Image frontOfCard, Image backOfCard, int x, int y, int width, int height) {
        this(frontOfCard, backOfCard, x, y, width, height, false);
    }

    public Card(Image frontOfCard, Image backOfCard) {
        this(frontOfCard, backOfCard, 0, 0, 0, 0, false);
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
    public void paint() {

        if (g == null)
            return;
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = getX() + 1;
        int y = getY() + 1;
        int w = getWidth() - 1;
        int h = getHeight() - 1;
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawRoundRect(x-1, y-1, w, h, ARC_SIZE, ARC_SIZE);

        graphics.setClip(new RoundRectangle2D.Float(x, y, w, h, ARC_SIZE, ARC_SIZE));
        if (paintFront) {
            if (frontOfCard != null)
                graphics.drawImage(frontOfCardTemp, x, y, null);
            else {
                graphics.drawString("Image not found", x, y);
                graphics.fillRect(x, y, w, h);
            }
        } else if (backOfCard != null)
            graphics.drawImage(backOfCardTemp, x, y, null);
        else {
            graphics.drawString("Image not found", x, y);
            graphics.fillRect(x, y, w, h);
        }


        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(x, y, w, h, ARC_SIZE, ARC_SIZE); //paint border

    }

    public void setPaintFront(boolean paintFront) {
        this.paintFront = paintFront;
    }

    @Override
    public void setSize(int width, int height) {
        backOfCardTemp = backOfCard.getScaledInstance(width - 1, height- 1, Image.SCALE_SMOOTH);
        frontOfCardTemp = frontOfCard.getScaledInstance(width- 1, height- 1, Image.SCALE_SMOOTH);
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
