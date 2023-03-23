package de.henrik.implementation.card;

import de.henrik.engine.card.CardStack;
import de.henrik.engine.card.CardStackArea;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.card.stack.BasicCardStack;

import java.util.ArrayList;
import java.util.List;

public class SelfStackingCardStackArea extends CardStackArea {
    public SelfStackingCardStackArea(int maxNumberOfStack, int xSpace, int ySpace) {
        super(maxNumberOfStack, xSpace, ySpace);
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
            CardStack stack = new BasicCardStack("s", playingCard, 7);
            stack.setDrawStackSizeHint(true);
            this.addStack(stack);
            stack.moveCardToStack(playingCard);
        }
    }

    public void removeCard(PlayingCard card) {
        List<CardStack> stacks = new ArrayList<>();
        for (CardStack cardStack : this.getStacks()) {
            if (cardStack.test(card)) {
                cardStack.removeCard();
                if (cardStack.getStackSize() == 0) {
                    stacks.add(cardStack);
                }
            }
        }
        for (CardStack stack : stacks) {
            this.removeStack(stack);
        }
    }
}
