package de.henrik.implementation.card;

import de.henrik.engine.card.CardStack;
import de.henrik.engine.card.CardStackArea;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.card.stack.BasicCardStack;

public class SelfStackingCardStackArea extends CardStackArea {
    public SelfStackingCardStackArea(int maxNumberOfStack, int xSpace, int ySpace, int maxCardHeight, int maxCardWidth) {
        super(maxNumberOfStack, xSpace, ySpace, maxCardHeight,maxCardWidth);
    }


    public void addCard(PlayingCard playingCard) {
        boolean notAdded = true;
        for (CardStack cardStack : this.getStacks()) {
            if (cardStack.test(playingCard)) {
                cardStack.moveCardToStack(playingCard);
                notAdded = false;
            }
        }
        if (notAdded) {
            CardStack stack = new BasicCardStack("s", playingCard, -1);
            this.addStack(stack);
            stack.moveCardToStack(playingCard);
        }
    }
}