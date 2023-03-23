package de.henrik.implementation.card.stack;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.events.GameMouseListenerAdapter;
import de.henrik.engine.game.Game;
import de.henrik.implementation.GameEvent.DraggingCardEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.player.PlayerImpl;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DraggableCardStack extends BasicCardStack {
    public DraggableCardStack(String name, Card allowedCardType, int maxStackSize) {
        super(name, allowedCardType, maxStackSize);
        GameBoard gameBoard = (GameBoard) Game.game.getActiveGameBoard();
        addMouseListener(new GameMouseListenerAdapter() {
            boolean pressed = false;
            int oldRenderPolicy;

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

