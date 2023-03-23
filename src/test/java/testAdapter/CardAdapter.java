package testAdapter;

import de.henrik.engine.card.Card;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;

public class CardAdapter {
    public static Card getCard(int id) {
        return null;
    }

    public static int getCost(Card card) {
        return 0;
    }

    public static int getCardClassID(Card card) {
        return 0;
    }

    public static int getCardTypeID(Card card) {
        return 0;
    }

    public static void event(Card card, Player activePlayer, Player cardOwner, Game game) {
    }
}
