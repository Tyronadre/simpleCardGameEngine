package testAdapter;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.card.playingcard.CardClass;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.card.playingcard.PlayingCardBuilder;
import de.henrik.implementation.player.PlayerImpl;
import de.henrik.implementation.player.PlayerPaneImpl;

import java.util.List;

public class PlayerAdapter {

    public static Player getPlayer(Game game, int i) {
        PlayerImpl player = new PlayerImpl(i, "player" + i);
        game.getActiveGameBoard().add(player.getPlayerPane());
        ((GameBoard) game.getActiveGameBoard()).getPlayers().add(player);
        for (Card card : PlayingCardBuilder.buildCardsFromCSV("/cardsBase.csv"))
            player.addCard((PlayingCard) card);
        return player;
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
     * Returns the amount of coins the player has, but parsed from however it is displayed in the GUI
     *
     * @param player the player
     * @return the amount of coins the player has, but parsed from however it is displayed in the GUI
     */
    public static int getShownCoins(Player player) {
        return Integer.parseInt(((PlayerPaneImpl) player.getPlayerPane()).getCoinLabelText().substring("Coins: ".length()));
    }

    public static void setCoins(Player player, int coins) {
        ((PlayerImpl) player).setCoins(coins);
    }

    public static void addCard(Player player, Card card) {
        ((PlayerImpl) player).addCard((PlayingCard) card);
    }

    public static void freeCard(Player player, Card card) {
        ((PlayerImpl) player).freeCard((PlayingCard) card);
    }

    public static void removeCard(Player player, Card card) {
        ((PlayerImpl) player).removeCard((PlayingCard) card);
    }

    public static CardStack[] getAllNonSpaceStationCardStacks(Player player) {
        return ((PlayerImpl) player).getCardStacks().stream().filter(cardStack -> !((PlayingCard) cardStack.getCard()).getCardClass().equals(CardClass.SPACE_STATION)).toArray(CardStack[]::new);
    }

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

    public static void clearCards(Player player) {
        for (Card card : getAllCards(player)) {
            removeCard(player, card);
        }
    }

    private static Iterable<? extends Card> getAllCards(Player player) {
        return player.getCardList().stream().toList();
    }
}
