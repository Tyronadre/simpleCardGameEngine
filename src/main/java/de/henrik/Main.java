package de.henrik;

import de.henrik.engine.card.CardStackArea;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.GameBoard;
import de.henrik.engine.util.GameImage;
import de.henrik.implementation.card.EmptyCardStack;
import de.henrik.implementation.game.Options;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Game game = Game.game;
        GameBoard gameBoard = new GameBoard(GameImage.getImage("/background/gameboard.png"), Toolkit.getDefaultToolkit().getScreenSize());

//        DrawStacks drawStacks = new DrawStacks(23,new Dimension(Options.getWidth(), Options.getHeight()/3),new Point(0,Options.getHeight()/3));
//        gameBoard.add(drawStacks);

        CardStackArea cardStackArea = new CardStackArea(10, 2, 2);


        for (int i = 0; i < 100; i++) {
            cardStackArea.addStack(new EmptyCardStack("empty_stack_"+ i,GameImage.getImage("/emptyStack.png")));
        }

        cardStackArea.setSize(Options.getWidth() / 3,Options.getHeight()/3);
        gameBoard.add(cardStackArea);


        game.start(gameBoard);
        System.out.println("Resolution: " + Options.getHeight() + "," + Options.getWidth());
//        drawStacks.fillDrawStacks();

    }

}