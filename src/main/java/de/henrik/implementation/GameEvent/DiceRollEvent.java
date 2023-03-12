package de.henrik.implementation.GameEvent;

import de.henrik.engine.events.GameEvent;

public class DiceRollEvent extends GameEvent {
    public int roll;

    public DiceRollEvent(int roll) {
        super( "Dice Rolled", null);
        this.roll = roll;
    }
}
