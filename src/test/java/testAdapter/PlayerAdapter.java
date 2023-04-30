package testAdapter;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.game.Player;

import java.util.Collection;

public class PlayerAdapter {
    /**
     * Returns a Player
     *
     * @param i the player id
     */
    public static Player getPlayer(int i) {
        return null;

    }

    /**
     * Returns the amount of coins the player has
     *
     * @param player the player
     * @return the amount of coins the player has
     */
    public static int getCoins(Player player) {
        return 0;
    }

    /**
     * Sets the amount of coins the player has
     *
     * @param player the player
     * @param coins  the amount of coins
     */
    public static void setCoins(Player player, int coins) {
    }

    /**
     * Returns the amount of coins the player has, but parsed from however it is displayed in the GUI
     *
     * @param player the player
     * @return the amount of coins the player has, but parsed from however it is displayed in the GUI
     */
    public static int getShownCoins(Player player) {
        return 0;
    }

    /**
     * Buys a Card for a player
     *
     * @param player the player
     * @param card   the card
     */
    public static void buyCard(Player player, Card card) {

    }

    /**
     * Adds a Card to a player, but as if the Card costs 0 Coins
     *
     * @param player the player
     * @param card   the card
     */
    public static void freeCard(Player player, Card card) {
    }

    /**
     * Removes a Card from a player
     *
     * @param player the player
     * @param card   the card
     */
    public static void removeCard(Player player, Card card) {
    }

    /**
     * Returns all CardStacks that are not for SpaceStations of a player
     *
     * @param player the player
     * @return all CardStacks that are not for SpaceStations of a player
     */
    public static CardStack[] getAllNonSpaceStationCardStacks(Player player) {
        return null;
    }

    /**
     * Returns the CardStack that a specified Card would fit
     *
     * @param player the player
     * @param card   the card
     * @return the CardStack that a specified Card would fit
     */
    public static CardStack getCardStack(Player player, Card card) {
        return null;

    }


    /**
     * Clears all Cards from a player
     *
     * @param player the player
     */
    public static void clearCards(Player player) {

    }

    /**
     * Returns all Cards of a player
     *
     * @param player the player
     * @return all Cards of a player
     */
    private static Iterable<? extends Card> getAllCards(Player player) {
        return null;
    }

    /**
     * Gives a player a Landmark, but as if the Landmark costs 0 Coins
     *
     * @param player the player
     * @param id     the id of the Landmark
     */
    public static void freeLandmark(Player player, int id) {
    }

    /**
     * Tests if a player has a Landmark
     *
     * @param player the player
     * @param id     the id of the Landmark
     * @return true if the player has the Landmark, false otherwise
     */
    public static boolean hasLandmark(Player player, int id) {
        return false;
    }

    /**
     * Gets the LandmarkCard with the specified id from a player
     *
     * @param player the player
     * @param id     the id of the Landmark
     */
    public static Card getLandmark(Player player, int id) {
        return null;

    }

    /**
     * Removes a Landmark from a player
     *
     * @param player the player
     * @param id     the id of the Landmark
     */
    public static void removeLandmark(Player player, int id) {
    }

    /**
     * Returns the CardStack of a Landmark with the specified id
     *
     * @param player the player
     * @param id     the id of the Landmark
     * @return the CardStack of the Landmark
     */
    public static CardStack getLandmarkStack(Player player, int id) {
        return null;

    }

    /**
     * Buy a Landmark for a player
     *
     * @param player the player
     * @param id     the id of the Landmark
     */
    public static void buyLandmark(Player player, int id) {


    }

    /**
     * Returns how many dice are available for a player
     *
     * @param player the player
     * @return how many dice are available for this player
     */
    public static int availableDice(Player player) {
        return 0;
    }

    /**
     * Clears all Landmarks from a player
     *
     * @param player the player
     */
    public static void clearLandmarks(Player player) {

    }

    public static Collection<? extends Card> getAllLandmarks(Player player) {
        return null;
    }

    public static Collection<? extends CardStack> getCardStacks(Player player) {
        return null;
    }
}
