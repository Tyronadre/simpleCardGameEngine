package de.henrik.engine.components;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.base.GameImage;
import de.henrik.engine.events.GameMouseListener;
import de.henrik.engine.events.GameMouseListenerAdapter;
import de.henrik.engine.game.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Button extends GameComponent {

    private String description;
    private Font font;
    private GameImage background;
    public static final int state_DEFAULT = 0;
    public static final int state_HOVERED = 1;
    public static final int state_CLICKED = 2;
    public static final int state_DISABLED = 3;
    private int state = state_DEFAULT;
    private final List<ActionListener> actionListeners = new ArrayList<>();

    public Button(String description, GameImage background, int x, int y, int width, int height) {
        super(x, y, width, height);
        if (description != null) {
            this.description = description;
            font = Game.game.getFont().deriveFont((float) getHeight() - 5);
        }
        if (background != null) this.background = background.getScaledInstance(width, height);
        GameMouseListener defaultHandler = new GameMouseListenerAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (board.active && state != state_DISABLED && e.getButton() == MouseEvent.BUTTON1) {
                    state = state_CLICKED;
                    repaint(getClip());
                    for (var actionListener : actionListeners) {
                        actionListener.actionPerformed(new ActionEvent(this, 0, "buttonClicked"));
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (board.active && state != state_DISABLED) {
                    state = state_HOVERED;
                    repaint(getClip());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (board.active && state != state_DISABLED) {
                    state = state_DEFAULT;
                    repaint(getClip());
                }
            }
        };
        addMouseListener(defaultHandler);
    }


    public Button(String description, int x, int y, int width, int height) {
        this(description, null, x, y, width, height);
    }

    public Button(String description) {
        this(description, 0, 0, 0, 0);
    }

    public Button(GameImage background) {
        this(background, 0, 0, 0, 0);
    }

    public Button(GameImage background, int x, int y, int width, int height) {
        this(null, background, x, y, width, height);
    }

    public void changeDescription(String description) {
        this.description = description;
        repaint();
    }

    @Override
    public void paint(GameGraphics g) {
        if (!visible)
            return;
        if (background != null) {
            g.drawImage(background.getImage(), getX(), getY());
        }
        if (description != null && !description.equals("")) {
            g.getGraphics().setFont(font);
            g.drawString(description, getX() + 5, getY() + getHeight() - getHeight() / 10 - 5);
        }
        switch (state) {
            case state_DISABLED -> {
                g.getGraphics().drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
                g.setColor(new Color(0.25f,0.25f,0.25f,0.25f));
                g.getGraphics().fillRoundRect(getX(),getY(),getWidth(),getHeight(),3,3);
            }
            case state_DEFAULT -> g.getGraphics().drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
            case state_HOVERED -> {
                g.setColor(new Color(76, 90, 162));
                g.getGraphics().drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
            }
            case state_CLICKED -> {
                g.setColor(new Color(147, 147, 147, 26));
                g.getGraphics().fillRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
                g.setColor(GameGraphics.defaultColor);
                g.getGraphics().drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
            }
        }
        g.setColor(GameGraphics.defaultColor);
        super.paint(g);
    }

    public void enable() {
        state = state_DEFAULT;
    }

    public void disable() {
        state = state_DISABLED;
    }

    @Override
    public void setSize(int width, int height) {
        if (description != null) font = Game.game.getFont().deriveFont((float) height - 5);
        if (background != null) background = background.getScaledInstance(width - 1, height - 1);
        super.setSize(width, height);
    }

    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    public void removeActionListener() {
        actionListeners.clear();
    }

    public boolean isEnabled() {
        return state != state_DISABLED;
    }
}
