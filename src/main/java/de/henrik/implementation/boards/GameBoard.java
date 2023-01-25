package de.henrik.implementation.boards;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.base.Board;
import de.henrik.implementation.player.Player;
import de.henrik.implementation.game.Options;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends Board {
    private final List<Player> players;

    private Card cardDragged;

    public GameBoard(GameImage backgroundImage) {
        super(backgroundImage);
        this.players = new ArrayList<>();
    }

    /**
     * sets the card that should be dragged.
     * <p>
     * If null the dragged card will be removed. If there is no dragged card, nothing will happen.
     *
     * @param card The dragged Card
     * @throws IllegalArgumentException if there is already a card being dragged.
     */
    public void setCardDragged(Card card) {
        if (card == null) {
            remove(cardDragged);
        } else if (isCardDragged()) throw new IllegalArgumentException();
        else add(card);
        this.cardDragged = card;
    }

    public boolean isCardDragged() {
        return cardDragged != null;
    }

    @Override
    public void activate() {
        for (int i = 0; i < Options.getPlayerCount(); i++) {
            var p = new Player(i, "Player " + i);
            players.add(p);
            add(p.getPlayerPane());
        }
        repaint();
        super.activate();
    }


//    /**
//     * Returns the topmost cardStack at that position or null if there is none
//     *
//     * @param location the location to search
//     * @return the cardStack or null
//     */
//    public CardStack getCardStackAt(Point location) {
//        for (CardStack stack : cardStacks) {
//            if (stack.pointInside(location)) return stack;
//        }
//        return null;
//    }
}
