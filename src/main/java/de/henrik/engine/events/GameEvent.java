package de.henrik.engine.events;

import de.henrik.engine.base.GameComponent;

public class GameEvent {
    private int id;
    private String name;
    private GameComponent parent;

    public GameEvent(int id, String name, GameComponent parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GameComponent getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "GameEvent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }
}
