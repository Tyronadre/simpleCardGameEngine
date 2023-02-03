package de.henrik.engine.game;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.components.Label;
import de.henrik.engine.components.Pane;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

abstract public class Player {
    protected int id;
    String name;
    protected List<Card> cardList;

    protected PlayerPane playerPane;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.cardList = new ArrayList<>();
    }

    public PlayerPane getPlayerPane() {
        return playerPane;
    }

    abstract public Predicate<Card> isActiveCard();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
