package de.henrik.implementation.card.playingcard;

import de.henrik.engine.util.GameImage;
import de.henrik.implementation.card.BasicCard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayingCard extends BasicCard {
    private final ActionListener actionListener;

    public PlayingCard(int ID, int cost, CardClass cardClass, CardType cardType, GameImage frontOfCard, GameImage backOfCard, ActionListener actionListener) {
        super(ID, cost, cardClass, cardType, frontOfCard, backOfCard);
        this.actionListener = actionListener;
    }


    public void triggerCard() {
        actionListener.actionPerformed(new ActionEvent(this, getID(), getID() + " triggerd"));
    }



}
