package de.henrik.engine.card;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.game.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardStackArea extends GameComponent {
    List<CardStack> cardStacks;
    final int xSpace, ySpace, maxNumberOfStack,maxCardHeight, maxCardWidth;


    public CardStackArea(int maxNumberOfStack, int xSpace, int ySpace, int maxCardHeight, int maxCardWidth) {
        this.cardStacks = new ArrayList<>();
        this.xSpace = xSpace;
        this.ySpace = ySpace;
        this.maxCardHeight = maxCardHeight;
        this.maxCardWidth = maxCardWidth;
        this.maxNumberOfStack = maxNumberOfStack;
    }
    public CardStackArea(int maxNumberOfStack, int xSpace, int ySpace) {
        this.cardStacks = new ArrayList<>();
        this.xSpace = xSpace;
        this.ySpace = ySpace;
        this.maxCardHeight = Integer.MAX_VALUE;
        this.maxCardWidth = Integer.MAX_VALUE;
        this.maxNumberOfStack = maxNumberOfStack;
    }

    @Override
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
        updateLayout();
    }


    /**
     * Maximize the usable space in a grid pattern.
     * This resizes the cardStacks
     */
    private void updateLayout() {
        if (this.getHeight() == 0 || this.getWidth() == 0 || cardStacks.size() == 0) {
            return;
        }

        int cardStackOffsetX = CardStack.X_CARD_OFFSET;
        int cardStackOffsetY = CardStack.Y_CARD_OFFSET;

        int cardWidth = 0;
        int cardHeight = 0;
        int cardsNumber = 0;
        //finde den größten stack:
        for (CardStack cardStack : cardStacks) {
            if (cardStack.getStackMaxDrawSize() > cardsNumber) {
                cardsNumber = cardStack.getStackMaxDrawSize();
            }
        }

        int maxRows = 1;
        for (int line = 0; line < maxRows; line++) {
            cardWidth = ((getWidth() - xSpace * (cardStacks.size() * cardStackOffsetY)) * maxRows) / (cardStacks.size());
            cardHeight = (int) (cardWidth / (2 / (double) 3));
            if ((cardHeight * maxRows) + 2 * ySpace + maxRows * ySpace + maxRows * cardStackOffsetY > this.getHeight()) {
                cardHeight = (getHeight() - ySpace * (maxRows + 1) - maxRows * cardStackOffsetY) / maxRows;
                cardWidth = (int) (cardHeight * (2 / (double) 3));
                break;
            }
            if (((maxRows + 1) * cardHeight + 3 * ySpace) < getHeight()) {
                maxRows++;
            }
        }
        if (cardWidth > maxCardWidth) {
            cardWidth = maxCardWidth;
            cardHeight = (int) (cardWidth / (2 / (double) 3));
        }
        if (cardHeight > maxCardHeight) {
            cardHeight = maxCardHeight;
            cardWidth = (int) (cardHeight * (2 / (double) 3));
        }

        boolean layout_changed = (cardStacks.size() == 0) || (cardWidth != cardStacks.get(0).getCardSize().getWidth()) || (cardHeight != cardStacks.get(0).getCardSize().getHeight());

        //Karten können über die Kante Gucken, dann müssen wir den Parent an der Stelle neu malen. Wir wollen nicht jeden stack rendern, sondern berechnen die ganze fläche die aus der area rausguckt

        int row = 0;
        int col = 0;
        for (CardStack cardStack : cardStacks) {
            int posX = getX() + xSpace + col * (cardWidth + xSpace);
            int posY = getY() + ySpace + row * (cardHeight + ySpace);

            cardStack.setPosition(posX, posY);
            cardStack.setSize(cardWidth, cardHeight);

            if (++row % maxRows == 0) {
                col++;
                row = 0;
            }
        }

//        //We don't need to do repaint everything if the layout doesn't change
//        if (layout_changed) {
//            repaint();
//        } else {
//            cardStacks.get(cardStacks.size() - 1).repaint();
//        }
//
//        Toolkit.getDefaultToolkit().sync();
    }

    public boolean addStack(CardStack cardStack) {
        if (cardStacks.size() == maxNumberOfStack) return false;
        cardStacks.add(cardStack);
        add(cardStack);
        updateLayout();
        return true;
    }

    public boolean removeStack(CardStack cardStack) {
        if (!cardStacks.contains(cardStack)) return false;
        cardStacks.remove(cardStack);
        remove(cardStack);
        updateLayout();
        return true;
    }

    @Override
    public void paintChildren(GameGraphics g) {
        if (!Game.isRunning()) return;
        for (int i = getChildren().size() - 1; i >= 0; i--) {
            GameComponent child = getChildren().get(i);
            // verringert draw timing, aber dann dürfen sich die stacks nicht überlappen, sonst siehts doof aus
             if (g.getClip() == null || g.getClip().intersects(child.getClip()))
                child.paint(g.create().setClip(child.getClip()));
            // langsamer sieht aber besser aus
//            child.paint(g.create().setClip(null));
        }
    }

    public List<CardStack> getStacks() {
        return cardStacks;
    }
}
