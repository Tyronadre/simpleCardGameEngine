package de.henrik.engine.card;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.game.Game;

import java.util.ArrayList;
import java.util.List;

public class CardStackArea extends GameComponent {
    final List<CardStack> cardStacks;
    final int xSpace, ySpace, maxNumberOfStack, maxCardHeight, maxCardWidth;


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


        double cardWidth = 0;
        double cardHeight = 0;
        int biggestPossibleStack = 0;
        for (CardStack stack : cardStacks) {
            if (stack.stackMaxDrawSize > biggestPossibleStack) {
                biggestPossibleStack = stack.stackMaxDrawSize;
            }
        }


        int maxRows = 1;
        for (int line = 0; line < maxRows; line++) {
            cardWidth = (getWidth() - (cardStacks.size() + 1) * (xSpace + biggestPossibleStack * CardStack.X_CARD_OFFSET)) * maxRows/ (double) cardStacks.size();
            cardHeight = (cardWidth / (2 / 3.0));
            if ((cardHeight * maxRows) + (1 + maxRows) * (ySpace + biggestPossibleStack + CardStack.Y_CARD_OFFSET) > this.getHeight()) {
                cardHeight = (getHeight() - (maxRows + 1) * (ySpace + biggestPossibleStack * CardStack.Y_CARD_OFFSET)) / (double) maxRows;
                cardWidth = (cardHeight * (2 / 3.0));
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

        int row = 0;
        int col = 0;
        for (CardStack cardStack : cardStacks) {
            int posX = (int) (getX() + xSpace + col * (cardWidth + xSpace) + (col) * biggestPossibleStack * CardStack.X_CARD_OFFSET);
            int posY = (int) (getY() + ySpace + row * (cardHeight + ySpace) + (row) * biggestPossibleStack * CardStack.Y_CARD_OFFSET);

            cardStack.setPosition(posX, posY);
            cardStack.setCardSize((int) cardWidth, (int) cardHeight);

            if (++row % maxRows == 0) {
                col++;
                row = 0;
            }
        }
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
            if (g.getClip() == null || g.getClip().intersects(child.getClip()))
                child.paint(g.create());
        }
    }

    public List<CardStack> getStacks() {
        return cardStacks;
    }

    public void removeEmptyStacks() {
        ArrayList<CardStack> stacksToRemove = new ArrayList<>();
        for (CardStack cardStack : cardStacks) {
            if (cardStack.getStackSize() == 0) {
                stacksToRemove.add(cardStack);
            }
        }
        for (CardStack cardStack : stacksToRemove) {
            removeStack(cardStack);
        }
    }
}
