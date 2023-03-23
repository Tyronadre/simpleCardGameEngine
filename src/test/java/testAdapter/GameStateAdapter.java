package testAdapter;

import de.henrik.engine.game.Game;

public class GameStateAdapter {
    public enum GameState {
        MENU,
        PAUSE,
        NEW_PLAYER,
        ROLL_DICE,
        BUY_CARD,
    }

    public static GameState getGameState(Game game) {
        return null;
    }
}
