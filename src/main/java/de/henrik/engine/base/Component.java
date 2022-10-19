package de.henrik.engine.base;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Component {
    private int x, y, width, height;
    protected Graphics2D g;
    private List<Component> children;


    public Component(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.g = g;
        children = new ArrayList<>();
    }

    public Component(Point pos, Dimension size) {
        this(pos.x, pos.y, size.width, size.height);
    }


    /**
     * {@link Component#setGraphics(Graphics2D) } has to be called before any paint call
     */
    abstract public void paint();

    public void paintChildren() {
        for (Component child : children) {
            child.paint();
        }
    }

    /**
     * Repaints all children that were intersected.
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     */
    public void repaint(int x, int y, int width, int height) {
        g.setClip(x,y,width,height);
        Rectangle repaintRec = new Rectangle(x, y, width, height);
        for (Component child : children) {
            if (new Rectangle(child.getX(), child.getY(), child.getWidth(), child.getHeight()).intersects(repaintRec)) {
                child.paint();
            }
        }
        g.setClip(null);
    }

    public void repaint(Rectangle intersection) {
        repaint(intersection.x, intersection.y,intersection.width,intersection.height);
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

    private void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height) {
        setHeight(height);
        setWidth(width);
    }

    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }

    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        paint();
    }

    public void setPosition(Point pos) {
        setPosition(pos.x, pos.y);
    }

    public Point getPosition() {
        return new Point(getX(), getY());
    }


    /**
     * Sets the Graphics2D for this component and all children
     *
     * @param g the new Graphics2D
     */
    public void setGraphics(Graphics2D g) {
        this.g = g;
        for (Component child : children) {
            child.setGraphics(g);
        }
    }

    public void add(Component component) {
        children.add(component);
    }

    public void remove(Component component) {
        children.remove(component);
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

    public List<Component> getChildren() {
        return children;
    }


}
