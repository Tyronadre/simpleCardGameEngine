package de.henrik.implementation;

import de.henrik.engine.CardStack;
import de.henrik.engine.GameBoard;

import java.awt.*;

public class BasicCardStack extends CardStack {

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        g.setColor(Color.green);
        g.drawRect(getX(),getY(),getWidth(),getHeight());
    }

    public BasicCardStack(String name, Point pos, GameBoard gameBoard) {
        super(name, ALL_CARDS_TURNED, card -> true, new Dimension(200, 300), pos, 20, gameBoard);
        setTopCardDraggable(true);
        setDrawStackSizeHint(true);
    }

    public void fillStack(int amount) {
        for (int i = 0; i < amount; i++)
            addCard(new BasicCard(1));
    }
}
