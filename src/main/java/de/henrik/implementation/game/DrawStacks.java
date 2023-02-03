package de.henrik.implementation.game;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.card.CardStackArea;
import de.henrik.engine.events.GameEventListener;
import de.henrik.implementation.card.playingcard.PlayingCardBuilder;
import de.henrik.implementation.card.stack.BasicCardStack;
import de.henrik.implementation.card.stack.DraggableCardStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawStacks extends GameComponent {
    private final CardStackArea drawStacks;
    private final CardStack startCards;

    private final int startCardsXSpace = 10;
    private final int startCardsYSpace = 20;
    private int drawStacksMaxCount;
    private List<GameEventListener> gameEventListeners = new ArrayList<>();

    public DrawStacks(int drawStacksMaxCount, Dimension size, Point pos) {
        super(pos, size);
        List<Card> playingCards = PlayingCardBuilder.buildCardsFromCSV("/cardsE0.csv");
        if (Options.expansion1Selected) playingCards.addAll(PlayingCardBuilder.buildCardsFromCSV("/cardsE1.csv"));
        if (Options.expansion2Selected) playingCards.addAll(PlayingCardBuilder.buildCardsFromCSV("/cardsE2.csv"));
        this.drawStacksMaxCount = drawStacksMaxCount;
        startCards = new BasicCardStack("initStack", -1);
        startCards.addCards(playingCards);
        startCards.shuffel();
        startCards.setDrawStackSizeHint(true);
        drawStacks = new CardStackArea(drawStacksMaxCount, 20, 20);
        add(drawStacks);
        add(startCards);
        resize();
    }

    private void resize() {
        int startCardsHeight = getHeight() - 2 * startCardsYSpace;
        int startCardsWidth = (int) (startCardsHeight * (2 / (double) 3));
        startCards.setPosition(getWidth() - startCardsWidth - startCardsXSpace, startCardsYSpace + getY());
        startCards.setSize(startCardsWidth, startCardsHeight);
        drawStacks.setPosition(getPosition());
        drawStacks.setSize(getWidth() - startCardsWidth - 2 * startCardsXSpace, getHeight());
        repaint(getClip());
    }

    @Override
    public void setPosition(int x, int y) {
        super.setSize(x, y);
        resize();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        resize();
    }

    /**
     * Fills the drawStacks.
     * If there are no cards left, nothing happens.
     * If there are no drawStacks to fill, nothing happens.
     */
    public void fillDrawStacks() {
        if (startCards.getStackSize() == 0) return;
        while (anyStackEmpty() && startCards.getStackSize() != 0) {
            addCardToDrawStack(startCards.removeCard());
        }
    }

    private void addCardToDrawStack(Card card) {
        CardStack emptyStack = null;
        for (CardStack stack : drawStacks.getStacks()) {
            if (stack.addCard(card)) return;
            if (stack.getStackSize() == 0) {
                emptyStack = stack;
            }
        }
        drawStacks.removeStack(emptyStack);
        DraggableCardStack draggableCardStack = new DraggableCardStack("draw_stack_" + card.getID(), card, -1);
        draggableCardStack.addCard(card);
        drawStacks.addStack(draggableCardStack);
//        draggableCardStack.addMouseListener(draggableCardStack.getDragAdapter());
    }

    private boolean anyStackEmpty() {
        if (drawStacks.getStacks().size() <= drawStacksMaxCount) return true;
        for (CardStack stack : drawStacks.getStacks()) {
            if (stack.getStackSize() == 0) {
                return true;
            }
        }

        return false;
    }

    public List<CardStack> getCardStacks() {
        return drawStacks.getStacks();
    }

    /**
     * Trys to add a card to the existing stacks. if the card is null returns false
     *
     * @param card the card to add
     * @return {@code TRUE} if the card was added, otherwise {@code FALSE}
     */
    private boolean addCardToExistingStack(Card card) {
//        if (card == null)
//            return false;
//
//        for (var drawStack : drawStacks) {
//            if (drawStack.CardID == card.ID) {
//                drawStack.cardStack.addCard(card);
//                return true;
//            }
//        }
        return false;
    }

    @Override
    public void paint(GameGraphics g) {
        g.setColor(Color.DARK_GRAY);
        g.getGraphics().fillRect(getX(), getY(), getWidth(), getHeight());
        super.paint(g);
    }

    public boolean canFillDrawStacks() {
        return startCards.getStackSize() > 0;
    }
}
