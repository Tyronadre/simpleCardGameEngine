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
            case ChoiceEvent -> "choice event";
            case RollDiceEvent -> "Dice Rolled";
            case NextPlayerEvent -> null;
            case BuyCardsState -> "Buy Cards";
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
    public static void setChoiceEventChoice(GameEvent event, GameComponent gameComponent, Player owner){
        ((ChoiceEvent) event).selected.consume(new ChoiceSelectedEvent(gameComponent, (PlayerImpl) owner));
    }

    public static void rollDiceEvent(int dice1, int dice2) {
        Provider.game.event(new DiceRollEvent(dice1, dice2));
        Provider.gameEventThread.handleNextEvent();
        Provider.gameEventThread.handleNextEvent();
    }

    public static void nextPlayerEvent() {
        ((GameBoard) Provider.game.getActiveGameBoard()).nextPlayer();
    }

    public static GameEvent setPlayerEvent(Player player) {
        return new PlayerChangeEvent((PlayerImpl) player);
    }

    public static void optionEvent(GameEvent event, int i) {
        ((GameDialogEvent) event).getAnswerRunnables()[i].run();
    }

    /**
     * Type of Events
     */
    public enum EventType {
        ChoiceEvent,
        RollDiceEvent,
        BuyCardsState, NextPlayerEvent
    }
}
