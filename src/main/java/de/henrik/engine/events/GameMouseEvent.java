package de.henrik.engine.events;

import de.henrik.engine.base.GameComponent;

import java.awt.*;

public class GameMouseEvent {
    private Point location;
    private GameComponent parent;
    private String name;
    private int id;
    public static final int ME_PRESSED = 0;
    public static final int ME_RELEASED = 1;
    public static final int ME_ENTERED = 2;
    public static final int ME_EXITED = 3;
    public static final int ME_DRAGGED = 4;


    public GameMouseEvent(int id, Point location, GameComponent parent, String name) {
        this.location = location;
        this.parent = parent;
        this.name = name;
        if (id < 0 || id > 4) {
            throw new IllegalArgumentException("ID needs to be in range");
        }
        this.id = id;
    }

    public Point getLocation() {
        return location;
    }

    public GameComponent getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MouseEvent{" +
                "location=" + location +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
