package de.henrik.implementation.boards;

import de.henrik.engine.base.*;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.events.GameMouseListenerAdapter;
import de.henrik.engine.game.Board;
import de.henrik.engine.game.Border;
import de.henrik.implementation.GameEvent.DraggingCardEvent;
import de.henrik.implementation.card.BasicCard;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.game.DrawStacks;
import de.henrik.implementation.player.PlayerImpl;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard extends Board {
    private final List<PlayerImpl> players;
    PlayerImpl activePlayer;

    private Card cardDragged;

    DrawStacks drawStacks;

    public GameBoard(GameImage backgroundImage) {
        super(backgroundImage);
        this.players = new ArrayList<>();
        drawStacks = new DrawStacks(20, new Dimension(Options.getWidth(), Options.getHeight() / 3), new Point(0, Options.getHeight() / 3));
        add(drawStacks);
        addActivationListener(e -> {
            drawStacks.fillDrawStacks();
        });
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
        updateActivePlayer(players.get(new Random().nextInt(Options.getPlayerCount())));
    }

    private void updateActivePlayer(PlayerImpl player) {
        this.activePlayer = player;
        for (CardStack stack : drawStacks.getCardStacks()) {
            Card topCard = stack.getCard();
            if (topCard == null) {
                if (drawStacks.canFillDrawStacks()) {
                    drawStacks.fillDrawStacks();
                } else {
                    drawStacks.remove(stack);
                }
                continue;
            }
            if (((BasicCard) topCard).getCost() <= player.getCoins()) {
                stack.setBorder(new Border(Color.GREEN, true, 2, stack, 5));
                stack.addMouseListener(new GameMouseListenerAdapter() {
                    DraggingCardEvent cardEvent;
                    boolean pressed = true;
                    int oldRenderPolicy;

                    @Override
                    public void mousePressed(MouseEvent e) {

                        if (!GameBoard.this.isCardDragged() && stack.getCard() != null) {
                            cardEvent = new DraggingCardEvent(10, "Drag from draw stack", stack, (PlayingCard) stack.removeCard());
                            Card draggedCard = cardEvent.card;
                            cardEvent.startDragging = true;
                            GameBoard.this.setCardDragged(draggedCard);
                            pressed = true;
                            oldRenderPolicy = stack.getRenderPolicy();

                            //if the top card is turned we need to change the render policy temporary or the next card will be visible
                            if (oldRenderPolicy == CardStack.RP_TOP_CARD_TURNED)
                                stack.setRenderPolicy(CardStack.RP_ALL_CARDS_UNTURNED);

                            //render one card less
                            stack.setStackMaxDrawSize(stack.getStackMaxDrawSize() - 1);

                            //Card movement
                            new Thread(() -> {
                                Point offset = new Point(draggedCard.getX() - MouseInfo.getPointerInfo().getLocation().x, draggedCard.getY() - MouseInfo.getPointerInfo().getLocation().y);
                                Point mousePos;
                                long lastTime = 0L;

                                while (pressed) {
                                    if (System.currentTimeMillis() - lastTime >= 10) {
                                        lastTime = System.currentTimeMillis();

                                        mousePos = MouseInfo.getPointerInfo().getLocation();
                                        draggedCard.move(mousePos.x + offset.x, mousePos.y + offset.y);
                                        Toolkit.getDefaultToolkit().sync();
                                    }
                                }

                                //reset temp changes
                                if (oldRenderPolicy == CardStack.RP_TOP_CARD_TURNED) {
                                    stack.setRenderPolicy(CardStack.RP_TOP_CARD_TURNED);
                                }
                                stack.setStackMaxDrawSize(stack.getStackMaxDrawSize() + 1);

                                if (activePlayer.getPlayerPane().pointInside(MouseInfo.getPointerInfo().getLocation())) {
                                    stack.repaint();
                                } else {
                                    stack.moveCardToStack(cardEvent.card);
                                }
                                GameBoard.this.setCardDragged(null);
                                event(cardEvent);
                                cardEvent.endDragging = false;
                                cardEvent = null;
                                cardDragged = null;

                            }).start();
                            cardEvent.startDragging = true;
                            event(cardEvent);
                            cardEvent.startDragging = false;
                        }
                    }

                    @Override
                    public void mouseReleasedAnywhere(MouseEvent e) {
                        if (cardEvent != null) {
                            cardEvent.endDragging = true;
                            pressed = false;
                        }
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (cardEvent != null) {
                            cardEvent.pos = e.getLocationOnScreen();
                            event(cardEvent);
                        }
                    }
                });
            } else stack.setBorder(null);
        }
        this.removeGameListener();
        this.addEventListener(event -> {
            if (event instanceof DraggingCardEvent draggingCardEvent) {
                if (draggingCardEvent.startDragging) {
                    player.getPlayerPane().setBorder(new Border(Color.GREEN, true, 3, player.getPlayerPane(), 3));
                    player.getPlayerPane().repaint();
                } else if (draggingCardEvent.endDragging) {
                    player.getPlayerPane().setBorder(null);
                    if (player.getPlayerPane().pointInside(draggingCardEvent.pos)) {
                        player.addCard(draggingCardEvent.card);
                        // --- NEXT PLAYER --- //
                        updateActivePlayer(players.get((player.getId() + 1) % (players.size())));
                    }
                    player.getPlayerPane().repaint();
                }
            }
        });
        repaint();
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
