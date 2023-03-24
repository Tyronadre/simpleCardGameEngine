package testAdapter;

import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.GameStateChangeEvent;
import de.henrik.implementation.GameEvent.PlayerChangeEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.boards.MainMenu;
import de.henrik.implementation.player.PlayerImpl;

public class GameStateAdapter {
    public static void setGameState(GameState state) {
        if (state != GameState.MENU) {
            Provider.game.switchGameBoard("GameBoard");
        }
        switch (state) {
            case MENU -> Provider.game.switchGameBoard("MainMenu");
            case PAUSE -> Provider.gameEventThread.forceEvent(new GameStateChangeEvent(GameBoard.PAUSE_STATE));
            case NEW_PLAYER ->
                    Provider.gameEventThread.forceEvent(new GameStateChangeEvent(GameBoard.NEW_PLAYER_STATE));
            case ROLL_DICE -> Provider.gameEventThread.forceEvent(new GameStateChangeEvent(GameBoard.ROLL_DICE_STATE));
            case BUY_CARD -> Provider.gameEventThread.forceEvent(new GameStateChangeEvent(GameBoard.BUY_CARD_STATE));
        }
    }

    public static void setActivePlayer(Player player) {
        Provider.game.event(new PlayerChangeEvent((PlayerImpl) player));
        Provider.gameEventThread.handleNextEvent();
        Provider.gameEventThread.handleNextEvent();
    }

    public enum GameState {
        MENU, PAUSE, NEW_PLAYER, ROLL_DICE, BUY_CARD,
    }

    public static GameState getGameState() {
        if (Provider.game.getActiveGameBoard() instanceof GameBoard gameBoard) {
            return switch (gameBoard.getState()) {
                case 0 -> GameState.NEW_PLAYER;
                case 1 -> GameState.ROLL_DICE;
                case 2 -> GameState.BUY_CARD;
                case 3 -> GameState.PAUSE;
                default -> throw new IllegalStateException("Unexpected value: " + gameBoard.getState());
            };
        }
        else if (Provider.game.getActiveGameBoard() instanceof MainMenu){
            return GameState.MENU;
        }
        throw new IllegalArgumentException("Game is not in a valid state");
    }
}
