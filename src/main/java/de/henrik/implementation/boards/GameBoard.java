package de.henrik.implementation.boards;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.events.GameEventListener;
import de.henrik.engine.game.*;
import de.henrik.implementation.GameEvent.*;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.game.DrawStacks;
import de.henrik.implementation.game.Options;
import de.henrik.implementation.player.PlayerImpl;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard extends Board {
    private final List<PlayerImpl> players;
    PlayerImpl activePlayer;

    private Card cardDragged;

    public DrawStacks drawStacks;
    int gameState;

    public static final int NEW_PLAYER_STATE = 0;
    public static final int ROLL_DICE_STATE = 1;
    public static final int BUY_CARD_STATE = 2;


    public GameBoard() {
        super(new GameImage("/background/gameboard.jpg", false).getScaledInstance(Options.getWidth(), Options.getHeight()));
        this.players = new ArrayList<>();
        drawStacks = new DrawStacks(10, new Dimension(Options.getWidth(), Options.getHeight() / 3), new Point(0, Options.getHeight() / 3));
        add(drawStacks);
    }

    private GameEventListener onPlayerSwitchListener() {
        return event -> {
            if (event instanceof PlayerChangeEvent playerChangeEvent) {
                drawStacks.activePlayerLabel.setDescription("Active: " + playerChangeEvent.newPlayer.getName());
                Player oldActivePlayer = activePlayer;
                activePlayer = playerChangeEvent.newPlayer;
                game.setActivePlayer(activePlayer);
                if (oldActivePlayer != null) oldActivePlayer.getPlayerPane().repaint();
                activePlayer.getPlayerPane().repaint();

                event(new GameStateChangeEvent(ROLL_DICE_STATE));
            }
        };
    }

    private GameEventListener onDiceRollListener() {
        return event -> {
            if (event instanceof DiceRollEvent diceRollEvent && gameState == ROLL_DICE_STATE) {
                for (int i = 0; i < Options.getPlayerCount(); i++) {
                    PlayerImpl player = players.get((activePlayer.getId() + i + 1 ) % Options.getPlayerCount());
                    for (Card c : player.getCardList()) {
                        PlayingCard card = (PlayingCard) c;
                        card.event(new CardEvent(player, activePlayer, diceRollEvent.roll, this));
                    }
                    player.getPlayerPane().repaint();
                }
                event(new GameStateChangeEvent(BUY_CARD_STATE));
            }
        };
    }

    private GameEventListener onDragAndDropListener() {
        return new GameEventListener() {
            @Override
            public void handleEvent(GameEvent event) {
                if (event instanceof DraggingCardEvent draggingCardEvent && gameState == BUY_CARD_STATE) {
                    System.out.println(draggingCardEvent);
                    PlayerImpl player = draggingCardEvent.player;
                    if (draggingCardEvent.startDragging && draggingCardEvent.card.getCost() <= player.getCoins()) {
                        player.getPlayerPane().setBorder(new Border(Color.GREEN, true, 3, player.getPlayerPane(), 3));
                        player.getPlayerPane().repaint();
                    } else if (draggingCardEvent.endDragging) {
                        player.getPlayerPane().setBorder(null);
                        if (player.getPlayerPane().pointInside(draggingCardEvent.pos)) {
                            if (player.addCard(draggingCardEvent.card)) {
                                ((CardStack) draggingCardEvent.getParent()).removeCard();
                                event(new GameStateChangeEvent(NEW_PLAYER_STATE));
                            }
                        }
                        player.getPlayerPane().repaint();
                    }
                }
            }

            @Override
            public String toString() {
                return "onDragAndDropListener";
            }
        };
    }

    private GameEventListener onChoiceListener() {
        return event -> {
            if (event instanceof ChoiceEvent choiceEvent) {
                for (PlayerImpl player : players) {
                    if (choiceEvent.type.test(player.getPlayerPane())) {
                        player.getPlayerPane().setBorder(new Border(Color.GREEN, true, 3, player.getPlayerPane(), 3));

                    }
                    for (CardStack cardStack : player.getCardStacks()) {
                        if (choiceEvent.type.test(cardStack)) {
                            cardStack.setBorder(new Border(Color.GREEN, true, 3, cardStack, 3));
                        }
                    }
                }
            }
        };
    }

    private GameEventListener onGameStateChangeListener() {
        return event -> {
            if (event instanceof GameStateChangeEvent gameStateChangeEvent) {
                for (PlayerImpl player : players) {
                    player.removeBorders();
                }
                this.gameState = gameStateChangeEvent.newState;
                drawStacks.removeBorders();
                switch (gameStateChangeEvent.newState) {
                    case NEW_PLAYER_STATE -> {
                        drawStacks.fillDrawStacks();
                        drawStacks.skipTurn.disable();

                        nextPlayer();
                    }
                    case ROLL_DICE_STATE -> {
                        drawStacks.dice.setBorder(new Border(Color.GREEN, true, 2, drawStacks.dice, 3));
                        drawStacks.dice.enable();
                        if (activePlayer.hasLandmark(0)) {
                            drawStacks.twoDice.setBorder(new Border(Color.GREEN, true, 2, drawStacks.twoDice, 3));
                            drawStacks.twoDice.enable();
                        }
                        drawStacks.repaint();
                    }
                    case BUY_CARD_STATE -> {
                        drawStacks.skipTurn.enable();
                        drawStacks.dice.disable();
                        drawStacks.dice.setBorder(null);
                        drawStacks.twoDice.disable();
                        drawStacks.twoDice.setBorder(null);
                        activePlayer.updateGameBoard(this);
                    }
                }
            }
        };
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
            var p = new PlayerImpl(i, switch (i) {
                case 0 -> Options.player1Name;
                case 1 -> Options.player2Name;
                case 2 -> Options.player3Name;
                case 3 -> Options.player4Name;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            });
            players.add(p);
            add(p.getPlayerPane());
        }
        super.activate();

        drawStacks.dice.addActionListener(e -> {
            int roll = new Random().nextInt(6) + 1;
            drawStacks.diceRoll.setDescription("Rolled: " + roll);
            event(new DiceRollEvent(roll));
        });
        drawStacks.twoDice.addActionListener(e -> {
            int roll1 = new Random().nextInt(6) + 1;
            int roll2 = new Random().nextInt(6) + 1;
            int roll = roll1 + roll2;
            drawStacks.diceRoll.setDescription("Rolled: " + roll);
            event(new DiceRollEvent(roll));
        });


        game.addEventListener(onChoiceListener());
        game.addEventListener(onDiceRollListener());
        game.addEventListener(onDragAndDropListener());
        game.addEventListener(onPlayerSwitchListener());
        game.addEventListener(onGameStateChangeListener());
        event(new GameStateChangeEvent(NEW_PLAYER_STATE));
    }

    public void nextPlayer() {
        if (activePlayer != null)
            event(new PlayerChangeEvent(players.get((activePlayer.getId() + 1) % (players.size()))));
        else event(new PlayerChangeEvent(players.get(new Random().nextInt(Options.getPlayerCount()))));
    }

    public List<PlayerImpl> getPlayer() {
        return players;
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
