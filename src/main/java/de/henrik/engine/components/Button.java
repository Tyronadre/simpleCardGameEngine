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
import java.awt.event.MouseAdapter;
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
    private int state = state_DEFAULT;
    private final List<ActionListener> actionListeners = new ArrayList<>();

    public Button(String description, GameImage background, int x, int y, int width, int height) {
        super(x, y, width, height);
        if (description != null) {
            this.description = description;
            font = Game.game.getFont().deriveFont((float) getHeight() - 5);
        }
        if (background != null) this.background = background.getScaledInstance(width, height);
        addMouseListener(new GameMouseListenerAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (board.active) {
                    state = state_CLICKED;
                    for (var actionListener : actionListeners) {
                        actionListener.actionPerformed(new ActionEvent(this, 0, "buttonClicked"));
                    }
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (board.active) {
                    state = state_HOVERED;
                    repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (board.active) {
                    state = state_HOVERED;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (board.active) {
                    state = state_DEFAULT;
                    repaint();
                }
            }
        });
    }


    public Button(String description, int x, int y, int width, int height) {
        this(description, null, x, y, width, height);
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
            case state_DEFAULT -> {
                g.getGraphics().drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
            }
            case state_HOVERED -> {
                g.setColor(new Color(76, 90, 162));
                g.getGraphics().drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
            }
            case state_CLICKED -> {
                g.setColor(new Color(147, 147, 147, 26));
                g.getGraphics().fillRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
                g.setColor(Color.BLACK);
                g.getGraphics().drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
            }
        }

        super.paint(g);
    }

    @Override
    public void setSize(int width, int height) {
        if (description != null) font = Game.game.getFont().deriveFont((float) getHeight() - 5);
        if (background != null) background = background.getScaledInstance(width - 1, height - 1);
        super.setSize(width, height);
    }

    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

}
