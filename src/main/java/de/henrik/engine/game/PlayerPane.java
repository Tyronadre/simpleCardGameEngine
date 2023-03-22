package de.henrik.engine.game;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.components.Pane;

abstract public class PlayerPane extends Pane {
    public PlayerPane(GameImage background, int x, int y, int width, int height) {
        super(background, x, y, width, height);
    }
}
