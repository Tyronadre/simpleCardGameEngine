package de.henrik.engine.components;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.base.GameImage;

public class Pane extends GameComponent {

    private GameImage background;

    public Pane(GameImage background, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.background = background.getScaledInstance(width,height);
    }

    @Override
    public void paint(GameGraphics g) {
        g.drawImage(background.getImage(), getX(), getY());
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
