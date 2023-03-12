package de.henrik.implementation.GameEvent;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.Card;
import de.henrik.engine.events.GameEvent;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.player.PlayerImpl;

import java.awt.*;

public class DraggingCardEvent extends GameEvent {
    public boolean startDragging;
    public boolean endDragging;
    public Point pos;
    public PlayerImpl player;

    final public PlayingCard card;

    public DraggingCardEvent(GameComponent parent, PlayingCard card, PlayerImpl player) {
        super( "Drag Card", parent);
        this.card = card;
        this.player = player;
    }

    @Override
    public String toString() {
        return "DraggingCardEvent{" +
                ", name='" + getName() + '\'' +
                ", parent=" + getParent() +
                ", startDragging=" + startDragging +
                ", endDragging=" + endDragging +
                '}';
    }
}
