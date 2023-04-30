package de.henrik;

import de.henrik.engine.game.Board;
import de.henrik.engine.game.Game;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.boards.MainMenu;
import de.henrik.implementation.game.Options;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Board mainMenu = new MainMenu();
        Board gameBoard = new GameBoard();
        Game.registerGameBoard("MainMenu", mainMenu);
        Game.registerGameBoard("game", gameBoard);
        Game.setDefaultColor(Color.WHITE);

        Game.game.start(mainMenu);
        System.out.println("Resolution: " + Options.getWidth() + "x" + Options.getHeight());
    }
}