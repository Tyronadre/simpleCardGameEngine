package de.henrik.implementation.card;

import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.card.Card;
import de.henrik.engine.base.GameImage;
import de.henrik.implementation.card.playingcard.CardClass;
import de.henrik.implementation.card.playingcard.CardType;

import java.awt.*;

// FIXME: 27.10.2022 Implementation

public class BasicCard extends Card {
    private final int cost;
    private final CardClass cardClass;
    private final CardType cardType;

    private boolean deactived = false;

    public BasicCard(int ID, int cost, CardClass cardClass, CardType cardType, GameImage frontOfCard, GameImage backOfCard) {
        super(ID, frontOfCard, backOfCard);
        this.cost = cost;
        this.cardClass = cardClass;
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + getID() +
                "cost=" + cost +
                ", cardClass=" + cardClass +
                ", type=" + cardType +
                ", size= " + getSize() +
                ", pos= " + getPosition() +
                '}';
    }


    public int getCost() {
        return cost;
    }

    public CardClass getCardClass() {
        return cardClass;
    }

    public CardType getCardType() {
        return cardType;
    }

    public boolean isDeactived() {
        return deactived;
    }

    public void setDeactived(boolean deactived) {
        this.deactived = deactived;
    }

    @Override
    public void paint(GameGraphics g) {
        if (deactived) {
            g.setColor(new Color(0, 0, 0, 0.5f));
            g.getGraphics().fillRect(0, 0, getSize().width, getSize().height);
            g.setColor(Color.blue);
            g.getGraphics().drawString("Deactived", 10, 100);
        }
        super.paint(g);
    }
}