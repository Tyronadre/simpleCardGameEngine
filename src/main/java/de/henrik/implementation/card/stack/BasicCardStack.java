package de.henrik.implementation.card.stack;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;

public class BasicCardStack extends CardStack {

    public BasicCardStack(String name, Card allowedCardType, int maxStackSize) {
        super(name, RP_ALL_CARDS_TURNED, card -> card.equals(allowedCardType), maxStackSize);
    }

    public BasicCardStack(String name, int maxStackSize) {
        super(name, RP_ALL_CARDS_TURNED, card -> true, maxStackSize);
    }
}
