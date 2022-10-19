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
        gameBoard.setGraphics((Graphics2D) getGraphics().create());
        repaint();
    }

    public void start() {
        System.out.println("Game started");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (gameBoard != null)
            gameBoard.paint();

    }

    @Override
    public void repaint(long time, int x, int y, int width, int height) {
        super.repaint(time, x, y, width, height);
    }

    //
//    @Override
//    public void paintComponents(Graphics g) {
////        System.out.println("paintComponents");
//
//        super.paintComponents(g);
//    }
//
//    @Override
//    public void paintAll(Graphics g) {
//        System.out.println("paintAll");
//        super.paintAll(g);
//    }
//
//    @Override
//    public void repaint(long tm) {
////        System.out.println("repaint1");
//        super.repaint(tm);
//    }
//
//    @Override
//    public void repaint(long tm, int x, int y, int width, int height) {
////        System.out.println("repaint3");
//        super.repaint(tm, x, y, width, height);
//    }
//
//    @Override
//    public void repaint() {
////        System.out.println("repaint4");
//        super.repaint();
//    }
//
//    @Override
//    public void repaint(int x, int y, int width, int height) {
////        System.out.println("repaint5");
//        super.repaint(x, y, width, height);
//    }
}
