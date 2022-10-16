package de.henrik.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

abstract public class Card {
    public static final int ARC_SIZE = 15;
    private static int IDCount = 0;
    public final int ID;

    private Image frontOfCard, backOfCard;
    private Dimension size;
    private Point pos;

    /**
     * Generic Card constructor with a custom JPanel
     */
    public Card(Image frontOfCard, Image backOfCard) {
        if (frontOfCard == null || backOfCard == null)
            throw new IllegalArgumentException();

        this.ID = IDCount++;

        this.backOfCard = backOfCard;
        this.frontOfCard = frontOfCard;
        this.size = new Dimension(100, 100);
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

    public void paint(Graphics g, boolean front, Point pos) {
        System.out.println("paintCard");
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRoundRect(pos.x - 1, pos.y - 1, getWidth(), getHeight(), ARC_SIZE, ARC_SIZE);

        graphics.setClip(new RoundRectangle2D.Float(pos.x, pos.y, getWidth(), getHeight(), ARC_SIZE, ARC_SIZE));
        if (front) {
            if (frontOfCard != null)
                graphics.drawImage(frontOfCard, pos.x, pos.y, null);
            else {
                graphics.drawString("Image not found", pos.x, pos.y);
                graphics.fillRect(pos.x, pos.y, getWidth(), getHeight());
            }
        } else if (backOfCard != null)
            graphics.drawImage(backOfCard, pos.x, pos.y, null);
        else {
            graphics.drawString("Image not found", pos.x, pos.y);
            graphics.fillRect(pos.x, pos.y, getWidth(), getHeight());
        }


        graphics.setColor(Color.BLACK);
        graphics.drawRoundRect(pos.x, pos.y, getWidth(), getHeight(), ARC_SIZE, ARC_SIZE); //paint border
    }

    public int getWidth() {
        return size.width;
    }

    public int getHeight() {
        return size.height;
    }


    public void setSize(Dimension d) {
        size = d;
        frontOfCard = frontOfCard.getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH);
        backOfCard = backOfCard.getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH);
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getPos() {
        return pos;
    }
}
