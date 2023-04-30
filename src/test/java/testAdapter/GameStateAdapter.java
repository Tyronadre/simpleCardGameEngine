package testAdapter;

import de.henrik.engine.game.Player;

public class GameStateAdapter {
    public static void setGameState(GameState state) {

    }

    public static void setActivePlayer(Player player) {

    }

    public enum GameState {
        MENU, PAUSE, NEW_PLAYER, ROLL_DICE, BUY_CARD,
    }

    public static GameState getGameState() {
        return null;
    }
}
