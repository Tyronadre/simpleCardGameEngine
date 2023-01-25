package de.henrik.implementation.card.stack;

import de.henrik.engine.base.Game;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.implementation.boards.GameBoard;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DraggableCardStack extends BasicCardStack {


    public DraggableCardStack(String name, Card allowedCardType, int maxStackSize) {
        super(name, allowedCardType, maxStackSize);
        initDragAdapter();
    }

    private void initDragAdapter() {
        Game.game.addMouseListener(new MouseAdapter() {
            Card draggedCard = null;
            boolean pressed = true;


            @Override
            public void mousePressed(MouseEvent e) {
                if (board.active && !((GameBoard) DraggableCardStack.this.board).isCardDragged() && DraggableCardStack.this.pointInside(e.getLocationOnScreen()) && (draggedCard = removeCard()) != null) {
                    ((GameBoard) DraggableCardStack.this.board).setCardDragged(draggedCard);
                    pressed = true;
                    int oldRenderPolicy = getRenderPolicy();

                    //if the top card is turned we need to change the render policy temporary or the next card will be visible
                    if (oldRenderPolicy == CardStack.RP_TOP_CARD_TURNED)
                        setRenderPolicy(CardStack.RP_ALL_CARDS_UNTURNED);

                    //render one card less
                    setStackMaxDrawSize(getStackMaxDrawSize() - 1);

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
                        CardStack cardStack = board.getCardStackAtPosition(MouseInfo.getPointerInfo().getLocation());


                        //reset temp changes
                        if (oldRenderPolicy == RP_TOP_CARD_TURNED) {
                            setRenderPolicy(RP_TOP_CARD_TURNED);
                        }
                        setStackMaxDrawSize(getStackMaxDrawSize() + 1);

                        if (cardStack != null && cardStack.test(draggedCard)) {
                            cardStack.moveCardToStack(draggedCard);
                            paint(g);
                        } else {
                            moveCardToStack(draggedCard);
                        }
                        ((GameBoard) DraggableCardStack.this.board).setCardDragged(null);
                    }).start();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (board.active && ((GameBoard) DraggableCardStack.this.board).isCardDragged()) {
                    pressed = false;
                }
            }
        });
    }

}
