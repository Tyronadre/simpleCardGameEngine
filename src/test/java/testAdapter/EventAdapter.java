package testAdapter;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.*;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.player.PlayerImpl;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter {
    /**
     * Returns the name of an event
     *
     * @param eventType the type of the event
     * @return the name of an event
     */
    public static String getEventName(EventType eventType) {
        return switch (eventType) {
            case ChoiceEvent -> "Choice Event";
            case RollDiceEvent -> "Dice Rolled";
            case DialogEvent -> "Game Dialog Event";
            case NextPlayerEvent -> "Player Change";
        };
    }

    /**
     * Returns the choices for a ChoiceEvent
     *
     * @return the choices for a ChoiceEvent
     */
    public static GameComponent[] getChoiceEventChoices(Player[] players, GameEvent event) {
        List<GameComponent> choices = new ArrayList<>();
        ChoiceEvent choiceEvent = ((ChoiceEvent) event);
        for (Player player : players) {
            if (choiceEvent.type.test(player.getPlayerPane())) {
                choices.add(player.getPlayerPane());
            }
            for (CardStack cardStack : ((PlayerImpl) player).getCardStacks()) {
                if (choiceEvent.type.test(cardStack)) {
                    choices.add(cardStack);
                }
            }
        }
        return choices.toArray(GameComponent[]::new);
    }

    /**
     * Sets the choice for a ChoiceEvent
     *
     * @param gameComponent the choice for a ChoiceEvent
     */
    public static void setChoiceEventChoice(GameEvent event, GameComponent gameComponent, Player owner) {
        ((ChoiceEvent) event).selected.consume(new ChoiceSelectedEvent(gameComponent, (PlayerImpl) owner));
    }

    /**
     * Creates and events one roll dice event with the given numbers
     *
     * @param dice1 the first dice
     * @param dice2 the second dice
     */
    public static void rollDiceEvent(int dice1, int dice2) {
        Provider.game.event(new DiceRollEvent(dice1, dice2));
        Provider.gameEventThread.handleNextEvent();
    }

    /**
     * Switches to the next player.
     */
    public static void nextPlayerEvent() {
        ((GameBoard) Provider.game.getActiveGameBoard()).nextPlayer();
    }

    /**
     * Creates an event to change to a specific player
     * @param player the player to change to
     * @return the event
     */
    public static GameEvent setPlayerEvent(Player player) {
        return new PlayerChangeEvent((PlayerImpl) player);
    }

    /**
     * Events an option for a DialogEvent
     *
     * @param event the event
     * @param i the index of the option
     */
    public static void optionEvent(GameEvent event, int i) {
        ((GameDialogEvent) event).getAnswerRunnables()[i].run();
    }

    /**
     * Type of Events
     */
    public enum EventType {
        ChoiceEvent, RollDiceEvent, DialogEvent, NextPlayerEvent
    }
}
