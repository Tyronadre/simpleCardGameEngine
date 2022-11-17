package de.henrik.engine.game;

import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.base.GameComponent;

import java.awt.*;
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
public class GameBoard extends GameComponent {
    private BufferedImage backgroundImage;
    private final List<CardStack> cardStacks;
    private boolean build;

    private Card cardDragged;
    private final Game game = Game.game;


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

    /**
     * sets the card that should be dragged.
     * <p>
     * If null the dragged card will be removed. If there is no dragged card, nothing will happen.
     * @param card The dragged Card
     * @exception IllegalArgumentException if there is already a card being dragged.
     */
    public void setCardDragged(Card card) {
        if (card == null) {
            remove(cardDragged);
        } else if (isCardDragged())
            throw new IllegalArgumentException();
        else
            add(card);
        this.cardDragged = card;

    }

    public boolean isCardDragged() {
        return cardDragged != null;
    }

    @Override
    public void paint(GameGraphics g) {
        g.drawImage(backgroundImage, getX(), getY());
        paintChildren(g.create());
    }


    public void addMouseListener(MouseListener mouseListener) {
        game.addMouseListener(mouseListener);
    }


    public void removeMouseListener(MouseListener mouseListener) {
        game.removeMouseListener(mouseListener);
    }

    /**
     * Returns the topmost cardStack at that position or null if there is none
     *
     * @param location the location to search
     * @return the cardStack or null
     */
    public CardStack getCardStackAt(Point location) {
        for (CardStack stack : cardStacks) {
            if (stack.pointInside(location))
                return stack;
        }
        return null;
    }


}

