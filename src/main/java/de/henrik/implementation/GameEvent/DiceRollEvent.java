package de.henrik.implementation.GameEvent;

import de.henrik.engine.events.GameEvent;

public class DiceRollEvent extends GameEvent {
    public final int dice1;
    public final int dice2;

    public DiceRollEvent(int dice1, int dice2) {
        super( "Dice Rolled", null);
        this.dice1 = dice1;
        this.dice2 = dice2;
    }
}
