package de.henrik.engine;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame{

    public static final Game game;
    public static final Graphics2D grapics ;

    static {
        game = new Game();
        grapics = (Graphics2D) game.getGraphics();

    }

    private Game() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
    }

    public void setState(){

    }

    public void setGameBoard(GameBoard gameBoard){
        this.setContentPane(gameBoard);
        revalidate();
    }

    public void start() {
        System.out.println("Game started");
    }
}
