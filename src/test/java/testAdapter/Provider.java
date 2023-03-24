package testAdapter;

import TestUtil.TestGameEventThread;
import de.henrik.engine.events.SwitchGameBoardEvent;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.DiceRollEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.boards.MainMenu;
import de.henrik.implementation.game.DrawStacks;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Provider {
    public static final Game game = Game.game;
    public static final TestGameEventThread gameEventThread = new TestGameEventThread();
    private static boolean init = false;
    public static GameBoard gameBoard;
    public static MainMenu mainMenu;

    public static void init() {
        if (init) {
            gameEventThread.clearEvents();
            if (game.getActiveGameBoard() == gameBoard) {
                setMenuState();
            }
            Options.setPlayerCount(4);
            Options.player1Name = "p1";
            Options.player2Name = "p2";
            Options.player3Name = "p3";
            Options.player4Name = "p4";
            Options.drawStacks = 10;
            Options.setWidth(1920);
            Options.setHeight(1080);
            return;
        }
        init = true;

        mainMenu = new MainMenu();
        gameBoard = new GameBoard();
        Game.registerGameBoard("MainMenu", mainMenu);
        Game.registerGameBoard("GameBoard", gameBoard);
        Game.setDefaultColor(Color.WHITE);
        Game.game.setGameEventThread(gameEventThread);
        Game.game.start(mainMenu);
        Game.game.setVisible(false);
        gameBoard.getPlayers().clear();

        Options.setPlayerCount(4);
        Options.player1Name = "p1";
        Options.player2Name = "p2";
        Options.player3Name = "p3";
        Options.player4Name = "p4";
        Options.drawStacks = 10;
        Options.setWidth(1920);
        Options.setHeight(1080);
    }


    public static void setGameState() {
        game.event(new SwitchGameBoardEvent(mainMenu, gameBoard));
        gameEventThread.handleNextEvent();
        gameEventThread.handleNextEvent();
    }

    public static void setMenuState() {
        game.event(new SwitchGameBoardEvent(gameBoard, mainMenu));
        gameEventThread.handleNextEvent();
    }
}

