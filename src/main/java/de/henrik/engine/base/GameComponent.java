package de.henrik.engine.base;

import de.henrik.engine.events.GameMouseListener;
import de.henrik.engine.game.Board;
import de.henrik.engine.game.Border;
import de.henrik.engine.game.Game;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class GameComponent {
    private int x, y, width, height;
    protected GameGraphics g;
    protected GameComponent parent;
    protected final List<GameComponent> children;
    protected Board board;
    protected Border border;

    protected boolean visible = true;


    public GameComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        children = Collections.synchronizedList(new ArrayList<>());
    }

    public GameComponent(Point pos, Dimension size) {
        this(pos.x, pos.y, size.width, size.height);
    }

    public GameComponent() {
        children = new ArrayList<>();
    }


    /**
     * {@link GameComponent#setGraphics(GameGraphics) } has to be called before any paint call
     * checks if the game is running then calls {@link GameComponent#paintChildren(GameGraphics)}
     */
    public void paint(GameGraphics g) {
        if (Game.isRunning() && visible) {
            paintChildren(g);
            if (border != null) {
                border.paint(g);
            }
        }
    }


    public void paintChildren(GameGraphics g) {
        if (!Game.isRunning() || !visible) {
            return;
        }
        synchronized (children) {
            for (GameComponent child : children) {
                if (g.getClip() == null || g.getClip().intersects(child.getClip())) {
                    child.paint(g.create());
                }
            }
        }
    }

    /**
     * This method creates a graphic with a clip of the given size, and calls the paint method of this component.
     * This Method ensures that x and y are within this component, and width and height are at least 1, if not no paint call will happen.
     * If there is no parent, the paint method of this component will be called
     *
     * @param x      x
     * @param y      y
     * @param width  width
     * @param height height
     */
    public void repaint(int x, int y, int width, int height) {
        if (!Game.isRunning()) return;
        g = Game.getGameGraphics();
        x = Math.min(x, Options.getWidth());
        y = Math.min(y, Options.getHeight());
        width = Math.min(width, Options.getWidth() - x);
        height = Math.min(height, Options.getHeight() - y);
        if (width <= 0 || height <= 0 || g == null) return;
        if (parent == null || this instanceof Board) {
            paint(g.create().setClip(x, y, width, height));
            Toolkit.getDefaultToolkit().sync();
        } else parent.repaint(x, y, width, height);
    }

    /**
     * @param intersection part that need repainting
     * @see GameComponent#repaint(int, int, int, int)
     */
    public void repaint(Rectangle intersection) {
        repaint(intersection.x, intersection.y, intersection.width, intersection.height);
    }

    public void repaint() {
        repaint(getClip());
    }

    /**
     * @return a rectangle of this component, specified by {@link GameComponent#getX()}, {@link GameComponent#getY()}, {@link GameComponent#getWidth()}, {@link GameComponent#getHeight()}.
     */
    public Rectangle getClip() {
        return new Rectangle(getX(), getY(), getWidth() + 1, getHeight() + 1);
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
        if (x == getX() && y == getY()) return;
        boolean movementLeft = x - getX() <= 0;
        boolean movementUp = y - getY() <= 0;

        int oldx = getX();
        int oldy = getY();
        setPosition(x, y);


        //Repaint parent. (if no parent just this component)
        GameComponent p = parent;
        if (p == null) p = this;
        p.repaint(Math.max(oldx, 0), Math.max(movementUp ? Math.max(getY() + getHeight(), oldy) : oldy, 0), getWidth(), Math.min(getHeight(), Math.abs(getY() - oldy)));
        p.repaint(Math.max(movementLeft ? Math.max(getX() + getWidth(), oldx) : oldx, 0), Math.max(oldy, 0), Math.min(getWidth(), Math.abs(getX() - oldx)), getHeight());
        repaint();
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
        if (component.board == null) setBoardRecursively(this);
    }

    protected void setBoardRecursively(GameComponent parent) {
        if (parent.children.size() == 0) return;
        for (GameComponent child : parent.children) {
            child.board = parent.board;
            child.setBoardRecursively(child);
        }
    }

    public void remove(GameComponent component) {
        if (component == null) {
            return;
        }
        children.remove(component);
        component.remove();
    }

    /**
     * Removes this component from its parent and all children from this component
     */
    private void remove() {
        parent = null;
        for (GameComponent child : children) {
            child.remove();
        }
        for (MouseMotionListener mouseMotionListener : mouseMotionListenerHashMap.values()) {
            Game.game.removeMouseMotionListener(mouseMotionListener);
        }
        for (MouseListener mouseListener : mouseListenerHashMap.values()) {
            Game.game.removeMouseListener(mouseListener);
        }
    }

    /**
     * Checks if a given point is inside this component. The size of this component should be a rectangle that encumbers all cards.
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

    public GameComponent getParent() {
        return parent;
    }

    final HashMap<Integer, MouseListener> mouseListenerHashMap = new HashMap<>();
    final HashMap<Integer, MouseMotionListener> mouseMotionListenerHashMap = new HashMap<>();

    public void addMouseListener(GameMouseListener mouseListener) {
        var mouseListenerForObject = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (pointInside(e.getLocationOnScreen())) mouseListener.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (pointInside(e.getLocationOnScreen())) mouseListener.mouseReleased(e);
                mouseListener.mouseReleasedAnywhere(e);
            }
        };

        var mouseMotionListenerForObject = new MouseMotionAdapter() {
            Point lastLocation = new Point();
            Point init = null;

            @Override
            public void mouseMoved(MouseEvent e) {
                if (!pointInside(lastLocation) && pointInside(e.getLocationOnScreen())) mouseListener.mouseEntered(e);
                if (pointInside(lastLocation) && !pointInside(e.getLocationOnScreen())) mouseListener.mouseExited(e);
                lastLocation = e.getLocationOnScreen();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                lastLocation = e.getLocationOnScreen();
                if (init == null) {
                    if (pointInside(e.getLocationOnScreen())) {
                        init = e.getLocationOnScreen();
                        Game.game.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseReleased(MouseEvent e) {
                                Game.game.removeMouseListener(this);
                                init = null;
                            }
                        });
                    }
                } else if (init != e.getLocationOnScreen()) {
                    mouseListener.mouseDragged(e);
                }
            }
        };
        mouseListenerHashMap.put(mouseListener.hashCode(), mouseListenerForObject);
        mouseMotionListenerHashMap.put(mouseListener.hashCode(), mouseMotionListenerForObject);
        Game.game.addMouseListener(mouseListenerForObject);
        Game.game.addMouseMotionListener(mouseMotionListenerForObject);
    }

    public void removeMouseListener(GameMouseListener mouseListener) {
        Game.game.removeMouseListener(mouseListenerHashMap.get(mouseListener.hashCode()));
    }

    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            this.visible = visible;
            repaint();
        }
    }


    public void setBorder(Border border) {
        this.border = border;
        if (border != null)
            border.setParent(this);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setParent(GameComponent parent) {
        this.parent = parent;
    }

    public boolean getVisible() {
        return visible;
    }
}