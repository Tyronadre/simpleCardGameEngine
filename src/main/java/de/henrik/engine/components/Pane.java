package de.henrik.engine.components;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.base.GameImage;

public class Pane extends GameComponent {

    private GameImage background;

    public Pane(GameImage background, int x, int y, int width, int height, boolean visible){
        super(x, y, width, height);
        this.background = background == null ? null : background.getScaledInstance(width, height);
        this.setVisible(visible);
    }
    public Pane(GameImage background, int x, int y, int width, int height) {
        this(background, x, y, width, height, true);
    }

    public Pane(int x, int y, int width, int height) {
        this(null, x, y, width, height);
    }

    @Override
    public void paint(GameGraphics g) {
        if (!visible)
            return;
        if (background != null) g.drawImage(background.getImage(), getX(), getY());
        super.paint(g);
    }

    @Override
    public void setSize(int width, int height) {
        background = background.getScaledInstance(width - 1, height - 1);
        super.setSize(width, height);
    }

    public void setBackground(GameImage background) {
        this.background = background.getScaledInstance(getWidth(),getHeight());
        repaint();
    }
}
