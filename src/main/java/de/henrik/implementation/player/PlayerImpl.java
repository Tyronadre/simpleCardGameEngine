package de.henrik.implementation.player;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.events.GameMouseListenerAdapter;
import de.henrik.engine.game.Border;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.GameStateChangeEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.card.BasicCard;
import de.henrik.implementation.card.landmark.Landmark;
import de.henrik.implementation.card.landmark.LandmarkBuilder;
import de.henrik.implementation.card.playingcard.CardType;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;

public class PlayerImpl extends Player {
    int coins;
    LinkedHashMap<Landmark, Boolean> landmarkHashMap;


    public PlayerImpl(int id, String name) {
        super(id, name);
        coins = 5000;
        loadLandmarks();
        playerPane = new PlayerPaneImpl(this);
    }

    private void loadLandmarks() {
        landmarkHashMap = new LinkedHashMap<>();
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
        ((PlayerPaneImpl) playerPane).updateCoinLabel();
        System.out.println("Player " + getName() + " has " + this.coins + " coins left.");
        return coins;

    }

    public void addCoins(int coins) {
        this.coins += coins;
        ((PlayerPaneImpl) playerPane).updateCoinLabel();
        System.out.println("Player " + getName() + " has " + this.coins + " coins left.");
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

        // cardstacks
        for (CardStack stack : gameBoard.drawStacks.getCardStacks()) {
            Card topCard = stack.getCard();
            if (topCard != null && ((BasicCard) topCard).getCost() <= getCoins())
                stack.setBorder(new Border(Color.GREEN, true, 2, stack, 5));
            else stack.setBorder(null);
        }
        gameBoard.drawStacks.repaint();


        // landmarks
        for (CardStack landmarkStack : playerPane.landmarks.getStacks()) {
            Landmark landmark = (Landmark) landmarkStack.getCard();
            if (landmark.getCost() <= getCoins() && !hasLandmark(landmark.getID()))
                landmarkStack.setBorder(new Border(Color.GREEN, true, 2, landmarkStack, 5));
            else landmarkStack.setBorder(null);
            landmarkStack.addMouseListener(new GameMouseListenerAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (PlayerImpl.this == Game.game.getActivePlayer() && e.getButton() == MouseEvent.BUTTON1 && landmark.getCost() <= getCoins() && !hasLandmark(landmark.getID())) {
                        removeCoins(landmark.getCost());
                        landmarkHashMap.put(landmark, true);
                        Game.game.event(new GameStateChangeEvent(GameBoard.NEW_PLAYER_STATE));
                        landmarkStack.setRenderPolicy(CardStack.RP_ALL_CARDS_TURNED);
                    }
                }
            });
            landmarkStack.repaint();
        }
    }

    public List<CardStack> getCardStacks() {
        return getPlayerPane().ownedCards.getStacks();
    }

    public void removeBorders() {
        getPlayerPane().removeBorders();
    }

    public void removeEmptyStacks() {
        getPlayerPane().ownedCards.removeEmptyStacks();
    }

    public List<Card> getCardList(CardType type) {
        List<Card> cardList = new ArrayList<>();
        for (Card card : getCardList()) {
            if (card instanceof PlayingCard) {
                if (((PlayingCard) card).getCardType() == type) {
                    cardList.add(card);
                }
            }
        }
        return cardList;
    }

}
