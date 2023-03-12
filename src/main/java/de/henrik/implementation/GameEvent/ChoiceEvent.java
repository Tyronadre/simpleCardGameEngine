package de.henrik.implementation.GameEvent;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.events.GameEvent;

import java.util.function.Predicate;

public class ChoiceEvent extends GameEvent {
    public Predicate<? super GameComponent> type;
    public ChoiceSelectedListener selected;

    public ChoiceEvent(Predicate<? super GameComponent> type, ChoiceSelectedListener selected) {
        super("choice event", null);
        this.type = type;
        this.selected = selected;
    }
}
