package de.henrik.implementation.game;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.card.CardStackArea;
import de.henrik.engine.components.Button;
import de.henrik.engine.components.Label;
import de.henrik.engine.game.Game;
import de.henrik.implementation.GameEvent.GameStateChangeEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.card.playingcard.PlayingCardBuilder;
import de.henrik.implementation.card.stack.BasicCardStack;
import de.henrik.implementation.card.stack.DraggableCardStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawStacks extends GameComponent {
    private final CardStackArea drawStacks;
    private final CardStack startCards;

    private final int drawStacksMaxCount;
    public final Button dice;
    public final Button twoDice;
    public final Button skipTurn;
    public final Label diceRoll;
    public final Label activePlayerLabel;

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
        drawStacks = new CardStackArea(drawStacksMaxCount, 5, 5);

        dice = new Button(new GameImage("/other/dice.png"));
        dice.disable();
        twoDice = new Button(new GameImage("/other/twoDice.png"));
        twoDice.disable();
        diceRoll = new Label("Rolled: 0");
        skipTurn = new Button("Skip Turn");
        skipTurn.addActionListener(e -> Game.game.event(new GameStateChangeEvent(GameBoard.NEW_PLAYER_STATE)));
        skipTurn.disable();
        activePlayerLabel = new Label("Loading...");

        add(dice);
        add(twoDice);
        add(diceRoll);
        add(skipTurn);
        add(drawStacks);
        add(startCards);
        add(activePlayerLabel);

        resize();
    }

    private void resize() {
        int startCardsYSpace = 20;
        int startCardsHeight = getHeight() - 2 * startCardsYSpace;
        int startCardsWidth = (int) (startCardsHeight * (2 / (double) 3));
        int startCardsXSpace = 10;
        startCards.setPosition(getWidth() - startCardsWidth - startCardsXSpace, startCardsYSpace + getY());
        startCards.setCardSize(startCardsWidth, startCardsHeight);
        dice.setPosition(startCards.getX() - 220, 10 + getY());
        dice.setSize(100, 100);
        twoDice.setPosition(startCards.getX() - 110, 10 + getY());
        twoDice.setSize(100, 100);
        diceRoll.setPosition(startCards.getX() - 220, 150 + getY());
        diceRoll.setSize(210, 30);
        skipTurn.setPosition(startCards.getX() - 220, 190 + getY());
        skipTurn.setSize(210, 30);
        activePlayerLabel.setPosition(startCards.getX() - 220, 240 + getY());
        activePlayerLabel.setSize(210, 30);

        drawStacks.setPosition(getPosition());
        drawStacks.setSize(getWidth() - startCards.getWidth() - 230, getHeight());
        repaint();
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
            removeEmptyStacks();
        while (anyStackEmpty() && startCards.getStackSize() != 0) {
            addCardToDrawStack(startCards.removeCard());
        }
        if (anyStackEmpty() && startCards.getStackSize() == 0)
            removeEmptyStacks();
    }

    private void removeEmptyStacks() {
        List<CardStack> removeStacks = new ArrayList<>();
        for (CardStack cardStack : getCardStacks()) {
            if (cardStack.getStackSize() == 0) {
                removeStacks.add(cardStack);
            }
        }
        for (CardStack cardStack : removeStacks) {
            drawStacks.removeStack(cardStack);
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
        DraggableCardStack draggableCardStack = new DraggableCardStack("draw_stack_" + card.getID(), card, 7);
        draggableCardStack.addCard(card);
        drawStacks.addStack(draggableCardStack);
    }

    private boolean anyStackEmpty() {
        if (drawStacks.getStacks().size() < drawStacksMaxCount) return true;
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



    @Override
    public void paint(GameGraphics g) {
        g.setColor(new Color(0.25f, 0.25f, 0.25f,0.6f));

        g.getGraphics().fillRect(getX(), getY(), getWidth(), getHeight());
        g.setColor(GameGraphics.defaultColor);
        super.paint(g);
    }

    public void removeBorders() {
        this.setBorder(null);
        for (CardStack cardStack : getCardStacks()) {
            cardStack.setBorder(null);
        }

    }
}
