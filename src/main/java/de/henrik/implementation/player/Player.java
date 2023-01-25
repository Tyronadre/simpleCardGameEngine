package de.henrik.implementation.player;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.components.Label;
import de.henrik.engine.card.CardStackArea;
import de.henrik.engine.components.Pane;
import de.henrik.implementation.card.landmark.Landmark;
import de.henrik.implementation.card.landmark.LandmarkBuilder;
import de.henrik.implementation.card.stack.BasicCardStack;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends de.henrik.engine.base.Player {
    int coins;
    HashMap<Landmark, Boolean> landmarkHashMap;
    CardStackArea landmarks = new CardStackArea(-1, 4, 4);

    Label coinLabel;


    public Player(int id, String name) {
        super(id, name);
        coins = 5;
        loadLandmarks();
        initPlayerPane();
    }

    private void loadLandmarks() {
        landmarkHashMap = new HashMap<>();
        for (Landmark landmark : LandmarkBuilder.buildLandmarkFromCSV("/landmarksE0.csv")) {
            landmarkHashMap.put(landmark, false);
        }
        if (Options.expansion1Selected) {
            for (Landmark landmark : LandmarkBuilder.buildLandmarkFromCSV("/landmarksE1.csv")) {
                landmarkHashMap.put(landmark, false);
            }
        }
        if (Options.expansion2Selected) {
            for (Landmark landmark : LandmarkBuilder.buildLandmarkFromCSV("/landmarksE2.csv")) {
                landmarkHashMap.put(landmark, false);
            }
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
                        x = 0;
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


        // --- LANDMARKS, COINS, PLAYERNAME --- //
        GameImage landmarkBG = new GameImage("/player/landmarkBackground.png");
        Pane landmarksAndInfo = new Pane(landmarkBG, x, y, width / 4 - 3, height);
        playerPane.add(landmarksAndInfo);

        CardStackArea landmarks = new CardStackArea(20, 3, 3);
        landmarksAndInfo.add(landmarks);
        landmarks.setSize(landmarksAndInfo.getWidth(), landmarksAndInfo.getHeight()-40);
        landmarks.setPosition(landmarksAndInfo.getX(), landmarksAndInfo.getY()+40);
        for (Landmark landmark : landmarkHashMap.keySet()) {
            CardStack landmarkStack = new BasicCardStack("player_" + this.id + "_landmark_" + landmark.ID, landmark, 1);
            landmarkStack.addCard(landmark);
            landmarks.addStack(landmarkStack);
        }

        Label playerName = new Label("Player " + id, landmarksAndInfo.getX(), landmarksAndInfo.getY(), 150, 30);
        coinLabel = new Label("Coins: " + coins, 150 + landmarksAndInfo.getX(), landmarksAndInfo.getY(), 150, 30);

        landmarksAndInfo.add(playerName);
        landmarksAndInfo.add(coinLabel);

        // --- PLAYER CARDS --- //
        playerPane.add(new Pane(new GameImage(Color.BLUE), x + width / 4 + 5, y, width / 4 * 3 - 5, height));
    }
}
