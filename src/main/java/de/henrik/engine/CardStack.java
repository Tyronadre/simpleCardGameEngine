package de.henrik.engine;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;


/**
 * Funktionen
 * <ul>
 *     <li>Spezifiziert max stack size (wenn -1 dann unendlich)</li>
 *     <li>Spezifiziert wie karten gerendert werden (alle bg, oberste bg, kein bg)</li>
 *     <li>shuffel</li>
 *     <li>Events:
 *     <ul>
 *         <li>hover</li>
 *         <li>click</li>
 *         <li>drag</li>
 *     </ul></li>
 *     <li>löschen / hinzufügen von karten</li>
 *     <li>regeln für den stack (nur bestimmte karten dürfen gestackt werden)</li>
 * </ul>
 */
public abstract class CardStack {
    static final List<String> givenNames = new ArrayList<>();

    public static final int ALL_CARDS_TURNED = 0;
    public static final int TOP_CARD_TURNED = 1;
    public static final int ALL_CARDS_UNTURNED = 2;
    final String name;
    final int renderPolicy;
    final Predicate<Card> stackAllowed;
    final Dimension size;
    final Point pos;
    final int maxStackSize;
    final GameBoard gameBoard;


    final List<Card> cards;
    boolean drawStackSizeHint;
    int stackMaxDrawSize = 5;

    MouseAdapter topCardDraggableAdapter;


    public CardStack(String name, int renderPolicy, Predicate<Card> stackAllowed, Dimension size, Point pos, int maxStackSize, GameBoard gameBoard) {
        //CheckArguments
        if (givenNames.contains(name))
            throw new IllegalArgumentException("This name was already used for a CardStack");
        if (0 > renderPolicy || renderPolicy > 2)
            throw new IllegalArgumentException("This is not a render policy");
        if (stackAllowed == null)
            throw new IllegalArgumentException("StackAllowed can't be null");
        if (maxStackSize < -1)
            throw new IllegalArgumentException("MaxStackSize has to be greater than -1");
        if (gameBoard == null)
            throw new IllegalArgumentException("GameBoard can't be null");
        givenNames.add(name);

        //Init finals//
        this.name = name;
        this.renderPolicy = renderPolicy;
        this.stackAllowed = stackAllowed;
        this.size = size;
        this.pos = pos;
        this.maxStackSize = maxStackSize;
        this.drawStackSizeHint = false;
        this.gameBoard = gameBoard;

        //Init cardlist
        cards = new ArrayList<>();

        //Add to gameboard
        gameBoard.addCardStack(this);
    }

    public void setDrawStackSizeHint(boolean drawStackSizeHint) {
        this.drawStackSizeHint = drawStackSizeHint;
    }

    public boolean getDrawStackSizeHint() {
        return drawStackSizeHint;
    }

    public int getStackMaxDrawSize() {
        return stackMaxDrawSize;
    }

    public void setStackMaxDrawSize(int stackMaxDrawSize) {
        this.stackMaxDrawSize = stackMaxDrawSize;
    }

    public boolean addCard(Card card) {
        if ((maxStackSize == -1 || maxStackSize >= cards.size()) && stackAllowed.test(card)) {
            cards.add(card);
            card.setSize(size);
            card.setPos(new Point(pos));
            paintStack();
            return true;
        }
        gameBoard.repaint();
        return false;
    }

    public boolean addCards(Collection<Card> cards) {
        for (Card card : cards) {
            if (!addCard(card))
                return false;
        }
        return true;
    }

    public Card getCard() {
        return getCard(cards.size() - 1);
    }

    public Card getCard(int pos) {
        return cards.get(pos);
    }

    public Card removeCard() {
        Card card = removeCard(cards.size() - 1);
        paintStack();
        return card;
    }

    public Card removeCard(int pos) {
        return cards.remove(pos);
    }

    public void shuffel() {
        Collections.shuffle(cards);
        paintStack();
    }

    public void paintStack() {
        if (cards.size() > 0 && gameBoard.isBuild()) {
            Graphics g = gameBoard.getGraphics();
            Point cardPos = new Point(pos);
            int max = Math.min(stackMaxDrawSize, cards.size());
            switch (renderPolicy) {
                case ALL_CARDS_TURNED -> {
                    for (int i = 0; i < max && cards.size() - max + i < cards.size(); i++) {
                        getCard(cards.size() - max + i).paint(g, false, cardPos);
                        cardPos.x += 2;
                        cardPos.y += 3;
                    }
                }
                case ALL_CARDS_UNTURNED -> {
                    for (int i = 0; i < max && cards.size() - max + i < cards.size(); i++) {
                        getCard(cards.size() - max + i).paint(g, true, cardPos);
                        cardPos.x += 2;
                        cardPos.y += 3;
                    }
                }
                case TOP_CARD_TURNED -> {
                    for (int i = 0; i < max - 1 && cards.size() - max + i - 1 < cards.size(); i++) {
                        getCard(cards.size() - max + i).paint(g, false, cardPos);
                        cardPos.x += 2;
                        cardPos.y += 3;
                    }
                    getCard().paint(g, true, cardPos);
                }
            }
            if (drawStackSizeHint && max < cards.size()) {
                int x = pos.x + max * 2 + 1;
                int y = pos.y + size.height - 10 + max * 3 + 1;
                g.setColor(Color.BLACK);
                int width = 58;

                if (cards.size() < 100)
                    width = 50;
                if (cards.size() < 10)
                    width = 42;

                g.drawRoundRect(x - 1, y - 11, width, 12, 5, 5);
                g.drawString(cards.size() + " cards", x, y);
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getStackSize() {
        return cards.size();
    }

    public boolean containsPoint(Point point) {
        Point topCardPos = new Point(pos);
        int max = Math.min(stackMaxDrawSize, cards.size());
        for (int i = 0; i < max && cards.size() - max + i < cards.size(); i++) {
            topCardPos.x += 2;
            topCardPos.y += 3;
        }
        return point.x < topCardPos.x + size.width && point.x >= topCardPos.x && point.y < topCardPos.y + size.height && point.y > topCardPos.y;
    }

    public void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        gameBoard.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (containsPoint(e.getLocationOnScreen()))
                    mouseMotionListener.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (containsPoint(e.getLocationOnScreen()))
                    mouseMotionListener.mouseMoved(e);
            }
        });
    }

    private void removeMouseMotionListener(MouseMotionAdapter topCardDraggableAdapter) {
        gameBoard.removeMouseMotionListener(topCardDraggableAdapter);
    }

    public void addMouseListener(MouseListener mouseListener) {
        gameBoard.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (containsPoint(e.getLocationOnScreen()))
                    mouseListener.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (containsPoint(e.getLocationOnScreen()))
                    mouseListener.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (containsPoint(e.getLocationOnScreen()))
                    mouseListener.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (containsPoint(e.getLocationOnScreen()))
                    mouseListener.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (containsPoint(e.getLocationOnScreen()))
                    mouseListener.mouseExited(e);
            }
        });
    }

    public void removeMouseListener(MouseListener mouseListener) {
        gameBoard.removeMouseListener(mouseListener);
    }

    public void moveCardToStack(Card card) {
        final double MAX_VEL = 3.0;
        final double ACC = 0.4;
        Thread thread = new Thread(() -> {

            while (true) {


                Point lastPos = null;
                double vel;
                if (lastPos == null)
                    vel = 0.0;
                else
                    vel = Math.sqrt(Math.pow(Math.abs(card.getPos().x - lastPos.x), 2) + Math.pow(Math.abs(card.getPos().y - lastPos.y), 2));
                lastPos = card.getPos();

            }

        });
    }

    public void setTopCardDraggable(boolean b) {
        //  Init adapter for dragging
        if (topCardDraggableAdapter == null)
            topCardDraggableAdapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!gameBoard.isCardDragged() && cards.size() > 0) {
                        System.out.println(e);
                        gameBoard.setCardDragged(true);
                        Card card = removeCard();
                        boolean drawBackside = renderPolicy == ALL_CARDS_UNTURNED;
                        paintStack();
                        new Thread(() -> {
                            Point offset = new Point(card.getPos().x + MouseInfo.getPointerInfo().getLocation().x, card.getPos().y + MouseInfo.getPointerInfo().getLocation().y);
                            Point mousePos;
                            Point cardPos = new Point();
                            long lastTime = System.currentTimeMillis();
                            while (true) {
                                mousePos = MouseInfo.getPointerInfo().getLocation();
                                if (System.currentTimeMillis() - lastTime >= 1000) {
                                    lastTime = System.currentTimeMillis();
                                    gameBoard.repaint(cardPos.x - 5, cardPos.y - 5, card.getWidth() + 10, card.getHeight() + 10);
                                    cardPos.move(mousePos.x - offset.x, mousePos.y - offset.y);
                                    card.paint(gameBoard.getGraphics().create(), drawBackside, cardPos);
                                    System.out.println("Thread running");
                                }
                            }
                        }).start();
                    }
                }
            };
        if (b)
            addMouseListener(topCardDraggableAdapter);
        else
            addMouseListener(topCardDraggableAdapter);
    }

}
