package testAdapter;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Player;

public class EventAdapter {
    /**
     * Returns the name of an event
     *
     * @param eventType the type of the event
     * @return the name of an event
     */
    public static String getEventName(EventType eventType) {
        return null;
    }

    /**
     * Returns the choices for a ChoiceEvent
     *
     * @return the choices for a ChoiceEvent
     */
    public static GameComponent[] getChoiceEventChoices(Player[] players, GameEvent event) {
        return null;

    }

    /**
     * Sets the choice for a ChoiceEvent
     *
     * @param gameComponent the choice for a ChoiceEvent
     */
    public static void setChoiceEventChoice(GameEvent event, GameComponent gameComponent, Player owner) {
    }

    public static void rollDiceEvent(int dice1, int dice2) {

    }

    public static void nextPlayerEvent() {
    }

    public static GameEvent setPlayerEvent(Player player) {
        return null;

    }

    public static void optionEvent(GameEvent event, int i) {
    }

    /**
     * Type of Events
     */
    public enum EventType {
        ChoiceEvent, RollDiceEvent, DialogEvent, NextPlayerEvent
    }
}
