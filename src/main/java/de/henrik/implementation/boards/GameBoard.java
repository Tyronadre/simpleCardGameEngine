package de.henrik.implementation.boards;

import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.game.Board;
import de.henrik.engine.game.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends Board {
    private final List<CardStack> cardStacks;
    private boolean build;

    private Card cardDragged;
    private final Game game = Game.game;

    public GameBoard(GameImage backgroundImage) {
        super(backgroundImage);
    }


    public void setBackgroundImage(GameImage backgroundImage) {
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
     *
     * @param card The dragged Card
     * @throws IllegalArgumentException if there is already a card being dragged.
     */
    public void setCardDragged(Card card) {
        if (card == null) {
            remove(cardDragged);
        } else if (isCardDragged()) throw new IllegalArgumentException();
        else add(card);
        this.cardDragged = card;

    }

    public boolean isCardDragged() {
        return cardDragged != null;
    }

    @Override
    public void paint(GameGraphics g) {
        g.drawImage(backgroundImage.getImage(), getX(), getY());
        super.paint(g);
    }

    @Override
    public void activate() {

    }

    @Override
    public void deavtivte() {

    }


    /**
     * Returns the topmost cardStack at that position or null if there is none
     *
     * @param location the location to search
     * @return the cardStack or null
     */
    public CardStack getCardStackAt(Point location) {
        for (CardStack stack : cardStacks) {
            if (stack.pointInside(location)) return stack;
        }
        return null;
    }
}
