package de.henrik.implementation.card;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;

import java.awt.*;

public class BasicCardStack extends CardStack {
    public BasicCardStack(String name, Card allowedCardType, int maxStackSize) {
        super(name,RP_ALL_CARDS_TURNED, card -> card.getID() == allowedCardType.getID(),maxStackSize);
    }

    public BasicCardStack(String name, int maxStackSize) {
        super(name, RP_ALL_CARDS_TURNED, card -> true, maxStackSize);
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        g.setClip(getClip());
        g.setColor(Color.red);
        if (getStackSize() == 0) {
            g.fillRect(0,0,3000,3000);
        }
        g.setColor(null);
        g.setClip(null);
    }
}
