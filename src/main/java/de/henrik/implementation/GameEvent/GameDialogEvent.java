package de.henrik.implementation.GameEvent;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.events.GameEvent;

public class GameDialogEvent extends GameEvent {
    private final String message;
    private final String[] answerOptions;
    private final Runnable[] answerRunnables;

    public GameDialogEvent(GameComponent owner, String message, String[] answerOptions, Runnable[] answerRunnables) {
        super("GameDialogEvent", owner);
        if (answerOptions.length != answerRunnables.length)
            throw new IllegalArgumentException("answerOptions.length != answerRunnables.length");
        this.message = message;
        this.answerOptions = answerOptions;
        this.answerRunnables = answerRunnables;
    }

    public String getMessage() {
        return message;
    }

    public String[] getAnswerOptions() {
        return answerOptions;
    }

    public Runnable[] getAnswerRunnables() {
        return answerRunnables;
    }
}
