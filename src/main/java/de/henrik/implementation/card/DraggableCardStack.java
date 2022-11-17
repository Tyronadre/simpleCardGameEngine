package de.henrik.implementation.card;

import de.henrik.engine.card.Card;

public class DraggableCardStack extends BasicCardStack {

    public DraggableCardStack(String name, Card allowedCardType, int maxStackSize) {
        super(name, allowedCardType, maxStackSize);
    }

    public DraggableCardStack(String name, int maxStackSize) {
        super(name, maxStackSize);
    }
}
