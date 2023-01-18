package de.henrik.engine.game;

import de.henrik.engine.base.GameGraphics;
import de.henrik.implementation.game.Options;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Game extends JFrame {

    public static final Game game = new Game();
    private static boolean running = false;
    ;

    private static HashMap<String, Board> gameBoards = new HashMap<>();

    static {
        Options.init();
    }

    private Board gameBoard;

    private Game() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        setVisible(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public Board getActiveGameBoard() {
        return gameBoard;
    }

    public void start(Board gameBoard) {
        System.out.println("Game started");
        this.gameBoard = gameBoard;
        Graphics2D g = (Graphics2D) getGraphics().create();
        g.setClip(0, 0, getWidth(), getHeight());
        gameBoard.setGraphics(new GameGraphics(g));
        setVisible(true);
        running = true;
    }

    public static boolean isRunning() {
        return running;
    }

    @Override
    public void paint(Graphics g) {
        if (isRunning()) gameBoard.paint(new GameGraphics((Graphics2D) getGraphics()));
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public static GameGraphics getGameGraphics() {
        if (!isRunning()) return null;
        return new GameGraphics((Graphics2D) game.getGraphics());
    }

    public static void registerGameBoard(String gameBoardName, Board gameBoard) {
        if (gameBoards.containsKey(gameBoardName))
            throw new IllegalArgumentException("This gameBoard is already registered!");
        gameBoards.put(gameBoardName, gameBoard);
    }

    public void switchGameBoard(String gameBoardName) {
        if (!gameBoards.containsKey(gameBoardName))
            throw new IllegalArgumentException("This gameBoard is not registered!");
        this.gameBoard.deavtivte();
        this.gameBoard = gameBoards.get(gameBoardName);
        this.gameBoard.activate();
        repaint();

    }


}
