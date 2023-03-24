package testAdapter;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.GameStateChangeEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.card.landmark.Landmark;
import de.henrik.implementation.card.playingcard.CardClass;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.player.PlayerImpl;
import de.henrik.implementation.player.PlayerPaneImpl;

import java.util.Collection;
import java.util.List;

public class PlayerAdapter {
    /**
     * Returns a Player
     *
     * @param i    the player id
     */
    public static Player getPlayer(int i) {
        if (Provider.game.getActiveGameBoard() instanceof GameBoard gameBoard) {
            for (Player player : gameBoard.getPlayers()) {
                if (player.getId() == i) {
                    return player;
                }
            }
            throw new IllegalStateException("Player " + i + " not found");
        }
        throw new IllegalStateException("Not in Playing State");
    }

    /**
     * Returns the amount of coins the player has
     *
     * @param player the player
     * @return the amount of coins the player has
     */
    public static int getCoins(Player player) {
        return ((PlayerImpl) player).getCoins();
    }

    /**
     * Sets the amount of coins the player has
     *
     * @param player the player
     * @param coins  the amount of coins
     */
    public static void setCoins(Player player, int coins) {
        ((PlayerImpl) player).setCoins(coins);
    }

    /**
     * Returns the amount of coins the player has, but parsed from however it is displayed in the GUI
     *
     * @param player the player
     * @return the amount of coins the player has, but parsed from however it is displayed in the GUI
     */
    public static int getShownCoins(Player player) {
        return Integer.parseInt(((PlayerPaneImpl) player.getPlayerPane()).getCoinLabelText().substring("Coins: ".length()));
    }

    /**
     * Buys a Card for a player
     *
     * @param player the player
     * @param card   the card
     */
    public static void buyCard(Player player, Card card) {
        Provider.gameEventThread.handleAllEvents();
        ((PlayerImpl) player).addCard((PlayingCard) card);
        Provider.game.event(new GameStateChangeEvent(GameBoard.NEW_PLAYER_STATE));
        Provider.gameEventThread.handleNextEvent();
    }

    /**
     * Adds a Card to a player, but as if the Card costs 0 Coins
     *
     * @param player the player
     * @param card   the card
     */
    public static void freeCard(Player player, Card card) {
        ((PlayerImpl) player).freeCard((PlayingCard) card);
    }

    /**
     * Removes a Card from a player
     *
     * @param player the player
     * @param card   the card
     */
    public static void removeCard(Player player, Card card) {
        ((PlayerImpl) player).removeCard((PlayingCard) card);
    }

    /**
     * Returns all CardStacks that are not for SpaceStations of a player
     *
     * @param player the player
     * @return all CardStacks that are not for SpaceStations of a player
     */
    public static CardStack[] getAllNonSpaceStationCardStacks(Player player) {
        return ((PlayerImpl) player).getCardStacks().stream().filter(cardStack -> !((PlayingCard) cardStack.getCard()).getCardClass().equals(CardClass.SPACE_STATION)).toArray(CardStack[]::new);
    }

    /**
     * Returns the CardStack that a specified Card would fit
     *
     * @param player the player
     * @param card   the card
     * @return the CardStack that a specified Card would fit
     */
    public static CardStack getCardStack(Player player, Card card) {
        List<CardStack> stacks = ((PlayerImpl) player).getCardStacks().stream().filter(cardStack -> cardStack.test(card)).toList();
        if (stacks.size() > 1) {
            throw new RuntimeException("More than one card stack found");
        }
        if (stacks.size() == 0) {
            throw new RuntimeException("No card stack found");
        }
        return stacks.get(0);
    }


    /**
     * Clears all Cards from a player
     *
     * @param player the player
     */
    public static void clearCards(Player player) {
        for (Card card : getAllCards(player)) {
            removeCard(player, card);
        }
    }

    /**
     * Returns all Cards of a player
     *
     * @param player the player
     * @return all Cards of a player
     */
    private static Iterable<? extends Card> getAllCards(Player player) {
        return player.getCardList().stream().toList();
    }

    /**
     * Gives a player a Landmark, but as if the Landmark costs 0 Coins
     *
     * @param player the player
     * @param id     the id of the Landmark
     */
    public static void freeLandmark(Player player, int id) {
        ((PlayerImpl) player).freeLandmark(id);
    }

    /**
     * Tests if a player has a Landmark
     *
     * @param player the player
     * @param id     the id of the Landmark
     * @return true if the player has the Landmark, false otherwise
     */
    public static boolean hasLandmark(Player player, int id) {
        return ((PlayerImpl) player).hasLandmark(id);
    }

    /**
     * Gets the LandmarkCard with the specified id from a player
     *
     * @param player the player
     * @param id     the id of the Landmark
     */
    public static Card getLandmark(Player player, int id) {
        return ((PlayerImpl) player).getLandmark(id);
    }

    /**
     * Removes a Landmark from a player
     *
     * @param player the player
     * @param id     the id of the Landmark
     */
    public static void removeLandmark(Player player, int id) {
        ((PlayerImpl) player).removeLandmark((Landmark) getLandmark(player, id));
    }

    /**
     * Returns the CardStack of a Landmark with the specified id
     *
     * @param player the player
     * @param id     the id of the Landmark
     * @return the CardStack of the Landmark
     */
    public static CardStack getLandmarkStack(Player player, int id) {
        for (CardStack stack : ((PlayerImpl) player).getLandmarkStacks())
            if (stack.getCard().getID() == id) return stack;
        throw new RuntimeException("No landmark stack found");
    }

    /**
     * Buy a Landmark for a player
     *
     * @param player the player
     * @param id     the id of the Landmark
     */
    public static void buyLandmark(Player player, int id) {
        PlayerImpl playerImpl = (PlayerImpl) player;
        playerImpl.buyLandmark((Landmark) getLandmark(player, id), getLandmarkStack(player, id));

    }

    /**
     * Returns how many dice are available for a player
     *
     * @param player the player
     * @return how many dice are available for this player
     */
    public static int availableDice(Player player) {
        return ((PlayerImpl) player).hasLandmark(16) ? 2 : 1;
    }

    /**
     * Clears all Landmarks from a player
     *
     * @param player the player
     */
    public static void clearLandmarks(Player player) {
        for (Landmark landmark : ((PlayerImpl) player).getAllLandmarks())
            removeLandmark(player, landmark.getID());
    }

    public static Collection<? extends Card> getAllLandmarks(Player player) {
        return ((PlayerImpl) player).getAllLandmarks();
    }

    public static Collection<? extends CardStack> getCardStacks(Player player) {
        return ((PlayerImpl) player).getCardStacks();
    }
}
