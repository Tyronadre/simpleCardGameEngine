package de.henrik.implementation.GameEvent;

import de.henrik.engine.events.GameEvent;
import de.henrik.implementation.player.PlayerImpl;

public class PlayerChangeEvent extends GameEvent {


    public final PlayerImpl newPlayer;

    public PlayerChangeEvent(PlayerImpl newPlayer) {
        super( "Player Change", null);
        this.newPlayer = newPlayer;
    }
}
