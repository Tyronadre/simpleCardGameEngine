package de.henrik;

import de.henrik.engine.Game;
import de.henrik.engine.CardStack;
import de.henrik.engine.GameBoard;
import de.henrik.implementation.BasicCardStack;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Game game = Game.game;

        GameBoard gameBoard = new GameBoard(GameImage.getImage("/background/gameboard.png"), Toolkit.getDefaultToolkit().getScreenSize());
        BasicCardStack cardStack = new BasicCardStack("stack1", new Point(100, 100), gameBoard);
        BasicCardStack cardStack1 = new BasicCardStack("stack2", new Point(400, 100), gameBoard);
        cardStack.fillStack(10);
        cardStack1.fillStack(40);

        gameBoard.build();
        game.setGameBoard(gameBoard);

        game.start();



//        JButton removeCard = new JButton("Remove card");
//        game.add(removeCard, BorderLayout.SOUTH);
//        removeCard.addActionListener(e -> {
//            cardStack.removeCard();
//            gameBoard.repaint();
//            removeCard.setEnabled(cardStack.getStackSize() > 0);
//        });
    }
}