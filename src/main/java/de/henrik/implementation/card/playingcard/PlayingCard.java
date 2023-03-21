package de.henrik.implementation.card.playingcard;

import de.henrik.engine.base.GameImage;
import de.henrik.implementation.GameEvent.CardEvent;
import de.henrik.implementation.GameEvent.CardEventListener;
import de.henrik.implementation.card.BasicCard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayingCard extends BasicCard {
    private final CardEventListener eventListener;

    public PlayingCard(int ID, int cost, CardClass cardClass, CardType cardType, GameImage frontOfCard, GameImage backOfCard, CardEventListener eventListener) {
        super(ID, cost, cardClass, cardType, frontOfCard, backOfCard);
        this.eventListener = eventListener;
    }

    public void event(CardEvent event) {
        eventListener.consume(event);
    }


}
