package de.henrik.engine;

import de.henrik.engine.base.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * Funktionen:
 * <ul>
 *     <li>Feste Größe</li>
 *     <li>Hintergrundbild definieren</li>
 *     <li>Definieren von Positionen an denen Kartenstapel liegen. Dabei ist Dimension der stapel fest. Die maximale Anzahl der karten kann festgelegt werden.</li>
 *     <li>Rückgabe von Karten die auf kartenstapel liegen</li>
 *     <li>bewegen von karten von einem stapel zum anderen</li>
 *     <li>es muss build() aufgerufen werden um  das GameBoard zu bauen. dann können keine stacks mehr entfernt oder hinzugefügt werden</li>
 * </ul>>
 */
public class GameBoard extends Component {
    private BufferedImage backgroundImage;
    private final List<CardStack> cardStacks;
    private boolean build;

    private boolean cardDragged;
    private Game game = Game.game;


    public GameBoard(BufferedImage backgroundImage, Dimension size) {
        super(0, 0, size.width, size.height);
        cardStacks = new ArrayList<>();
        this.backgroundImage = backgroundImage;
    }


    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void addCardStack(CardStack cardStack) {
        cardStacks.add(cardStack);
        add(cardStack);
    }

    public List<CardStack> getCardStacks() {
        return cardStacks;
    }


    public CardStack getCardStack(String name) {
        for (CardStack cardStack : cardStacks)
            if (cardStack.getName().equals(name)) return cardStack;
        return null;
    }

    public void setCardDragged(boolean cardDragged) {
        this.cardDragged = cardDragged;
    }

    public boolean isCardDragged() {
        return cardDragged;
    }

    @Override
    public void paint() {
        g.drawImage(backgroundImage, getX(), getY(), null);
        paintChildren();
    }

    @Override
    public void repaint(int x, int y, int width, int height) {
        x = Math.min(getWidth(), Math.max(x, 0));
        y = Math.min(getHeight(), Math.max(y, 0));
        width = Math.min(width, getWidth() - x);
        height = Math.min(height, getHeight() - y);
        if (height == 0 || width == 0)
            return;
        g.setClip(x, y, width, height);

        g.drawImage(backgroundImage.getSubimage(x, y, width, height), x, y, null);
        Rectangle repaintRec = new Rectangle(x, y, width, height);
        for (Component child : getChildren()) {
            Rectangle childRec = new Rectangle(child.getX(), child.getY(), child.getWidth(), child.getHeight());
            if (childRec.intersects(repaintRec)) {
                System.out.println("Paint child " + child);
                // TODO: 20.10.2022 Repainting needs some more work 
                child.repaint(childRec.intersection(repaintRec));
            }
        }
        g.setClip(null);
    }


    @Override
    public void paintChildren() {
        super.paintChildren();
    }

    public void addMouseListener(MouseListener mouseListener) {
        game.addMouseListener(mouseListener);
    }


    public void removeMouseListener(MouseListener mouseListener) {
        game.removeMouseListener(mouseListener);
    }

    public CardStack getCardStackAt(Point location) {
        for (CardStack stack : cardStacks) {
            if (stack.pointInside(location))
                return stack;
        }
        return null;
    }


}

