package de.henrik.implementation.player;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.card.CardStackArea;
import de.henrik.engine.events.GameMouseListenerAdapter;
import de.henrik.engine.game.Border;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.CardEvent;
import de.henrik.implementation.GameEvent.DraggingCardEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.card.BasicCard;
import de.henrik.implementation.card.landmark.Landmark;
import de.henrik.implementation.card.landmark.LandmarkBuilder;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.util.List;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Predicate;

public class PlayerImpl extends Player {
    int coins;
    HashMap<Landmark, Boolean> landmarkHashMap;


    public PlayerImpl(int id, String name) {
        super(id, name);
        coins = 5;
        loadLandmarks();
        playerPane = new PlayerPaneImpl(this);
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
        ((PlayerPaneImpl) playerPane).updateCoinLabel(coins);
        return coins;

    }

    public void addCoins(int coins) {
        this.coins += coins;
        ((PlayerPaneImpl) playerPane).updateCoinLabel(coins);
    }

    @Override
    public Predicate<Card> isActiveCard() {
        return null;
    }

    public boolean addCard(PlayingCard card) {
        if (coins < card.getCost()) {
            return false;
        }
        removeCoins(card.getCost());
        cardList.add(card);
        ((PlayerPaneImpl) playerPane).getCardArea().addCard(card);
        return true;
    }


    public boolean hasLandmark(int id) {
        for (Landmark landmark : landmarkHashMap.keySet()) {
            if (landmark.getID() == id) {
                return landmarkHashMap.get(landmark);
            }
        }
        throw new IllegalArgumentException("This id is not a loaded Landmark: " + id);
    }

    @Override
    public PlayerPaneImpl getPlayerPane() {
        return (PlayerPaneImpl) playerPane;
    }

    public void updateGameBoard(GameBoard gameBoard) {
        PlayerPaneImpl playerPane = getPlayerPane();

        // borders for cardstacks
        for (CardStack stack : gameBoard.drawStacks.getCardStacks()) {
            Card topCard = stack.getCard();
            if (((BasicCard) topCard).getCost() <= PlayerImpl.this.getCoins())
                stack.setBorder(new Border(Color.GREEN, true, 2, stack, 5));
            else stack.setBorder(null);
        }
        gameBoard.drawStacks.repaint();
    }

    public List<CardStack> getCardStacks() {
        return getPlayerPane().ownedCards.getStacks();
    }

    public void removeBorders() {
        getPlayerPane().removeBorders();
    }
}
