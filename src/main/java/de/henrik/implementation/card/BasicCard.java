package de.henrik.implementation.card;

import de.henrik.engine.card.Card;
import de.henrik.implementation.card.playingcard.CardClass;
import de.henrik.implementation.card.playingcard.CardType;

import java.awt.*;

// FIXME: 27.10.2022 Implementation

public class BasicCard extends Card {
    private final int cost;
    private final CardClass cardClass;
    private final CardType cardType;

    public BasicCard(int ID, int cost, CardClass cardClass, CardType cardType, Image frontOfCard, Image backOfCard) {
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



}