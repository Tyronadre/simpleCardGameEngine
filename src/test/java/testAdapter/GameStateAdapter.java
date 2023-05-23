package testAdapter;

import de.henrik.engine.events.SwitchGameBoardEvent;
import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.GameStateChangeEvent;
import de.henrik.implementation.GameEvent.PlayerChangeEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.boards.MainMenu;
import de.henrik.implementation.player.PlayerImpl;

public class GameStateAdapter {

    /**
     * Sets the active player
     *
     * @param player the player to set as active
     */
    public static void setActivePlayer(Player player) {
        Provider.game.event(new PlayerChangeEvent((PlayerImpl) player));
        Provider.gameEventThread.handleNextEvent();
        Provider.gameEventThread.handleNextEvent();
    }

    /**
     * Sets the game state
     */
    public static void setGameState() {
        Provider.game.event(new SwitchGameBoardEvent(Provider.mainMenu, Provider.gameBoard));
        Provider.gameEventThread.handleNextEvent();
        Provider.gameEventThread.handleNextEvent();
    }

    /**
     * Game States
     */
    public enum GameState {
        MENU, PAUSE, NEW_PLAYER, ROLL_DICE, BUY_CARD,
    }

    /**
     *
     * @return the game state
     */
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
