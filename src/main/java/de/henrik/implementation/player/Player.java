package de.henrik.implementation.player;

import de.henrik.engine.base.GameGraphics;
import de.henrik.implementation.game.Options;
import de.henrik.engine.base.GameComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends GameComponent {
    String name;
    private static final List<String> givenNames = new ArrayList<>();
    int coins;


    public Player(String name) {
        if (givenNames.contains(name))
            throw new IllegalArgumentException("Player-names have to be unique");
        givenNames.add(this.name = name);
        coins = 4;

        int playerCount = Options.getPlayerCount();

        if (Options.getPlayerCount() == 2) {
            setPosition(switch (playerCount) {
                case 0 -> new Point(0, 0);
                case 1 -> new Point(0, Options.getHeight() / 3 * 2);
                default -> throw new IllegalStateException("Unexpected value: " + playerCount);
            });
            setSize(new Dimension(Options.getWidth(), Options.getHeight() / 3));
        } else {
            setPosition(switch (playerCount) {
                case 0 -> new Point(0, 0);
                case 1 -> new Point(Options.getWidth() / 2, 0);
                case 2 -> new Point(0, Options.getHeight() / 3 * 2);
                case 3 -> new Point(Options.getWidth() / 2, Options.getHeight() / 3 * 2);
                default -> throw new IllegalStateException("Unexpected value: " + playerCount);
            });
            setSize(new Dimension(Options.getWidth() / 2, Options.getHeight() / 3));
        }
    }

    public int getCoins() {
        return coins;
    }

    public int removeCoins(int coins) {
        if (this.coins <= coins) {
            int retCoins = coins - this.coins;
            this.coins = 0;
            return retCoins;
        }
        this.coins -= coins;
        return coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    @Override
    public void paint(GameGraphics g) {

    }
}
