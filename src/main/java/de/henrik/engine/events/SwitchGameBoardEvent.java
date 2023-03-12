package de.henrik.engine.events;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.game.Board;

public class SwitchGameBoardEvent extends GameEvent {
    public final Board oldBoard,newBoard;

    public SwitchGameBoardEvent(Board oldBoard, Board newBoard) {
        super("SwitchGameBoardEvent", null);
        this.oldBoard = oldBoard;
        this.newBoard = newBoard;
    }
}
