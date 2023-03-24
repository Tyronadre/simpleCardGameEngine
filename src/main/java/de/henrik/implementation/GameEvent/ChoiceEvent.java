package de.henrik.implementation.GameEvent;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.events.GameEvent;

import java.util.function.Predicate;

public class ChoiceEvent extends GameEvent {
    public final Predicate<? super GameComponent> type;
    public final ChoiceSelectedListener selected;

    public ChoiceEvent(Predicate<? super GameComponent> type, ChoiceSelectedListener onSelect) {
        super("Choice Event", null);
        this.type = type;
        this.selected = onSelect;
    }
}
