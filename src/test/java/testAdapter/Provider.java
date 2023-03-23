package testAdapter;

import de.henrik.engine.game.Board;
import de.henrik.engine.game.Game;
import de.henrik.implementation.GameEvent.DiceRollEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.boards.MainMenu;
import de.henrik.implementation.game.DrawStacks;
import de.henrik.implementation.game.Options;
import TestUtil.TestGameEventThread;

import java.awt.*;
import java.util.Random;

public class Provider {
    public static final Game game = Game.game;
    public static final TestGameEventThread gameEventThread = new TestGameEventThread();

    public static void init() {

        Board mainMenu = new MainMenu();
        Board gameBoard = new GameBoard() {
            @Override
            public void activate() {
                drawStacks = new DrawStacks(Options.drawStacks, new Dimension(Options.getWidth(), Options.getHeight() / 3), new Point(0, Options.getHeight() / 3));
                add(drawStacks);

                drawStacks.dice.addActionListener(e -> {
                    int roll = new Random().nextInt(6) + 1;
                    drawStacks.diceRoll.setDescription("Rolled: " + roll);
                    event(new DiceRollEvent(roll));
                });
                drawStacks.twoDice.addActionListener(e -> {
                    int roll1 = new Random().nextInt(6) + 1;
                    int roll2 = new Random().nextInt(6) + 1;
                    int roll = roll1 + roll2;
                    if (roll1 == roll2) {
                        lastRollDouble = true;
                    }
                    drawStacks.diceRoll.setDescription("Rolled: " + roll1 + " + " + roll2 + " = " + roll);
                    event(new DiceRollEvent(roll));
                });
            }
        };
        Game.registerGameBoard("MainMenu", mainMenu);
        Game.registerGameBoard("GameBoard", gameBoard);
        Options.setPlayerCount(4);
        Options.player1Name = "p1";
        Options.player2Name = "p2";
        Options.player3Name = "p3";
        Options.player4Name = "p4";
        Options.drawStacks = 10;
        Options.setWidth(1920);
        Options.setHeight(1080);
        Game.setDefaultColor(Color.WHITE);
        Game.game.setGameEventThread(gameEventThread);
        Game.game.start(mainMenu);
        Game.game.setVisible(false);
    }

    public static void setGameState() {
        Game.game.switchGameBoard("GameBoard");
        gameEventThread.handleNextEvent();

    }

    public static void setMenuState() {
        Game.game.switchGameBoard("MainMenu");
        gameEventThread.handleNextEvent();
    }
}
