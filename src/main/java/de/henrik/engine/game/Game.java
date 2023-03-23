package de.henrik.engine.game;

import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.events.GameEventListener;
import de.henrik.engine.events.SwitchGameBoardEvent;
import de.henrik.engine.events.SwitchGameBoardListener;
import de.henrik.implementation.game.Options;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.util.HashMap;

public class Game extends JFrame {

    public static final Game game = new Game();
    private static boolean running = false;

    private Player activePlayer;

    private static final HashMap<String, Board> gameBoards = new HashMap<>();

    private GameEventThread gameEventThread;

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

        gameEventThread = new GameEventThread();
    }

    public static void setDefaultColor(Color color) {
        GameGraphics.defaultColor = color;
    }

    public static Board getGameBoard(String gameBoard) {
        return gameBoards.get(gameBoard);
    }

    public Board getActiveGameBoard() {
        return gameBoard;
    }

    public void start(Board gameBoard) {
        System.out.println("Game started");

        addEventListener((SwitchGameBoardListener) event -> {
            event.oldBoard.deactivate();
            Game.game.gameBoard = event.newBoard;
            event.newBoard.activate();
            event.newBoard.repaint();
        });

        this.gameBoard = gameBoard;
        Graphics2D g = (Graphics2D) getGraphics().create();
        g.setClip(0, 0, getWidth(), getHeight());
        gameBoard.setGraphics(new GameGraphics(g));
        gameBoard.activate();
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
        game.event(new SwitchGameBoardEvent(getActiveGameBoard(), gameBoards.get(gameBoardName)));
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void event(GameEvent event) {
        gameEventThread.submitEvent(event);
    }

    public void addEventListener(GameEventListener gameEventListener) {
        gameEventThread.addListener(gameEventListener);
    }

    public void removeEventListener(GameEventListener gameEventListener) {
        gameEventThread.removeListener(gameEventListener);
    }

    /**
     * Removes all event listener and adds a new default SwitchGameBoardListener
     */
    public void clearEventListener() {
        gameEventThread.removeAllListener();
        gameEventThread.addListener((SwitchGameBoardListener) event -> {
            event.oldBoard.deactivate();
            Game.game.gameBoard = event.newBoard;
            clearEventListener();
            event.newBoard.activate();
        });
    }

    public void setWaitForEvent(boolean waitForEvent) {
        gameEventThread.waitForEvent(waitForEvent);
    }

    public void forceEvent(GameEvent event) {
        gameEventThread.forceEvent(event);
    }


    public void setGameEventThread(GameEventThread gameEventThread){
        this.gameEventThread.stop(true);
        this.gameEventThread = gameEventThread;
    }
}
