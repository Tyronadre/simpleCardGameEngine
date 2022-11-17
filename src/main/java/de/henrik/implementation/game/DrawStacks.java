package de.henrik.implementation.game;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.card.CardStackArea;
import de.henrik.implementation.card.BasicCardStack;
import de.henrik.implementation.card.DraggableCardStack;
import de.henrik.implementation.card.playingcard.PlayingCardBuilder;

import java.awt.*;
import java.util.List;

public class DrawStacks extends GameComponent {
    private final CardStackArea drawStacks;
    private final CardStack startCards;

    private final int startCardsXSpace = 10;
    private final int startCardsYSpace = 10;
    private int drawStacksMaxCount;

    public DrawStacks(int drawStacksMaxCount, Dimension size, Point pos) {
        super(pos, size);
        List<Card> playingCards = PlayingCardBuilder.buildCardsFromCSV("/cardsE0.csv");
        if (Options.expansion1Selected)
            playingCards.addAll(PlayingCardBuilder.buildCardsFromCSV("/cardsE1.csv"));
        if (Options.expansion2Selected)
            playingCards.addAll(PlayingCardBuilder.buildCardsFromCSV("/cardsE2.csv"));
        this.drawStacksMaxCount = drawStacksMaxCount;
        startCards = new BasicCardStack("initStack", -1);
        startCards.addCards(playingCards);
        startCards.setDrawStackSizeHint(true);

        drawStacks = new CardStackArea(drawStacksMaxCount, 10, 10);

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
        if (startCards.getStackSize() == 0)
            return;
        while (anyStackEmpty() && startCards.getStackSize() != 0) {
            addCardToDrawStack(startCards.removeCard());
            startCards.repaint();
        }
    }

    private void addCardToDrawStack(Card card) {
        CardStack emptyStack = null;
        for (CardStack stack : drawStacks.getStacks()) {
            if (stack.addCard(card))
                return;
            if (stack.getStackSize() == 0) {
                emptyStack = stack;
            }
        }
        drawStacks.removeStack(emptyStack);
        DraggableCardStack draggableCardStack = new DraggableCardStack("draw_stack_" + card.getID(), card, -1);
        draggableCardStack.addCard(card);
        drawStacks.addStack(draggableCardStack);
    }

    private boolean anyStackEmpty() {
        if (drawStacks.getStacks().size() <= drawStacksMaxCount)
            return true;
        for (CardStack stack : drawStacks.getStacks()) {
            if (stack.getStackSize() == 0) {
                return true;
            }
        }

        return false;
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
        g.setClip(getClip());
        g.getGraphics().fillRect(0, 0, 3000, 3000);
        drawStacks.paint(g.create());
        startCards.paint(g.create());
        //        for (var drawStack : drawStacks) {
//            if (drawStack.cardStack == null) {
//                g.setColor(Color.green);
//                g.getGraphics().fillRect(drawStack.position.x, drawStack.position.y, drawStack.dimension.width, drawStack.dimension.height);
//                g.setColor(null);
//                continue;
//            }
//            drawStack.cardStack.paint(g);
//        }
//        startCards.paint(g);
//        g.dispose();
    }
}
