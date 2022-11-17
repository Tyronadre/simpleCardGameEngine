package de.henrik.engine.card;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.game.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardStackArea extends GameComponent {
    List<CardStack> cardStacks;
    final int xSpace, ySpace, maxNumberOfStack;


    public CardStackArea(int maxNumberOfStack, int xSpace, int ySpace) {
        this.cardStacks = new ArrayList<>();
        this.xSpace = xSpace;
        this.ySpace = ySpace;
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
        if (this.getHeight() == 0 || this.getWidth() == 0) {
            return;
        }
        /*
         1234 -> 135 -> 147 -> f
                 246    258
                        369
         */
        int cardWidth = 0;
        int cardHeight = 0;
        boolean finalSize = false;

        int maxRows = 1;
        for (int line = 0; line < maxRows; line++) {
            cardWidth = ((getWidth() - xSpace * cardStacks.size() - xSpace) * maxRows) / (cardStacks.size());
            cardHeight = (int) (cardWidth / (2 / (double) 3));
            if ((cardHeight * maxRows) + 2 * ySpace + maxRows * ySpace > this.getHeight()) {
                cardHeight = (getHeight() - 2 * ySpace - maxRows * ySpace) / maxRows;
                cardWidth = (int) (cardHeight * (2 / (double) 3));
                break;
            }
            if (((maxRows + 1) * cardHeight + 3 * ySpace) < getHeight()) {
                maxRows++;
            }
        }

        int row = 0;
        int col = 0;
        for (CardStack cardStack : cardStacks) {
            int posX = xSpace + col * (cardWidth + xSpace);
            int posY = ySpace + row * (cardHeight + ySpace);

            cardStack.setSize(cardWidth, cardHeight);
            cardStack.setPosition(posX, posY);


            if (++row % maxRows == 0) {
                col++;
                row = 0;
            }
        }

        paint(g);
    }

    public boolean addStack(CardStack cardStack) {
        if (cardStacks.size() == maxNumberOfStack)
            return false;
        cardStacks.add(cardStack);
        updateLayout();
        return true;
    }

    public boolean removeStack(CardStack cardStack) {
        if (!cardStacks.contains(cardStack))
            return false;
        cardStacks.remove(cardStack);
        updateLayout();
        return true;
    }

    @Override
    public void paint(GameGraphics g) {
        if (!Game.isRunning())
            return;
        g.setColor(Color.pink);
        g.getGraphics().fillRect(getX(),getY(),getWidth(),getHeight());
        g.setColor(null);
        cardStacks.forEach(cS -> {
            cS.paint(g);
            System.out.println(cS);;
        });
    }
}
