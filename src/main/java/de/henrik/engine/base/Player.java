package de.henrik.engine.base;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.components.Label;
import de.henrik.engine.components.Pane;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract public class Player {
    protected int id;
    String name;
    List<Card> cardList;

    protected Pane playerPane;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.cardList = new ArrayList<>();
    }

    abstract public void initPlayerPane();



    public Pane getPlayerPane() {
        return playerPane;
    }
}
