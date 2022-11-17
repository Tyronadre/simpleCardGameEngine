package de.henrik;

import de.henrik.engine.game.Game;
import de.henrik.engine.game.GameBoard;
import de.henrik.engine.util.GameImage;
import de.henrik.implementation.game.DrawStacks;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Game game = Game.game;
        GameBoard gameBoard = new GameBoard(GameImage.getImage("/background/gameboard.png"), Toolkit.getDefaultToolkit().getScreenSize());

        DrawStacks drawStacks = new DrawStacks(23,new Dimension(Options.getWidth(), Options.getHeight()/3),new Point(0,Options.getHeight()/3));
        gameBoard.add(drawStacks);
        game.start(gameBoard);

        drawStacks.fillDrawStacks();

    }

}