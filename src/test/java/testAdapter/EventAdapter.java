package testAdapter;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.game.PlayerPane;

public class EventAdapter {
    /**
     * Returns the name of an event
     * @param eventType the type of the event
     * @return the name of an event
     */
    public static String getEventName(EventType eventType) {
        return "";
    }

    /**
     * Returns the choices for a ChoiceEvent
     * @return the choices for a ChoiceEvent
     */
    public static GameComponent[] getChoiceEventChoices() {
        return null;
    }

    /**
     * Sets the choice for a ChoiceEvent
     * @param gameComponent the choice for a ChoiceEvent
     */
    public static void setChoiceEventChoice(GameComponent gameComponent) {
    }

    /**
     * Type of Events
     */
    public enum EventType {
        ChoiceEvent,
    }
}
