package de.henrik.engine.components;

import de.henrik.engine.base.Game;
import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;

import java.awt.*;
import java.awt.event.*;

public class TextField extends GameComponent {
    String savedString;
    public static final int state_DEFAULT = 0;
    public static final int state_HOVERED = 1;
    public static final int state_CLICKED = 2;
    private int state = state_DEFAULT;
    private Font font;

    public TextField(String initalString, int x, int y, int width, int height) {
        super(x, y, width, height);
        savedString = initalString;
        font = Game.game.getFont().deriveFont((float) getHeight());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (board.active) {
                    state = state_CLICKED;
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

        Game.game.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (Game.isRunning() && Game.game.getActiveGameBoard() == board && state_CLICKED == state) {
                    if (e.getKeyChar() == '\b') {
                        if (savedString.length() != 0) savedString = savedString.substring(0, savedString.length() - 1);
                    } else savedString += e.getKeyChar();
                    repaint();
                }
            }
        });
    }

    @Override
    public void setSize(int width, int height) {
        font = Game.game.getFont().deriveFont((float) getHeight());
        super.setSize(width, height);
    }

    public void setDescription(String description) {
        this.savedString = description;
        repaint();
    }


    @Override
    public void paint(GameGraphics g) {
        super.paint(g);
        switch (state) {
            case state_CLICKED:
                g.setColor(new Color(147, 147, 147, 26));
                g.getGraphics().fillRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
            case state_HOVERED:
                g.setColor(new Color(76, 90, 162));
                g.getGraphics().drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
                break;
            case state_DEFAULT:
                g.setColor(Color.black);
                g.getGraphics().drawRoundRect(getX(), getY(), getWidth(), getHeight(), 3, 3);
        }
        g.setColor(Color.black);
        g.getGraphics().setFont(font);
        g.getGraphics().drawString(savedString, this.getX(), this.getY() + getHeight());

    }
}
