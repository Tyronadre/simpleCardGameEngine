package de.henrik.implementation.card;

import de.henrik.implementation.card.playingcard.CardClass;
import de.henrik.implementation.card.playingcard.CardType;

import java.awt.*;

public class Landmark extends BasicCard{
    public Landmark(int ID, int cost, CardClass cardClass, CardType cardType, Image frontOfCard, Image backOfCard) {
        super(ID, cost, cardClass, cardType, frontOfCard, backOfCard);
    }
}
