package de.henrik.implementation.GameEvent;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.events.GameEvent;
import de.henrik.implementation.player.PlayerImpl;

public class ChoiceSelectedEvent extends GameEvent {
    public GameComponent selected;
    public PlayerImpl owner;

    public ChoiceSelectedEvent(GameComponent selected, PlayerImpl owner) {
        super( "Choice selected", null);
        this.selected = selected;
        this.owner = owner;
    }
}
