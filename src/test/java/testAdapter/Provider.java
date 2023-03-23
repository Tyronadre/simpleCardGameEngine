package testAdapter;

import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;

public class Provider {

    public static Game getGame() {
        return null;
    }

    /**
     * Returns the last event that was triggered
     */
    public static GameEvent getLastEvent() {
        return null;
    }
}
