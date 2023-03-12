package de.henrik.implementation.GameEvent;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Game;

public class GameStateChangeEvent extends GameEvent {
    public int newState;
    public static final int GAME_STATE_CHANGE_EVENT = 0;

    public GameStateChangeEvent(int newState) {
        super( "Game State Changed", Game.game.getActiveGameBoard());
        this.newState = newState;
    }

    @Override
    public String toString() {
        return "GameStateChangeEvent{" +
                "newState=" + newState +
                '}';
    }
}
