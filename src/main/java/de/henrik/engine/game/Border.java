package de.henrik.engine.game;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameGraphics;

import java.awt.*;

public class Border {

    final Color color;
    final boolean dashed;
    final int size;
    final int arc;
    GameComponent parent;

    public Border(Color color, boolean dashed, int size, GameComponent parent, int arc) {
        this.color = color;
        this.dashed = dashed;
        this.size = size;
        this.arc = arc;
        this.parent = parent;
    }

    public Border(Color color, boolean dashed, int size, int arc) {
        this(color, dashed, size, null,arc);
    }

    public void setParent(GameComponent parent) {
        this.parent = parent;
    }

    public void paint(GameGraphics graphics) {
        GameGraphics g = graphics.create().setClip(graphics.getClip());
        g.setColor(color);
        if (dashed)
            g.getGraphics().setStroke( new BasicStroke((float) size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, new float[]{10f}, 0f));
        else
            g.getGraphics().setStroke( new BasicStroke((float) size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{1f}, 0f));
        g.getGraphics().drawRoundRect(parent.getX()+size/2, parent.getY()+size/2,parent.getWidth()-size,parent.getHeight()-size, arc,arc);
    }
}
