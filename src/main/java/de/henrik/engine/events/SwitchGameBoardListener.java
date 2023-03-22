package de.henrik.engine.events;

public interface SwitchGameBoardListener extends GameEventListener {
    void handleEvent(SwitchGameBoardEvent event);

    default void handleEvent(GameEvent event) {
        if (event instanceof SwitchGameBoardEvent switchGameBoardEvent) {
            handleEvent(switchGameBoardEvent);
        }
    }
}
