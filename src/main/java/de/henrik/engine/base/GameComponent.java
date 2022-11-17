package de.henrik.engine.base;

import de.henrik.engine.game.Game;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GameComponent {
    private int x, y, width, height;
    protected GameGraphics g;
    protected GameComponent parent;
    private final List<GameComponent> children;


    public GameComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        children = new ArrayList<>();
    }

    public GameComponent(Point pos, Dimension size) {
        this(pos.x, pos.y, size.width, size.height);
    }

    public GameComponent() {
        children = new ArrayList<>();
    }


    /**
     * {@link GameComponent#setGraphics(GameGraphics) } has to be called before any paint call
     */
    abstract public void paint(GameGraphics g);


    public void paintChildren(GameGraphics g) {
        for (GameComponent child : children) {
            if (g.getClip() == null || g.getClip().intersects(child.getClip())) {
                child.paint(g.create());
            }
        }
    }

    /**
     * This method sets a clip of the given size, calls paint with this graphics, and then calls
     * the repaint method of all children that were intersected in the area that has to be repainted.
     * This Method ensures that x and y are within this component, and width and height are at least 1
     *
     * @param x      x
     * @param y      y
     * @param width  width
     * @param height height
     */
    public void repaint(int x, int y, int width, int height) {
        if (!Game.isRunning())
            return;
        g = Game.getGameGraphics();
        x = Math.min(x, Options.getWidth());
        y = Math.min(y, Options.getHeight());
        width = Math.min(width, Options.getWidth() - x);
        height = Math.min(height, Options.getHeight() - y);
        if (width < 0 || height < 0)
            return;
        paint(g.create().setClip(x, y, width, height));
        paintChildren(g.create().setClip(x, y, width, height));
    }

    /**
     * @param intersection part that need repainting
     * @see GameComponent#repaint(int, int, int, int)
     */
    public void repaint(Rectangle intersection) {
        repaint( intersection.x, intersection.y, intersection.width, intersection.height);
    }

    public void repaint() {
        repaint( getClip());
    }

    /**
     * @return a rectangle of this component, specified by {@link GameComponent#getX()}, {@link GameComponent#getY()}, {@link GameComponent#getWidth()}, {@link GameComponent#getHeight()}.
     */
    public Rectangle getClip() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }


    /**
     * Changes the size of this component
     *
     * @param width  the new width
     * @param height the new height
     */
    public void setSize(int width, int height) {
        setHeight(height);
        setWidth(width);
    }

    /**
     * @param size the new size
     * @see GameComponent#setSize(int, int)
     */
    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }

    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }


    /**
     * Changes the position of this Component, but does not paint it
     *
     * @param x new x
     * @param y new y
     * @see GameComponent#move(int, int)
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Changes the position of this Component, but does not paint it
     *
     * @param pos newPos
     * @see GameComponent#setPosition(int, int)
     */
    public void setPosition(Point pos) {
        setPosition(pos.x, pos.y);
    }

    /**
     * Changes the position of this Component and repaints the affected area.
     *
     * @param x new x
     * @param y new y
     * @see GameComponent#setPosition(int, int)
     */
    public void move(int x, int y) {
        if (x == getX() && y == getY())
            return;
        boolean movementLeft = x - getX() <= 0;
        boolean movementUp = y - getY() <= 0;

        int oldx = getX();
        int oldy = getY();
        setPosition(x, y);


        //Repaint parent. (if no parent just this component)
        GameComponent p = parent;
        if (p == null)
            p = this;
        p.repaint(
                oldx,
                movementUp ? getY() + getHeight() : oldy,
                getWidth(),
                Math.min(getHeight(), Math.abs(getY() - oldy)));
        p.repaint(
                movementLeft ? getX() + getWidth() : oldx,
                oldy,
                Math.min(getWidth(), Math.abs(getX() - oldx)),
                getHeight());
        paint(g);
    }

    /**
     * Changes the position of this Component and repaints the affected area.
     *
     * @param pos newPos
     * @see GameComponent#move(int, int)
     */
    public void move(Point pos) {
        move(pos.x, pos.y);
    }

    public Point getPosition() {
        return new Point(getX(), getY());
    }


    /**
     * Sets the Graphics2D for this component and all children
     *
     * @param g the new Graphics2D
     */
    public void setGraphics(GameGraphics g) {
        g.getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.g = g;
        for (GameComponent child : children) {
            child.setGraphics(g);
        }
    }

    public void add(GameComponent component) {
        children.add(component);
        component.parent = this;
    }

    public void remove(GameComponent component) {
        children.remove(component);
        component.parent = null;
    }

    /**
     * Checks if a given point is inside the stack. The size of the stack is a rectangle that encumbers all cards. It changes with the amount of cards displayed.
     *
     * @param point the point to check
     * @return {@code TRUE} if the point is inside the stacks' area, otherwise {@code FALSE}
     */
    public boolean pointInside(Point point) {
        return point.x >= getX() && point.x <= getX() + getWidth() && point.y >= getY() && point.y <= getY() + getHeight();
    }

    public List<GameComponent> getChildren() {
        return children;
    }
}