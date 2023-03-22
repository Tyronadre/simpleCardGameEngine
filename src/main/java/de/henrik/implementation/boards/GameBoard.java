package de.henrik.implementation.boards;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.components.Button;
import de.henrik.engine.components.Label;
import de.henrik.engine.components.Pane;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.events.GameEventListener;
import de.henrik.engine.events.GameMouseListenerAdapter;
import de.henrik.engine.game.Board;
import de.henrik.engine.game.Border;
import de.henrik.engine.game.Game;
import de.henrik.implementation.GameEvent.*;
import de.henrik.implementation.card.playingcard.CardType;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.card.playingcard.PlayingCardBuilder;
import de.henrik.implementation.game.DrawStacks;
import de.henrik.implementation.game.Options;
import de.henrik.implementation.player.PlayerImpl;
import de.henrik.implementation.player.PlayerPaneImpl;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameBoard extends Board {
    private final List<PlayerImpl> players;
    PlayerImpl activePlayer;

    private Card cardDragged;

    public DrawStacks drawStacks;
    int gameState;
    private boolean lastRollDouble = false;
    boolean hasRerolled = false;
    KeyListener escKeyListener;


    public static final int NEW_PLAYER_STATE = 0;
    public static final int ROLL_DICE_STATE = 1;
    public static final int BUY_CARD_STATE = 2;


    public GameBoard() {
        super(new GameImage("/background/gameboard.jpg", false).getScaledInstance(Options.getWidth(), Options.getHeight()));
        this.players = new ArrayList<>();
    }

    private GameEventListener onPlayerSwitchListener() {
        return event -> {
            if (event instanceof PlayerChangeEvent playerChangeEvent) {
                drawStacks.activePlayerLabel.setDescription("Active: " + playerChangeEvent.newPlayer.getName());
                if (activePlayer != null)
                    activePlayer.getPlayerPane().setBackground(PlayerPaneImpl.inactivePlayerBackground);
                activePlayer = playerChangeEvent.newPlayer;
                game.setActivePlayer(activePlayer);
                activePlayer.getPlayerPane().setBackground(PlayerPaneImpl.activePlayerBackground);

                event(new GameStateChangeEvent(ROLL_DICE_STATE));
            }
        };
    }

    private GameEventListener onDiceRollListener() {
        return event -> {
            if (event instanceof DiceRollEvent diceRollEvent && gameState == ROLL_DICE_STATE) {
                if (activePlayer.hasLandmark(19) && !hasRerolled) {
                    event(new GameDialogEvent(GameBoard.this, "You own \"Mercury\". You can choose to roll the dice again.", new String[]{"Roll again", "Don't roll again"}, new Runnable[]{() -> {
                        Game.game.setWaitForEvent(false);
                        hasRerolled = true;
                        event(new GameStateChangeEvent(ROLL_DICE_STATE));
                    }, () -> {
                        Game.game.setWaitForEvent(false);
                        handleCardActions(diceRollEvent.roll);
                    }}, false));
                } else {
                    handleCardActions(diceRollEvent.roll);
                }
            }
        };
    }

    private void handleCardActions(int roll) {
        drawStacks.dice.disable();
        drawStacks.dice.setBorder(null);
        drawStacks.twoDice.disable();
        drawStacks.twoDice.setBorder(null);
        for (CardType cardType : CardType.values()) {
            for (Card c : activePlayer.getCardList(cardType)) {
                PlayingCard card = (PlayingCard) c;
                card.event(new CardEvent(activePlayer, activePlayer, roll, this, c));
            }
        }

        event(new GameStateChangeEvent(BUY_CARD_STATE));
    }


    private GameEventListener onDialogListener() {
        return event -> {
            if (event instanceof GameDialogEvent gameDialogEvent) {
                Game.game.setWaitForEvent(true);
                Pane pane = new Pane(0, 0, Options.getWidth(), Options.getHeight());
                if (gameDialogEvent.isOpaque()) {
                    pane.setBackground(new GameImage(new Color(0.25f, 0.25f, 0.25f)));
                    pane.addMouseListener(new GameMouseListenerAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            e.consume();
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            e.consume();
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            e.consume();
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            e.consume();
                        }

                        @Override
                        public void mouseDragged(MouseEvent e) {
                            e.consume();
                        }

                        @Override
                        public void mouseReleasedAnywhere(MouseEvent e) {
                            e.consume();
                        }
                    });
                } else pane.setBackground(new GameImage(new Color(0f, 0f, 0f, 0.5f)));
                Label message = new Label(gameDialogEvent.getMessage());
                message.setPosition(Options.getWidth() / 5, Options.getHeight() / 3);
                message.setSize(Options.getWidth() / 5 * 3, 40);
                pane.add(message);
                for (int i = 0; i < gameDialogEvent.getAnswerOptions().length; i++) {
                    Button button = new Button(gameDialogEvent.getAnswerOptions()[i]);
                    button.setPosition(Options.getWidth() / 5, Options.getHeight() / 3 + 35 + (i + 1) * 35);
                    button.setSize(Options.getWidth() / 5 * 3 + 20, 30);
                    int finalI = i;
                    button.addActionListener(e -> {
                        remove(pane);
                        repaint();
                        gameDialogEvent.getAnswerRunnables()[finalI].run();
                    });
                    pane.add(button);
                }
                add(pane);
                repaint();
            }
        };
    }


    private GameEventListener onDragAndDropListener() {
        return new GameEventListener() {
            @Override
            public void handleEvent(GameEvent event) {
                if (event instanceof DraggingCardEvent draggingCardEvent) {
                    if (gameState != BUY_CARD_STATE) {
                        if (draggingCardEvent.endDragging)
                            ((CardStack) event.getParent()).moveCardToStack(draggingCardEvent.card);
                        return;
                    }
                    PlayerImpl player = draggingCardEvent.player;
                    if (draggingCardEvent.startDragging && draggingCardEvent.card.getCost() <= player.getCoins()) {
                        player.getPlayerPane().setBorder(new Border(Color.GREEN, true, 3, player.getPlayerPane(), 3));
                        player.getPlayerPane().repaint();
                    } else if (draggingCardEvent.endDragging) {
                        player.getPlayerPane().setBorder(null);
                        if (player.getPlayerPane().pointInside(draggingCardEvent.pos)) {
                            if (player.addCard(draggingCardEvent.card))
                                event(new GameStateChangeEvent(NEW_PLAYER_STATE));
                        } else {
                            ((CardStack) event.getParent()).moveCardToStack(draggingCardEvent.card);
                            player.updateGameBoard(GameBoard.this);
                            player.getPlayerPane().repaint();
                            player.removeCoins(0);
                        }
                    } else {
                        ((CardStack) event.getParent()).moveCardToStack(draggingCardEvent.card);
                    }
                    player.getPlayerPane().repaint();
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
                Game.game.setWaitForEvent(true);
                HashMap<PlayerImpl, List<GameComponent>> selectableComponents = new HashMap<>();
                for (PlayerImpl player : players) {
                    selectableComponents.put(player, new ArrayList<>());
                    if (choiceEvent.type.test(player.getPlayerPane())) {
                        selectableComponents.get(player).add(player.getPlayerPane());
                        player.getPlayerPane().setBorder(new Border(Color.GREEN, true, 3, 3));
                        player.getPlayerPane().repaint();
                    }
                    for (CardStack cardStack : player.getCardStacks()) {
                        if (choiceEvent.type.test(cardStack)) {
                            selectableComponents.get(player).add(cardStack);
                            cardStack.setBorder(new Border(Color.GREEN, true, 3, 3));
                            cardStack.repaint();
                        }
                    }
                }

                addMouseListener(new GameMouseListenerAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        for (PlayerImpl player : selectableComponents.keySet()) {
                            for (GameComponent component : selectableComponents.get(player)) {
                                if (component.pointInside(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
                                    selectableComponents.values().forEach(componentList -> componentList.forEach(component1 -> {
                                        component1.setBorder(null);
                                        component1.repaint();
                                    }));

                                    removeMouseListener(this);
                                    choiceEvent.selected.consume(new ChoiceSelectedEvent(component, player));
                                    return;
                                }
                            }
                        }
                    }
                });

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
                        if (activePlayer != null && activePlayer.hasAllLandmarks()) {
                            event(new GameDialogEvent(this, "You have all landmarks! You win!", new String[]{"Return to Main Menu"}, new Runnable[]{() -> {
                                Game.game.setWaitForEvent(false);
                                Game.game.switchGameBoard("MainMenu");
                            }}, false));
                            return;
                        }
                        drawStacks.fillDrawStacks();
                        drawStacks.skipTurn.disable();
                        drawStacks.diceRoll.setDescription("Rolled: 0");
                        nextPlayer();
                        System.out.println("New Player State");
                    }
                    case ROLL_DICE_STATE -> {
                        drawStacks.dice.setBorder(new Border(Color.GREEN, true, 2, drawStacks.dice, 3));
                        drawStacks.dice.enable();
                        if (activePlayer.hasLandmark(16)) {
                            drawStacks.twoDice.setBorder(new Border(Color.GREEN, true, 2, drawStacks.twoDice, 3));
                            drawStacks.twoDice.enable();
                        }
                        drawStacks.repaint();
                        System.out.println("Roll Dice State");
                    }
                    case BUY_CARD_STATE -> {
                        hasRerolled = false;
                        lastRollDouble = false;
                        drawStacks.skipTurn.enable();
                        activePlayer.updateGameBoard(this);
                        System.out.println("Buy Card State");
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
        drawStacks = new DrawStacks(Options.drawStacks, new Dimension(Options.getWidth(), Options.getHeight() / 3), new Point(0, Options.getHeight() / 3));
        add(drawStacks);

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
            for (Card card : PlayingCardBuilder.buildCardsFromCSV("/cardsBase.csv"))
                p.addCard((PlayingCard) card);
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
            if (roll1 == roll2) {
                lastRollDouble = true;
            }
            drawStacks.diceRoll.setDescription("Rolled: " + roll1 + " + " + roll2 + " = " + roll);
            event(new DiceRollEvent(roll));
        });
        escKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\u001B') {
                    event(new GameDialogEvent(GameBoard.this, "Are you sure you want to quit?", new String[]{"Yes", "No"}, new Runnable[]{() -> {
                        Game.game.setWaitForEvent(false);
                        Game.game.switchGameBoard("MainMenu");
                    }, () -> {
                        Game.game.setWaitForEvent(false);
                        repaint();
                    }}, true));
                }
            }
        };
        addKeyListener(escKeyListener);


        game.addEventListener(onChoiceListener());
        game.addEventListener(onDiceRollListener());
        game.addEventListener(onDragAndDropListener());
        game.addEventListener(onPlayerSwitchListener());
        game.addEventListener(onGameStateChangeListener());
        game.addEventListener(onDialogListener());
        event(new GameStateChangeEvent(NEW_PLAYER_STATE));
    }

    @Override
    protected void deactivate() {
        super.deactivate();
        game.removeEventListener(onChoiceListener());
        game.removeEventListener(onDiceRollListener());
        game.removeEventListener(onDragAndDropListener());
        game.removeEventListener(onPlayerSwitchListener());
        game.removeEventListener(onGameStateChangeListener());
        game.removeEventListener(onDialogListener());
        activePlayer = null;
        cardDragged = null;
        remove(drawStacks);
        removeKeyListener(escKeyListener);
        drawStacks = null;
        for (PlayerImpl player : players) {
            remove(player.getPlayerPane());
        }
        players.clear();
        hasRerolled = false;
        lastRollDouble = false;
    }


    public void nextPlayer() {
        if (activePlayer != null) if (lastRollDouble) {
            lastRollDouble = false;
            event(new PlayerChangeEvent(activePlayer));
        } else event(new PlayerChangeEvent(players.get((activePlayer.getId() + 1) % (players.size()))));
        else event(new PlayerChangeEvent(players.get(new Random().nextInt(Options.getPlayerCount()))));
    }

    public List<PlayerImpl> getPlayers() {
        return players;
    }


}
