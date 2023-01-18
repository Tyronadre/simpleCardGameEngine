package de.henrik.engine.game;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.components.Label;
import de.henrik.engine.components.Pane;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract public class Player {
    int id;
    String name;
    List<Card> cardList;

    Pane playerPane;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.cardList = new ArrayList<>();
    }

    public void initPlayerPane() {
        int x, y, width, height;
        switch (Options.getPlayerCount()) {
            case 2 -> {
                switch (this.id) {
                    case 0 -> {
                        x = 0;
                        y = 0;
                        width = Options.getWidth();
                        height = Options.getHeight() / 3;
                    }
                    case 1 -> {
                        x = 0;
                        y = Options.getHeight() / 3 * 2;
                        width = Options.getWidth();
                        height = Options.getHeight() / 3;
                    }
                    default -> throw new IllegalArgumentException();
                }
            }
            case 3 -> {
                switch (this.id) {
                    case 0 -> {
                        x = 0;
                        y = 0;
                        width = Options.getWidth() / 2;
                        height = Options.getHeight() / 3;
                    }
                    case 1 -> {
                        x = Options.getWidth() / 2;
                        y = 0;
                        width = Options.getWidth() / 2;
                        height = Options.getHeight() / 3;
                    }
                    case 2 -> {
                        x = Options.getWidth();
                        y = Options.getHeight() / 3 * 2;
                        width = Options.getWidth();
                        height = Options.getHeight() / 3;
                    }
                    default -> throw new IllegalArgumentException();
                }
            }
            case 4 -> {
                switch (this.id) {
                    case 0 -> {
                        x = 0;
                        y = 0;
                        width = Options.getWidth() / 2;
                        height = Options.getHeight() / 3;
                    }
                    case 1 -> {
                        x = Options.getWidth() / 2;
                        y = 0;
                        width = Options.getWidth() / 2;
                        height = Options.getHeight() / 3;
                    }
                    case 2 -> {
                        x = 0;
                        y = Options.getHeight() / 3 * 2;
                        width = Options.getWidth() / 2;
                        height = Options.getHeight() / 3;
                    }
                    case 3 -> {
                        x = Options.getWidth() / 2;
                        y = Options.getHeight() / 3 * 2;
                        width = Options.getWidth() / 2;
                        height = Options.getHeight() / 3;
                    }
                    default -> throw new IllegalArgumentException();
                }
            }
            default -> throw new IllegalArgumentException();
        }
        playerPane = new Pane(new GameImage(Color.BLACK), x, y, width, height);
        GameImage cardsBG = new GameImage("/player/cardsBackground.png");
        GameImage landmarkBG = new GameImage("/player/landmarkBackground.png");
        playerPane.add(new Pane(new GameImage(Color.BLUE), x, y, width / 4, height));
        playerPane.add(new Pane(new GameImage(Color.GREEN), x / 4 * 3 + 5, y, width / 4 * 3 - 5, height));
    }

    public Pane getPlayerPane() {
        return playerPane;
    }
}
