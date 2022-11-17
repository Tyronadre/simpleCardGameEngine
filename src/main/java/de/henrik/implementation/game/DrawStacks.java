package de.henrik.implementation.game;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.implementation.card.BasicCardStack;
import de.henrik.implementation.card.DraggableCardStack;
import de.henrik.implementation.card.playingcard.PlayingCardBuilder;

import java.awt.*;
import java.util.List;

public class DrawStacks extends GameComponent {
    private final DrawStackInfo[] drawStacks;
    private final int drawStacksMaxCount;
    private final CardStack startCards;


    public DrawStacks(int drawStacksMaxCount, Dimension size, Point pos) {
        super(pos, size);
        List<Card> playingCards = PlayingCardBuilder.buildCardsFromCSV("/cardsE0.csv");
        if (Options.expansion1Selected)
            playingCards.addAll(PlayingCardBuilder.buildCardsFromCSV("/cardsE1.csv"));
        if (Options.expansion2Selected)
            playingCards.addAll(PlayingCardBuilder.buildCardsFromCSV("/cardsE2.csv"));

        startCards = new BasicCardStack("initStack", 20);
        drawStacks = new DrawStackInfo[drawStacksMaxCount];
        this.drawStacksMaxCount = drawStacksMaxCount;
        initDrawStacks();
//        startCards.setSize((getWidth() - 2 * X_SPACING) / (drawStacksMaxCount + 1) - (X_SPACING));
//        startCards.setPosition();
    }

    final int X_SPACING = 10;
    final int Y_SPACING = 10;


    private void initDrawStacks() {
        for (int i = 0; i < drawStacks.length; i++) {
            drawStacks[i] = new DrawStackInfo();
            drawStacks[i].CardID = -1;

            //2*xspace extra für mainstack
            int cardWidth = (getWidth() - 2 * X_SPACING) / (drawStacksMaxCount + 1) - (X_SPACING);
            int cardHeight = (int) (cardWidth / (2 / (double) 3));

            int cardX;
            int cardY;
            //Zwei reihen wenn viel karten:
            if (cardHeight * 2 + 3 * Y_SPACING <= getHeight()) {
                cardX = getWidth() / (drawStacksMaxCount + 1) * (i % (drawStacksMaxCount / 2)) + X_SPACING;
                cardY = getHeight() + (getHeight() / 2 - cardHeight) / 2 + Y_SPACING;
                if (i >= drawStacksMaxCount / 2)
                    cardY += Y_SPACING + cardHeight;
            } else {
                cardX = getWidth() / (drawStacksMaxCount + 1) * i + X_SPACING;
                cardY = getHeight() + (getHeight() - cardHeight) / 2;
            }

            drawStacks[i].dimension = new Dimension(cardWidth, cardHeight);
            drawStacks[i].position = new Point(cardX, cardY);
        }
    }


    @Override
    public void setPosition(int x, int y) {
        super.setSize(x, y);
        for (int i = 0; i < drawStacksMaxCount; i++)
            drawStacks[i].position = new Point(X_SPACING + getWidth() / 11 * i, (getHeight() - (getHeight() * 4 / 3)) / 2);

    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        for (int i = 0; i < drawStacksMaxCount; i++)
            drawStacks[i].dimension = new Dimension((getWidth() / 11) - (2 + X_SPACING), getHeight() * 4 / 3);
    }

    /**
     * Fills the drawStacks.
     * If there are no cards left, nothing happens.
     * If there are no drawStacks to fill, nothing happens.
     */
    public void fillDrawStacks() {
        for (DrawStackInfo drawStack : drawStacks) {
            if (drawStack.cardStack == null) {
                Card newCard = startCards.removeCard();
                while (addCardToExistingStack(newCard))
                    newCard = startCards.removeCard();
                if (newCard == null)
                    return;
                CardStack cardStack = new DraggableCardStack("draw_stack_" + newCard.getID(), newCard, 3);
                cardStack.addCard(newCard);
                cardStack.setPosition(drawStack.position);
                cardStack.setSize(drawStack.dimension);

                drawStack.cardStack = cardStack;
                drawStack.CardID = newCard.ID;
            }
        }
    }

    /**
     * Trys to add a card to the existing stacks. if the card is null returns false
     *
     * @param card the card to add
     * @return {@code TRUE} if the card was added, otherwise {@code FALSE}
     */
    private boolean addCardToExistingStack(Card card) {
        if (card == null)
            return false;

        for (var drawStack : drawStacks) {
            if (drawStack.CardID == card.ID) {
                drawStack.cardStack.addCard(card);
                return true;
            }
        }
        return false;
    }

    static class DrawStackInfo {
        CardStack cardStack;
        int CardID;
        Dimension dimension;
        Point position;
    }

    @Override
    public void paint(GameGraphics g) {
        g.getGraphics().fillRect(0, 0, 3000, 3000);
        for (var drawStack : drawStacks) {
            if (drawStack.cardStack == null) {
                g.setColor(Color.green);
                g.getGraphics().fillRect(drawStack.position.x, drawStack.position.y, drawStack.dimension.width, drawStack.dimension.height);
                g.setColor(null);
                continue;
            }
            drawStack.cardStack.paint(g);
        }
        startCards.paint(g);
        g.dispose();
    }
}
