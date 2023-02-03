package de.henrik.implementation.GameEvent;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.Card;
import de.henrik.engine.events.GameEvent;
import de.henrik.implementation.card.playingcard.PlayingCard;

import java.awt.*;

public class DraggingCardEvent extends GameEvent {
    public boolean startDragging;
    public boolean endDragging;
    public Point pos;
    final public PlayingCard card;

    public DraggingCardEvent(int id, String name, GameComponent parent, PlayingCard card) {
        super(id, name, parent);
        this.card = card;
    }

    @Override
    public String toString() {
        return "DraggingCardEvent{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", parent=" + getParent() +
                ", startDragging=" + startDragging +
                ", endDragging=" + endDragging +
                '}';
    }
}
