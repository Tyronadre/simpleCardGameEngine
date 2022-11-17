package de.henrik.engine.game;

import de.henrik.engine.base.GameGraphics;
import de.henrik.implementation.game.Options;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    public static final Game game;
    private static boolean running;

    static {
        running = false;
        game = new Game();
        Options.init();
    }

    private GameBoard gameBoard;

    private Game() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        setVisible(false);
    }

    public void start(GameBoard gameBoard) {
        System.out.println("Game started");
        this.gameBoard = gameBoard;
        Graphics2D g = (Graphics2D) getGraphics().create();
        g.setClip(0, 0, getWidth(), getHeight());
//        gameBoard.setGraphics(new GameGraphics(g));
        setVisible(true);
        running = true;
    }

    public static boolean isRunning() {
        return running;
    }

    @Override
    public void paint(Graphics g) {
        if (isRunning())
            gameBoard.paint(new GameGraphics((Graphics2D) getGraphics()));
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
