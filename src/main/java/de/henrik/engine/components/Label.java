package de.henrik.engine.components;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.game.Game;

import java.awt.*;

public class Label extends GameComponent {
    private String description;
    private Font font;

    public Label(String description, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.description = description;
        font = Game.game.getFont().deriveFont((float) getHeight() - 5);
    }

    public Label(String description) {
        this(description, 0, 0, 0, 0);
    }

    @Override
    public void paint(GameGraphics g) {
        if (!visible)
            return;
        g.getGraphics().setFont(font);
        g.drawString(description, getX() + 5, getY() + getHeight() - getHeight() / 10 - 5);
        super.paint(g);
    }

    @Override
    public void setSize(int width, int height) {
        font = Game.game.getFont().deriveFont((float) height - 5);
        super.setSize(width, height);
    }

    public void setDescription(String description) {
        this.description = description;
        repaint();
    }

    public String getDescription() {
        return description;
    }
}
