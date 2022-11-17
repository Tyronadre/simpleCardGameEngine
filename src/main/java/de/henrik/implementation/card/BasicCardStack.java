package de.henrik.implementation.card;

import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.game.Game;

import java.awt.*;

public class BasicCardStack extends CardStack {
    public BasicCardStack(String name, Card allowedCardType, int maxStackSize) {
        super(name,RP_ALL_CARDS_TURNED, card -> card.getID() == allowedCardType.getID(),maxStackSize);
    }

    public BasicCardStack(String name, int maxStackSize) {
        super(name, RP_ALL_CARDS_TURNED, card -> true, maxStackSize);
    }
}
