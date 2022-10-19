package de.henrik.engine;

import de.henrik.engine.base.Component;

import javax.swing.*;
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
public abstract class CardStack extends Component {
    static final List<String> givenNames = new ArrayList<>();

    private static final int X_CARD_OFFSET = 3;
    private static final int Y_CARD_OFFSET = 4;

    /**
     * Render all card front
     */
    public static final int ALL_CARDS_TURNED = 0;

    /**
     * Render first card front, all other back
     */
    public static final int TOP_CARD_TURNED = 1;

    /**
     * Render all cards back
     */
    public static final int ALL_CARDS_UNTURNED = 2;
    final Dimension cardSize;
    final String name;
    int renderPolicy;
    final Predicate<Card> stackAllowed;
    final int maxStackSize;
    GameBoard gameBoard;


    final List<Card> cards;
    boolean drawStackSizeHint;
    int stackMaxDrawSize = 5;

    MouseAdapter topCardDraggableAdapter;


    /**
     * Creates a new CardStack. It will be added to the gameBoard.
     *
     * @param name         The unique name of this stack.
     * @param renderPolicy the renderPolicy. On of {@link CardStack#ALL_CARDS_TURNED}, {@link CardStack#TOP_CARD_TURNED}, {@link CardStack#ALL_CARDS_UNTURNED}
     * @param stackAllowed if a new card can be added to this stack. If null, all cards will be allowed.
     * @param size         the size of the cards of this stack. The actual size of this stack changes {@link CardStack#getSize()}
     * @param pos          the pos of this stack
     * @param maxStackSize how many cards can be on this stack at most, or -1 if there can be infinite
     * @param gameBoard    The gameBoard of this cardStack
     * @throws IllegalArgumentException if one of the arguments is wrong
     */
    public CardStack(String name, int renderPolicy, Predicate<Card> stackAllowed, Dimension size, Point pos, int maxStackSize, GameBoard gameBoard) {
        super(pos, size);
        //CheckArguments
        if (givenNames.contains(name))
            throw new IllegalArgumentException("This name was already used for a CardStack");
        if (0 > renderPolicy || renderPolicy > 2)
            throw new IllegalArgumentException("This is not a render policy");
        if (maxStackSize < -1)
            throw new IllegalArgumentException("MaxStackSize has to be greater than -1");
        givenNames.add(name);

        //Init finals//
        this.name = name;
        this.renderPolicy = renderPolicy;
        this.stackAllowed = Objects.requireNonNullElseGet(stackAllowed, () -> card -> true);
        this.cardSize = size;
        this.maxStackSize = maxStackSize;
        this.drawStackSizeHint = false;
        this.gameBoard = gameBoard;

        //Init cardlist
        cards = new ArrayList<>();

        //Add to gameboard
        gameBoard.addCardStack(this);

        //Init draggable cards
        topCardDraggableAdapter = new MouseAdapter() {
            Card card;
            Boolean pressed;

            @Override
            public void mousePressed(MouseEvent e) {
                if (!pointInside(e.getLocationOnScreen()))
                    return;
                if (!gameBoard.getCardStackAt(e.getLocationOnScreen()).equals(CardStack.this))
                    return;
                pressed = true;
                int oldRenderPolicy = getRenderPolicy();

                card = removeCard();
                if (oldRenderPolicy == TOP_CARD_TURNED) {
                    setRenderPolicy(ALL_CARDS_UNTURNED);
                }
                gameBoard.repaint(getX(), getY(), getWidth() + X_CARD_OFFSET, getHeight() + Y_CARD_OFFSET);
                new Thread(() -> {
                    if (gameBoard.isCardDragged())
                        return;
                    Point offset = new Point(card.getX() - MouseInfo.getPointerInfo().getLocation().x, card.getY() - MouseInfo.getPointerInfo().getLocation().y);
                    Point mousePos;
                    Point newPos = new Point(card.getX(), card.getY());
                    boolean movementUp;
                    boolean movementLeft;
                    card.paint();
                    while (pressed) {
                        mousePos = MouseInfo.getPointerInfo().getLocation();
                        newPos.x = mousePos.x + offset.x;
                        newPos.y = mousePos.y + offset.y;

                        movementLeft = newPos.x - card.getX() <= 0;
                        movementUp = newPos.y - card.getY() <= 0;
                        gameBoard.repaint(
                                card.getX(),
                                movementUp ? newPos.y + card.getHeight() : card.getY(),
                                card.getWidth(),
                                Math.min(card.getHeight(), Math.abs(newPos.y - card.getY())));
                        gameBoard.repaint(
                                movementLeft ? newPos.x + card.getWidth() : card.getX(),
                                card.getY(),
                                Math.min(card.getWidth(), Math.abs(newPos.x - card.getX())),
                                card.getHeight()
                        );

                        card.setPosition(mousePos.x + offset.x, mousePos.y + offset.y);
                        card.paint();
                        Toolkit.getDefaultToolkit().sync();
                    }
                    CardStack cardStack = gameBoard.getCardStackAt(MouseInfo.getPointerInfo().getLocation());
                    int x = card.getX();
                    int y = card.getY();
                    int w = card.getWidth();
                    int h = card.getHeight();

                    if (cardStack != null && cardStack.addCard(card)) {
                        gameBoard.repaint(x,y, w, h);
                        cardStack.paint();
                    } else {
                        addCard(card);
                        gameBoard.repaint(x,y, w, h);
                    }
                    if (oldRenderPolicy == TOP_CARD_TURNED) {
                        setRenderPolicy(TOP_CARD_TURNED);
                        paint();
                    }


                    gameBoard.setCardDragged(false);
                }).start();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
            }
        };
    }



    private int getRenderPolicy() {
        return renderPolicy;
    }

    private void setRenderPolicy(int policy) {
        renderPolicy = policy;
        paint();
    }

    /**
     * @param drawStackSizeHint If {@code TRUE} there will be a hint how many cards are in the stack, if there are more cards than {@link CardStack#getStackMaxDrawSize()}
     */
    public void setDrawStackSizeHint(boolean drawStackSizeHint) {
        this.drawStackSizeHint = drawStackSizeHint;
    }

    /**
     * @param stackMaxDrawSize the maximal amount of cards that will be painted.
     */
    public void setStackMaxDrawSize(int stackMaxDrawSize) {
        this.stackMaxDrawSize = stackMaxDrawSize;
    }


    /**
     * @return the maximum amount of cards this stack will draw. By default, this is 5.
     */
    public int getStackMaxDrawSize() {
        return stackMaxDrawSize;
    }

    /**
     * Test if a card can be added to the stack.
     * @param card The card to test
     * @return {@code TRUE} if the card can be added, {@code FALSE} otherwise.
     */
    public boolean test(Card card) {
        return (maxStackSize == -1 || maxStackSize >= cards.size()) && stackAllowed.test(card);
    }

    /**
     * Adds one card to the stack. This card will be added at the specified position.
     * The cards position and size will be set to this stack default position and size. The position may change within {@link CardStack#paint()}
     * If the stack is full or the card is not allowed by the {@link CardStack#stackAllowed}-Predicate it will not be added.
     * This method uses the {@link CardStack#test(Card)} method to test if the card can be added
     *
     * @param card The card to be added
     * @param pos  The position of the card in the stack
     * @return {@code TRUE} if the card was added, {@code FALSE} otherwise
     */
    public boolean addCard(Card card, int pos) {
        if (test(card)) {
            cards.add(pos, card);
            add(card);
            card.setSize(cardSize);
            resize();
            return true;
        }
        return false;
    }

    private void resize() {
        setSize(getWidth() , getHeight());
    }


    /**
     * Adds one card to the stack. This card will be at the top of the stack. For more information see {@link CardStack#addCard(Card, int)}.
     *
     * @param card The card to be added
     * @return {@code TRUE} if the card was added, {@code FALSE} otherwise
     * @see CardStack#addCard(Card, int)
     */
    public boolean addCard(Card card) {
        return addCard(card, cards.size());
    }

    /**
     * Adds as many cards as possible to the stack. For more information see {@link CardStack#addCard(Card, int)}.
     *
     * @param cards cards to add
     * @return {@code TRUE} if all cards were added, {@code FALSE} otherwise
     * @see CardStack#addCard(Card)
     * @see CardStack#addCard(Card, int)
     */
    public boolean addCards(Collection<Card> cards) {
        for (Card card : cards) {
            if (!addCard(card))
                return false;
        }
        return true;
    }

    /**
     * Gets the top card of the stack, or null if the stack is empty
     *
     * @return The top card of the stack
     * @see CardStack#getCard(int)
     */
    public Card getCard() {
        return getCard(cards.size() - 1);
    }

    /**
     * Returns the card at the specified position of the stack, or null if the position is not in the stack
     *
     * @param pos the position of the card
     * @return the card
     */
    public Card getCard(int pos) {
        if (pos < 0 || pos >= cards.size())
            return null;
        return cards.get(pos);
    }

    /**
     * Removes the top card of the stack. Does nothing if the stack is empty
     *
     * @return the removed card, or null if the stack is empty
     * @see CardStack#removeCard(int)
     */
    public Card removeCard() {
        return removeCard(cards.size() - 1);
    }

    /**
     * Removes the card at the specified position of the stack, or null if the position is not in the stack.
     * Also removes this card as a child of this {@link CardStack} and repaints the stack.
     *
     * @param pos the position of the card
     * @return the card
     */
    public Card removeCard(int pos) {
        if (pos < 0 || pos >= cards.size())
            return null;
        Card card = cards.remove(pos);
        remove(card);
        resize();
        return card;
    }

    /**
     * Shuffels and repaints this stack.
     */
    public void shuffel() {
        Collections.shuffle(cards);
        paint();
    }

    /**
     * Paints this cardStack if the associated {@link GameBoard} has been build.
     * If a card has to be painted, it will be moved to the position and then be painted.
     * <p>
     * This uses the {@link Graphics} from {@link GameBoard}
     */
    @Override
    public void paint() {
        // TODO: 19.10.2022 Test with different resolutions
        //set location and face of cards and then paint it
        if (cards.size() > 0) {
            Point cardPos = getPosition();
            int max = Math.min(stackMaxDrawSize, cards.size());
            switch (renderPolicy) {
                case ALL_CARDS_TURNED -> {
                    for (int i = 0; i < max && cards.size() - max + i < cards.size(); i++) {
                        Card card = getCard(cards.size() - max + i);
                        card.setPosition(cardPos);
                        card.setPaintFront(true);
                        card.paint();
                        cardPos.x += X_CARD_OFFSET;
                        cardPos.y += Y_CARD_OFFSET;
                    }
                }
                case ALL_CARDS_UNTURNED -> {
                    for (int i = 0; i < max && cards.size() - max + i < cards.size(); i++) {
                        Card card = getCard(cards.size() - max + i);
                        card.setPosition(cardPos);
                        card.setPaintFront(false);
                        card.paint();
                        cardPos.x += X_CARD_OFFSET;
                        cardPos.y += Y_CARD_OFFSET;
                    }
                }
                case TOP_CARD_TURNED -> {
                    for (int i = 0; i < max - 1 && cards.size() - max + i - 1 < cards.size(); i++) {
                        Card card = getCard(cards.size() - max + i);
                        card.setPosition(cardPos);
                        card.setPaintFront(false);
                        card.paint();
                        cardPos.x += X_CARD_OFFSET;
                        cardPos.y += Y_CARD_OFFSET;
                    }
                    Card card = getCard();
                    card.setPosition(cardPos);
                    card.setPaintFront(true);
                    card.paint();
                }
            }
            //Paint the hint if we want it with some magic numbers.
            if (drawStackSizeHint && max < cards.size()) {

                Point pos = getPosition();
                int x = pos.x + max * (X_CARD_OFFSET - 1) + 8;
                int y = pos.y + getHeight() - 7 ;

                System.out.println("painting hint at " + pos);
                g.setColor(Color.BLACK);
                int width = 58;

                if (cards.size() < 100)
                    width = 50;
                if (cards.size() < 10)
                    width = 42;

                g.drawRoundRect(x-1, y-11, width, 12, 5, 5);
                g.drawString(cards.size() + " cards", x, y);
            }
        }
    }


    /**
     * @return the unique name of this stack
     */
    public String getName() {
        return name;
    }

    /**
     * @return how many cards are in the stack
     */
    public int getStackSize() {
        return cards.size();
    }

    @Override
    public int getWidth() {
        return cardSize.width + X_CARD_OFFSET * (Math.min(cards.size(), stackMaxDrawSize) - 1);
    }

    @Override
    public int getHeight() {
        return cardSize.height + (Y_CARD_OFFSET * Math.min(cards.size(), stackMaxDrawSize) - 1);
    }

    /**
     * @return the dimension of one card
     */
    public Dimension getCardSize() {
        return cardSize;
    }

    public void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        // TODO: 19.10.2022 addMouseMotionListener
//        gameBoard.addMouseMotionListener(new MouseMotionListener() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                if (contains(e.getLocationOnScreen()))
//                    mouseMotionListener.mouseDragged(e);
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                if (contains(e.getLocationOnScreen()))
//                    mouseMotionListener.mouseMoved(e);
//            }
//        });
    }

    private void removeMouseMotionListener(MouseMotionAdapter topCardDraggableAdapter) {
        // TODO: 19.10.2022 removeMouseMotionListener
//        gameBoard.removeMouseMotionListener(topCardDraggableAdapter);
    }

    public void addMouseListener(MouseListener mouseListener) {
        // TODO: 19.10.2022 addMouseListener
    }

    public void removeMouseListener(MouseListener mouseListener) {
        // TODO: 19.10.2022 removeMouseListener
//        gameBoard.removeMouseListener(mouseListener);
    }

    public void setTopCardDraggable(boolean b) {
        // TODO: 19.10.2022 setTopCardDraggable
        //  Init adapter for dragging
        if (b)
            gameBoard.addMouseListener(topCardDraggableAdapter);
        else
            gameBoard.removeMouseListener(topCardDraggableAdapter);

    }

    @Override
    public String toString() {
        return "Stack{" +
                "Name=" + name +
                ", cards= (" + cards.size() + "/" + maxStackSize +")"+
                ", pos=" + getPosition() +
                ", size=" + getSize() +
                '}';
    }
}
