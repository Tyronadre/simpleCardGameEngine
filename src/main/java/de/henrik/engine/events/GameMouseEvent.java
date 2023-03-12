package de.henrik.engine.events;

import de.henrik.engine.base.GameComponent;

import java.awt.*;

public class GameMouseEvent extends GameEvent {
    public final Point location;

    public GameMouseEvent(Point location, GameComponent parent) {
        super("GameMouseEvent",parent);
        this.location = location;
    }
}
