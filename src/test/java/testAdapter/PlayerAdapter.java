package testAdapter;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import de.henrik.engine.game.PlayerPane;

public class PlayerAdapter {

    public static Player getPlayer(Game game, int i) {
        return null;
    }

    /**
     * Returns the amount of coins the player has
     * @param player the player
     * @return the amount of coins the player has
     */
    public static int getCoins(Player player) {
        return 0;
    }

    /**
     * Returns the amount of coins the player has, but parsed from however it is displayed in the GUI
     * @param player the player
     * @return the amount of coins the player has, but parsed from however it is displayed in the GUI
     */
    public static short getShownCoins(Player player) {
        return 0;
    }

    public static void setCoins(Player player, int coins) {
    }

    public static void addCard(Player player, Card card) {
    }

    public static void removeCard(Player player, Card card) {
    }

    public static CardStack[] getAllNonSpaceStationCardStacks(Player player3) {
        return null;
    }

    public static PlayerPane getCardStack(Player player1, Card tradeCard2) {
        return null;
    }
}
