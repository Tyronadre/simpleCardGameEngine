package de.henrik.engine.game;

import de.henrik.engine.base.GameGraphics;
import de.henrik.engine.base.GameComponent;
import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.events.GameEventListener;
import de.henrik.implementation.game.Options;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EventListener;
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
    public boolean active;
    private GameImage backgroundImage;
    private List<ActionListener> onActivate;
    private List<ActionListener> onDeactivate;
    private List<GameEventListener> gameListener;

    public Board(GameImage backgroundImage) {
        super(0, 0, Options.getWidth(), Options.getHeight());
        this.backgroundImage = backgroundImage;
        this.onActivate = new ArrayList<>();
        this.onDeactivate = new ArrayList<>();
    }

    public void setBackgroundImage(GameImage backgroundImage) {
        this.backgroundImage = backgroundImage.getScaledInstance(getWidth(), getHeight());
        repaint();
    }

    @Override
    public void add(GameComponent component) {
        children.add(component);
        component.setParent(this);
        component.setBoard(this);
        setBoardRecursively(component);
    }

    @Override
    public void paint(GameGraphics g) {
        g.drawImage(backgroundImage.getImage(), getX(), getY());
        super.paint(g);
    }

    /**
     * Gets called whenever this GameBoard is switched to the active one
     */
    protected void activate() {
        this.active = true;
        for (ActionListener actionListener : onActivate) {
            actionListener.actionPerformed(new ActionEvent(this, 0, "GameBoardActivate"));
        }
    }

    ;

    /**
     * Gets called whenever this GameBoard was active and is switched for another one
     */
    protected void deactivate() {
        this.active = false;
        for (ActionListener actionListener : onDeactivate) {
            actionListener.actionPerformed(new ActionEvent(this, 1, "GameBoardDeactivate"));
        }
    }

    public void addActivationListener(ActionListener actionListener) {
        this.onActivate.add(actionListener);
    }

    public CardStack getCardStackAtPosition(Point pos) {
        for (GameComponent child : children) {
            if (child instanceof CardStack && child.pointInside(pos)) {
                return (CardStack) child;
            }
        }
        return null;
    }

    public void addKeyListener(KeyListener keyListener) {
        Game.game.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (Game.game.getActiveGameBoard() == Board.this) {
                    keyListener.keyTyped(e);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (Game.game.getActiveGameBoard() == Board.this) {
                    keyListener.keyPressed(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (Game.game.getActiveGameBoard() == Board.this) {
                    keyListener.keyReleased(e);
                }
            }
        });
    }

    protected void addEventListener(GameEventListener eventListener) {
        this.gameListener.add(eventListener);
    }

    protected void removeGameListener() {
        this.gameListener = new ArrayList<>();
    }

    public void event(GameEvent event) {
        for (GameEventListener gameEventListener : gameListener) {
            gameEventListener.handleEvent(event);
        }
    }
}

