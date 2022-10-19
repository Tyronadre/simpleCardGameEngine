package de.henrik.implementation;

import de.henrik.engine.Card;
import de.henrik.engine.CardStack;
import de.henrik.engine.GameBoard;

import java.awt.*;
import java.util.function.Predicate;

public class BasicCardStack extends CardStack {


    public BasicCardStack(String name, Point pos, GameBoard gameBoard) {
        super(name, TOP_CARD_TURNED, card -> true, new Dimension(200, 300), pos, 20, gameBoard);
        setTopCardDraggable(true);
        setDrawStackSizeHint(true);
    }

    public void fillStack(int amount) {
        for (int i = 0; i < amount; i++)
            addCard(new BasicCard(1));
    }
}
