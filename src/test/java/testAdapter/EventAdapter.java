package testAdapter;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.ChoiceEvent;
import de.henrik.implementation.GameEvent.ChoiceSelectedEvent;
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
            case ChoiceEvent -> "choice event";
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
        Provider.gameEventThread.handleNextEvent();
    }

    /**
     * Type of Events
     */
    public enum EventType {
        ChoiceEvent,
    }
}
