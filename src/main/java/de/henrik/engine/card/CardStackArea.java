package de.henrik.engine.card;

import de.henrik.engine.base.GameComponent;

import java.util.List;

public class CardStackArea extends GameComponent {
    List<CardStack> cardStack;
    final int xSpace, ySpace;


    public CardStackArea(int maxNumberOfStack, int xSpace, int ySpace) {
        this.cardStack = cardStack;
        this.xSpace = xSpace;
        this.ySpace = ySpace;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        updateLayout();
    }

    private void updateLayout() {
    }
}
