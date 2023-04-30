package de.henrik.implementation.boards;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.game.Board;
import de.henrik.engine.game.Game;
import de.henrik.implementation.game.Options;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameBoard extends Board {

    public GameBoard() {
        super(new GameImage("/background/gameboard.jpg").getScaledInstance(Options.getWidth(), Options.getHeight()));
    }

    @Override
    protected void activate() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        var escKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '\u001B') {
                    Game.game.switchGameBoard("MainMenu");
                }
            }
        };
        addKeyListener(escKeyListener);
        super.activate();
    }
}
