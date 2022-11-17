package de.henrik.engine.game;

import de.henrik.implementation.game.Options;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    public static final Game game;
    public static final Graphics2D grapics;

    static {
        game = new Game();
        Options.init();
        grapics = (Graphics2D) game.getGraphics();
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
        gameBoard.setGraphics(g);
        repaint();
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (gameBoard != null)
            gameBoard.paint((Graphics2D) g);
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
