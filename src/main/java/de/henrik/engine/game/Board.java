package de.henrik.engine.game;

import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameImage;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Funktionen:
 * <ul>
 *     <li>Feste Größe</li>
 *     <li>Hintergrundbild definieren</li>
 *     <li>Definieren von Positionen an denen Kartenstapel liegen. Dabei ist Dimension der stapel fest. Die maximale Anzahl der karten kann festgelegt werden.</li>
 *     <li>Rückgabe von Karten die auf kartenstapel liegen</li>
 *     <li>bewegen von karten von einem stapel zum anderen</li>
 * </ul>>
 */
abstract public class Board extends GameComponent {
    private GameImage backgroundImage;
    private Card cardDragged;

    public Board(GameImage backgroundImage) {
        super(0, 0, Options.getWidth(), Options.getHeight());
        this.backgroundImage = backgroundImage;
    }

    public void setBackgroundImage(GameImage backgroundImage) {
        this.backgroundImage = backgroundImage.getScaledInstance(getWidth(),getHeight());
        repaint();
    }

    @Override
    public void paint(GameGraphics g) {
        g.drawImage(backgroundImage.getImage(), getX(), getY());
        super.paint(g);
    }

    /**
     * Gets called whenever this GameBoard is switched to the active one
     */
    abstract public void activate();

    /**
     * Gets called whenever this GameBoard was active and is switched for another one
     */
    abstract public void deavtivte();
}

