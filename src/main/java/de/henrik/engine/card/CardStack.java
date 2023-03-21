package de.henrik.engine.card;

import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.base.GameComponent;
import de.henrik.engine.game.Game;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
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
public abstract class CardStack extends GameComponent {
    protected static final int X_CARD_OFFSET = 3;
    protected static final int Y_CARD_OFFSET = 4;

    /**
     * Render all card front
     */
    public static final int RP_ALL_CARDS_TURNED = 0;

    /**
     * Render first card front, all other back
     */
    public static final int RP_TOP_CARD_TURNED = 1;

    /**
     * Render all cards back
     */
    public static final int RP_ALL_CARDS_UNTURNED = 2;
    final String name;
    int renderPolicy;
    final Predicate<Card> stackAllowed;
    final int maxStackSize;

    final List<Card> cards;
    boolean drawStackSizeHint;
    int stackMaxDrawSize = 5;



    /**
     * Creates a new CardStack.
     *
     * @param name         The unique name of this stack.
     * @param renderPolicy the renderPolicy. On of {@link CardStack#RP_ALL_CARDS_TURNED}, {@link CardStack#RP_TOP_CARD_TURNED}, {@link CardStack#RP_ALL_CARDS_UNTURNED}
     * @param stackAllowed if a new card can be added to this stack. If null, all cards will be allowed.
     * @param size         the size of the cards of this stack. The actual size of this stack changes {@link CardStack#getSize()}
     * @param pos          the pos of this stack
     * @param maxStackSize how many cards can be on this stack at most, or -1 if there can be infinite
     * @throws IllegalArgumentException if one of the arguments is wrong
     */
    public CardStack(String name, int renderPolicy, Predicate<Card> stackAllowed, Dimension size, Point pos, int maxStackSize) {
        super(pos, size);
        //CheckArguments
        // TODO: 15.11.2022 funktioniert aktuell nicht mit stacks die sich wiederholen bei drawstacks
//        if (givenNames.contains(name))
//            throw new IllegalArgumentException("This name was already used for a CardStack");
        if (0 > renderPolicy || renderPolicy > 2)
            throw new IllegalArgumentException("This is not a render policy");
        if (maxStackSize < -1)
            throw new IllegalArgumentException("MaxStackSize has to be greater than -1");

        //Init cardlist
        cards = new ArrayList<>();

        //Init finals//
        this.name = name;
        this.renderPolicy = renderPolicy;
        this.stackAllowed = Objects.requireNonNullElseGet(stackAllowed, () -> card -> true);
        setSize(size);
        this.maxStackSize = maxStackSize;
        this.drawStackSizeHint = false;


//        //Init draggable cards

    }


    /**
     * Creates a new CardStack. Dimension and Size will be 0
     *
     * @param name         The unique name of this stack.
     * @param renderPolicy the renderPolicy. On of {@link CardStack#RP_ALL_CARDS_TURNED}, {@link CardStack#RP_TOP_CARD_TURNED}, {@link CardStack#RP_ALL_CARDS_UNTURNED}
     * @param stackAllowed if a new card can be added to this stack. If null, all cards will be allowed.
     * @param maxStackSize how many cards can be on this stack at most, or -1 if there can be infinite
     * @throws IllegalArgumentException if one of the arguments is wrong
     */
    public CardStack(String name, int renderPolicy, Predicate<Card> stackAllowed, int maxStackSize) {
        this(name, renderPolicy, stackAllowed, new Dimension(0, 0), new Point(0, 0), maxStackSize);
    }

    /**
     * Moves a card from any position to this stack. The affected area and this stack will be redrawn
     *
     * @param card The Card to move
     */
    public void moveCardToStack(Card card) {
        Rectangle rec = card.getClip();
        addCard(card);
        Game.game.getGameBoard().repaint(rec);
        repaint();
    }


    /**
     * @return the applied render policy
     */
    public int getRenderPolicy() {
        return renderPolicy;
    }

    /**
     * Changes the render policy and redraws this stack. Has to be one of
     * <ul>
     * <li>{@link CardStack#RP_ALL_CARDS_TURNED}</li>
     * <li>{@link CardStack#RP_TOP_CARD_TURNED}</li>
     * <li>{@link CardStack#RP_ALL_CARDS_UNTURNED}</li>
     * </ul>
     *
     * @param policy the new Policy
     */
    public void setRenderPolicy(int policy) {
        if (policy < 0 || policy > 2)
            throw new IllegalArgumentException("This is not a valid render policy.");
        renderPolicy = policy;
        paint(g);
    }

    /**
     * @param drawStackSizeHint If {@code TRUE} there will be a hint at the bottom left how many cards are in the stack, if there are more cards than {@link CardStack#getStackMaxDrawSize()}
     */
    public void setDrawStackSizeHint(boolean drawStackSizeHint) {
        this.drawStackSizeHint = drawStackSizeHint;
    }

    /**
     * @param stackMaxDrawSize the maximal amount of cards that will be painted.
     */
    public void setStackMaxDrawSize(int stackMaxDrawSize) {
        if (stackMaxDrawSize > 0)
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
     *
     * @param card The card to test
     * @return {@code TRUE} if the card can be added, {@code FALSE} otherwise.
     */
    public boolean test(Card card) {
        return (maxStackSize == -1 || maxStackSize >= cards.size()) && stackAllowed.test(card);
    }

    /**
     * Adds one card to the stack. This card will be added at the specified position.
     * The cards position and size will be set to this stack default position and size. The position may change within {@link CardStack#paint(GameGraphics)}
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
            card.setSize(getCardSize());
            repaint();
            return true;
        }
        return false;

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
        Rectangle clip = getClip();
        Card card = cards.remove(pos);
        repaint(clip);
        return card;
    }

    /**
     * Shuffels and repaints this stack.
     */
    public void shuffel() {
        Collections.shuffle(cards);
        paint(g);
    }

    @Override
    public void paintChildren(GameGraphics g) {
        if (cards.size() > 0) {
            Point cardPos = getPosition();
            int max = Math.min(stackMaxDrawSize, cards.size());
            switch (renderPolicy) {
                case RP_ALL_CARDS_TURNED -> {
                    for (int i = 0; i < max && cards.size() - max + i < cards.size(); i++) {
                        Card card = getCard(cards.size() - max + i);
                        card.setPosition(cardPos);
                        card.setPaintFront(true);
                        card.paint(g.create());
                        cardPos.x += X_CARD_OFFSET;
                        cardPos.y += Y_CARD_OFFSET;
                    }
                }
                case RP_ALL_CARDS_UNTURNED -> {
                    for (int i = 0; i < max && cards.size() - max + i < cards.size(); i++) {
                        Card card = getCard(cards.size() - max + i);
                        card.setPosition(cardPos);
                        card.setPaintFront(false);
                        card.paint(g.create().setClip(card.getClip()));
                        cardPos.x += X_CARD_OFFSET;
                        cardPos.y += Y_CARD_OFFSET;
                    }
                }
                case RP_TOP_CARD_TURNED -> {
                    for (int i = 0; i < max - 1 && cards.size() - max + i - 1 < cards.size(); i++) {
                        Card card = getCard(cards.size() - max + i);
                        card.setPosition(cardPos);
                        card.setPaintFront(false);
                        card.paint(g.create().setClip(card.getClip()));
                        cardPos.x += X_CARD_OFFSET;
                        cardPos.y += Y_CARD_OFFSET;
                    }
                    Card card = getCard();
                    card.setPosition(cardPos);
                    card.setPaintFront(true);
                    card.paint(g.create().setClip(card.getClip()));
                }
            }
            //Paint the hint (if we want it) with some magic numbers.
            if (drawStackSizeHint && max < cards.size()) {

                Point pos = getPosition();
                int x = pos.x + max * (X_CARD_OFFSET - 1) + 8;
                int y = pos.y + getHeight() - 7;

                g.setColor(GameGraphics.defaultColor);
                int width = 58;
                if (cards.size() < 100)
                    width = 50;
                if (cards.size() < 10)
                    width = 42;

                g.setClip(null);
                g.getGraphics().drawRoundRect(x - 1, y - 11, width, 12, 5, 5);
                g.getGraphics().drawString(cards.size() + " cards", x, y);
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
        return super.getWidth() + X_CARD_OFFSET * Math.max(0, Math.min(cards.size(), stackMaxDrawSize) - 1);
    }

    @Override
    public int getHeight() {
        return super.getHeight() + (Y_CARD_OFFSET * Math.max(0, Math.min(cards.size(), stackMaxDrawSize) - 1));
    }

    /**
     * @return the dimension of one card
     */
    public Dimension getCardSize() {
        return new Dimension(super.getWidth(), super.getHeight());
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


    public void removeMouseListener(MouseListener mouseListener) {
        // TODO: 19.10.2022 removeMouseListener
//        gameBoard.removeMouseListener(mouseListener);
    }

    public void setTopCardDraggable(boolean b) {
        // TODO: 19.10.2022 setTopCardDraggable
        //  Init adapter for dragging
//        if (b)
//            gameBoard.addMouseListener(topCardDraggableAdapter);
//        else
//            gameBoard.removeMouseListener(topCardDraggableAdapter);

    }

    @Override
    public String toString() {
        return "Stack{" +
                "Name=" + name +
                ", cards= (" + cards.size() + "/" + maxStackSize + ")" +
                ", pos=" + getPosition() +
                ", size=" + getSize() +
                '}';
    }

    public Collection<? extends Card> getCards() {
        return cards;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        cards.forEach(card -> card.setSize(width, height));
    }
}
