package de.henrik.engine.events;

import de.henrik.engine.base.GameComponent;

public class GameEvent {
    private final String name;
    private final GameComponent parent;

    public GameEvent(String name, GameComponent parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public GameComponent getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "GameEvent: " + name;
    }
}
