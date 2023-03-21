package de.henrik.implementation.card.stack;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.components.Label;
import de.henrik.engine.components.Pane;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.events.GameMouseListenerAdapter;
import de.henrik.engine.game.Game;
import de.henrik.implementation.GameEvent.DraggingCardEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.game.Options;
import de.henrik.implementation.player.PlayerImpl;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DraggableCardStack extends BasicCardStack {
    public DraggableCardStack(String name, Card allowedCardType, int maxStackSize) {
        super(name, allowedCardType, maxStackSize);
        GameBoard gameBoard = (GameBoard) Game.game.getGameBoard();
        addMouseListener(new GameMouseListenerAdapter() {
            boolean pressed = true;
            int oldRenderPolicy;
            Pane largeCard = null;

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (largeCard == null && getCard() != null) {
                    largeCard = new Pane(getRenderPolicy() == RP_ALL_CARDS_UNTURNED ? getCard().getBackOfCard() : getCard().getFrontOfCard(),
                            Options.getWidth() / 50,
                            Options.getHeight() / 50,
                            1000,
                            1500);
                    gameBoard.add(largeCard);
                }
                largeCard.setVisible(true);
                gameBoard.addMouseListener(new GameMouseListenerAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        largeCard.setVisible(false);
                        gameBoard.removeMouseListener(this);
                    }
                });
            }


            @Override
            public void mouseReleasedAnywhere(MouseEvent e) {
                pressed = false;

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!gameBoard.isCardDragged() && getCard() != null) {
                    PlayingCard draggedCard = (PlayingCard) removeCard();
                    gameBoard.setCardDragged(draggedCard);
                    pressed = true;
                    oldRenderPolicy = getRenderPolicy();
                    DraggingCardEvent cardEventStart = new DraggingCardEvent(DraggableCardStack.this, draggedCard, (PlayerImpl) Game.game.getActivePlayer());
                    cardEventStart.startDragging = true;
                    gameBoard.event(cardEventStart);


                    //if the top card is turned we need to change the render policy temporary or the next card will be visible
                    if (oldRenderPolicy == CardStack.RP_TOP_CARD_TURNED)
                        setRenderPolicy(CardStack.RP_ALL_CARDS_UNTURNED);

                    //render one card less
                    setStackMaxDrawSize(getStackMaxDrawSize() - 1);

                    //Card movement
                    new Thread(() -> {
                        Point offset = new Point(draggedCard.getX() - MouseInfo.getPointerInfo().getLocation().x, draggedCard.getY() - MouseInfo.getPointerInfo().getLocation().y);
                        Point mousePos = e.getLocationOnScreen();
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
                            setRenderPolicy(CardStack.RP_TOP_CARD_TURNED);
                        }
                        setStackMaxDrawSize(getStackMaxDrawSize() + 1);
                        moveCardToStack(draggedCard);
                        gameBoard.setCardDragged(null);

                        DraggingCardEvent cardEventEnd = new DraggingCardEvent(DraggableCardStack.this, draggedCard, (PlayerImpl) Game.game.getActivePlayer());
                        cardEventEnd.endDragging = true;
                        cardEventEnd.pos = mousePos;
                        gameBoard.event(cardEventEnd);
                    }).start();
                }
            }
        });
    }
}

