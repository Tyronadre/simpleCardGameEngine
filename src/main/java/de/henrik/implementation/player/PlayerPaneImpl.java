package de.henrik.implementation.player;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.card.CardStackArea;
import de.henrik.engine.components.Label;
import de.henrik.engine.components.Pane;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import de.henrik.engine.game.PlayerPane;
import de.henrik.implementation.card.SelfStackingCardStackArea;
import de.henrik.implementation.card.landmark.Landmark;
import de.henrik.implementation.card.stack.BasicCardStack;
import de.henrik.implementation.game.Options;

import java.awt.*;

public class PlayerPaneImpl extends PlayerPane {
    PlayerImpl player;
    CardStackArea landmarks;
    SelfStackingCardStackArea ownedCards;
    Label playerName;
    Label coinLabel;

    public PlayerPaneImpl(PlayerImpl player) {
        super(null, calcInitDim(player)[0], calcInitDim(player)[1],calcInitDim(player)[2],calcInitDim(player)[3]);
        this.player = player;


        // --- LANDMARKS, COINS, PLAYERNAME, SKIP TURN --- //
        GameImage landmarkBG = new GameImage("/player/landmarkBackground.png",true);
        Pane landmarksAndInfo = new Pane(getX(), getY(), getWidth() / 4 - 3, getHeight());
        add(landmarksAndInfo);

        landmarks = new CardStackArea(20, 3, 3);
        landmarksAndInfo.add(landmarks);
        landmarks.setSize(landmarksAndInfo.getWidth(), landmarksAndInfo.getHeight() - 40);
        landmarks.setPosition(landmarksAndInfo.getX(), landmarksAndInfo.getY() + 40);
        for (Landmark landmark : player.landmarkHashMap.keySet()) {
            CardStack landmarkStack = new BasicCardStack("player_" + player.getId() + "_landmark_" + landmark.ID, landmark, 1);
            landmarkStack.setRenderPolicy(CardStack.RP_ALL_CARDS_UNTURNED);
            landmarkStack.addCard(landmark);
            landmarks.addStack(landmarkStack);
        }

        playerName = new Label(player.getName(), landmarksAndInfo.getX(), landmarksAndInfo.getY()+3, 150, 30);
        coinLabel = new Label("Coins: " + player.getCoins(), 150 + landmarksAndInfo.getX(), landmarksAndInfo.getY()+3, 120, 30);

        landmarksAndInfo.add(playerName);
        landmarksAndInfo.add(coinLabel);

        // --- PLAYER CARDS --- //
        Pane playerCardsPane = new Pane(new GameImage("/background/cardsBackground.png",true), getX() + getWidth() / 4 + 5, getY(), getWidth() / 4 * 3 - 5, getHeight());
        ownedCards = new SelfStackingCardStackArea(-1, 5, 5);
        ownedCards.setPosition(playerCardsPane.getPosition());
        ownedCards.setSize(playerCardsPane.getSize());
        playerCardsPane.add(ownedCards);
        add(playerCardsPane);
    }

    protected void updateCoinLabel() {
        coinLabel.setDescription("Coins: " + player.getCoins());
        coinLabel.repaint();
    }

    private static int[] calcInitDim(Player player) {
        int x, y, width, height;
        switch (Options.getPlayerCount()) {
            case 2 -> {
                switch (player.getId()) {
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
                switch (player.getId()) {
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
                switch (player.getId()) {
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
        return new int[]{x, y, width, height};
    }

    public SelfStackingCardStackArea getCardArea() {
        return ownedCards;
    }

    public PlayerImpl getPlayer() {
        return player;
    }

    public void removeBorders() {
        setBorder(null);
        for (GameComponent child : children) {
            child.setBorder(null);
        }
        for (GameComponent landmark: landmarks.getChildren()) {
            landmark.setBorder(null);
        }
        repaint();
    }

    @Override
    public void paint(GameGraphics g) {
        if (player == Game.game.getActivePlayer())
            g.setColor(new Color(0.25f,0.4f,0.25f,0.5f));
        else
            g.setColor(new Color(0.25f,0.4f,0.4f,0.5f));
        g.getGraphics().fillRect(getX(), getY(), getWidth(), getHeight());
        g.setColor(GameGraphics.defaultColor);
        super.paint(g);
    }
}
