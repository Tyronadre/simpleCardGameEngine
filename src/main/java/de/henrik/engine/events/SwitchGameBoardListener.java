package de.henrik.engine.events;

import de.henrik.engine.game.Game;

public interface SwitchGameBoardListener extends GameEventListener {
    void handleEvent(SwitchGameBoardEvent event);

    default void handleEvent(GameEvent event) {
        if (event instanceof SwitchGameBoardEvent switchGameBoardEvent) {
            handleEvent(switchGameBoardEvent);
        }
    }
}
