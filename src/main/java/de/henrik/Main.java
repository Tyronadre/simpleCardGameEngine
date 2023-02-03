package de.henrik;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.game.Board;
import de.henrik.engine.game.Game;

import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.boards.MainMenu;
import de.henrik.implementation.game.Options;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Game game = Game.game;
        Board mainMenu = new MainMenu(new GameImage("/background/mainMenu.png"));
        Board gameBoard = new GameBoard(new GameImage("/background/gameboard.png"));
        Game.registerGameBoard("MainMenu", mainMenu);
        Game.registerGameBoard("game", gameBoard);

        Options.expansion1Selected = true;

        game.start(mainMenu);
        System.out.println("Resolution: " + Options.getWidth() + "x" + Options.getHeight());

    }


}