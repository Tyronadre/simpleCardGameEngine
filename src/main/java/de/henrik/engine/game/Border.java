package de.henrik.engine.game;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.components.Pane;
import de.henrik.implementation.card.playingcard.CardClass;

import java.awt.*;

public class Border {

    Color color;
    boolean dashed;
    int size,arc;
    GameComponent parent;

    public Border(Color color, boolean dashed, int size, GameComponent parent, int arc) {
        this.color = color;
        this.dashed = dashed;
        this.size = size;
        this.arc = arc;
        this.parent = parent;
    }

    public void paint(GameGraphics graphics) {
        GameGraphics g = graphics.create().setClip(graphics.getClip());
        g.setColor(color);
        if (dashed)
            g.getGraphics().setStroke( new BasicStroke((float) size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, new float[]{10f}, 0f));
        else
            g.getGraphics().setStroke( new BasicStroke((float) size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{1f}, 0f));
        g.getGraphics().drawRoundRect(parent.getX(), parent.getY(),parent.getWidth()-size/2,parent.getHeight()-size/2, arc,arc);
    }
}
