package de.henrik.engine;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    public static final Game game;
    public static final Graphics2D grapics;

    static {
        game = new Game();
        grapics = (Graphics2D) game.getGraphics();
    }

    private GameBoard gameBoard;

    private Game() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }

    public void setState() {

    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        Graphics2D g = (Graphics2D) getGraphics().create();
        g.setClip(0, 0, getWidth(), getHeight());
        gameBoard.setGraphics(g);
        repaint();
    }

    public void start() {
        System.out.println("Game started");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (gameBoard != null)
            gameBoard.paint((Graphics2D) g);

    }

}
