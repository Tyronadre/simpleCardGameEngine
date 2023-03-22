package de.henrik.engine.game;

import de.henrik.engine.card.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

abstract public class Player {
    protected final int id;
    final String name;
    protected final List<Card> cardList;

    protected PlayerPane playerPane;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.cardList = new ArrayList<>();
    }

    public PlayerPane getPlayerPane() {
        return playerPane;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Card> getCardList() {
        return cardList;
    }
}
