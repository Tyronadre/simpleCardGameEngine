package de.henrik.implementation.card.landmark;

import de.henrik.engine.base.GameImage;
import de.henrik.implementation.card.BasicCard;
import de.henrik.implementation.card.playingcard.CardClass;
import de.henrik.implementation.card.playingcard.CardType;

public class Landmark extends BasicCard {
    public Landmark(int ID, int cost, CardClass cardClass, CardType cardType, GameImage frontOfCard, GameImage backOfCard) {
        super(ID, cost, cardClass, cardType, frontOfCard, backOfCard);
    }
}
