package tests;

import de.henrik.engine.card.Card;
import de.henrik.engine.game.Player;
import de.henrik.implementation.game.Options;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import testAdapter.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameTest {

    static Player player0;
    static Player player1;


    @Test
    @Order(0)
    void init() {
        System.out.println("If one of these tests fail, all following tests will probably fail too.");
        Provider.init();
        CardAdapter.init();
        Provider.setMenuState();
        Options.drawStacks = 10;
    }

    @Test
    @Order(1)
    void MainMenuSettings() {
        MainMenuAdapter.setPlayerAmount(2);
        MainMenuAdapter.setPlayerName(0, "player1");
        MainMenuAdapter.setPlayerName(1, "player2");
        MainMenuAdapter.setExpansionE1(false);
        MainMenuAdapter.setExpansionE2(false);
    }

    @Test
    @Order(2)
    void StartGame() {
        Provider.setGameState();
        assertEquals(GameStateAdapter.GameState.NEW_PLAYER, GameStateAdapter.getGameState());
        player0 = PlayerAdapter.getPlayer(0);
        player1 = PlayerAdapter.getPlayer(1);
        assertEquals("player1", player0.getName());
    }


    @Test
    @Order(3)
    void PlayerStart() {
        assertEquals(2, player0.getCardList().size());
        assertEquals(2, player1.getCardList().size());
        assertArrayEquals(new int[]{1, 2}, PlayerAdapter.getPlayer(0).getCardList().stream().mapToInt(Card::getID).toArray());
        assertArrayEquals(new int[]{1, 2}, PlayerAdapter.getPlayer(1).getCardList().stream().mapToInt(Card::getID).toArray());
        var playerLandmarks = PlayerAdapter.getAllLandmarks(PlayerAdapter.getPlayer(0));
        assertEquals(4, playerLandmarks.size());
        assertArrayEquals(new int[]{16, 17, 18, 19}, playerLandmarks.stream().mapToInt(Card::getID).toArray());
        playerLandmarks = PlayerAdapter.getAllLandmarks(PlayerAdapter.getPlayer(1));
        assertEquals(4, playerLandmarks.size());
        assertArrayEquals(new int[]{16, 17, 18, 19}, playerLandmarks.stream().mapToInt(Card::getID).toArray());
        assertEquals(5, PlayerAdapter.getCoins(player0));
        assertEquals(5, PlayerAdapter.getCoins(player1));
    }

    @Test
    @Order(4)
    void DrawStacks() {
        assertEquals(84, DrawStacksAdapter.getAllCards().size());
        assertEquals(10, DrawStacksAdapter.getDrawStacks().size());
    }

    @Test
    @Order(5)
    void DrawStacksDraw() {
        DrawStacksAdapter.drawCard();
        assertEquals(83, DrawStacksAdapter.getAllCards().size());
        assertEquals(10, DrawStacksAdapter.getDrawStacks().size());
        assertEquals(2, player0.getCardList().size());
        assertEquals(2, player1.getCardList().size());
        assertEquals(2, PlayerAdapter.getCardStacks(player0).size());
        assertEquals(2, PlayerAdapter.getCardStacks(player1).size());
        assertArrayEquals(new int[]{1, 2}, PlayerAdapter.getPlayer(0).getCardList().stream().mapToInt(Card::getID).toArray());
        assertArrayEquals(new int[]{1, 2}, PlayerAdapter.getPlayer(1).getCardList().stream().mapToInt(Card::getID).toArray());
        var playerLandmarks = PlayerAdapter.getAllLandmarks(PlayerAdapter.getPlayer(0));
        assertEquals(4, playerLandmarks.size());
        assertArrayEquals(new int[]{16, 17, 18, 19}, playerLandmarks.stream().mapToInt(Card::getID).toArray());
        playerLandmarks = PlayerAdapter.getAllLandmarks(PlayerAdapter.getPlayer(1));
        assertEquals(4, playerLandmarks.size());
        assertArrayEquals(new int[]{16, 17, 18, 19}, playerLandmarks.stream().mapToInt(Card::getID).toArray());
        assertEquals(5, PlayerAdapter.getCoins(player0));
        assertEquals(5, PlayerAdapter.getCoins(player1));
    }


    @Test

    @Order(6)
    void t0_player0() {
        Provider.gameEventThread.clearEvents();
        GameStateAdapter.setActivePlayer(player0);
        assertEquals(GameStateAdapter.GameState.ROLL_DICE, GameStateAdapter.getGameState());
        assertEquals(player0, Provider.game.getActivePlayer());
        EventAdapter.rollDiceEvent(4, 0);
        assertEquals(5, PlayerAdapter.getCoins(player0));
        assertEquals(5, PlayerAdapter.getCoins(player1));

        PlayerAdapter.buyCard(player0, CardAdapter.getCard(1));
        assertEquals(3, player0.getCardList().size());
        assertEquals(2, player1.getCardList().size());
        assertArrayEquals(new int[]{1, 2, 1}, PlayerAdapter.getPlayer(0).getCardList().stream().mapToInt(Card::getID).toArray());
        assertArrayEquals(new int[]{1, 2}, PlayerAdapter.getPlayer(1).getCardList().stream().mapToInt(Card::getID).toArray());
        assertEquals(2, PlayerAdapter.getCardStacks(player0).size());
        assertEquals(2, PlayerAdapter.getCardStacks(player1).size());
        assertEquals(4, PlayerAdapter.getCoins(player0));

        assertEquals(5, PlayerAdapter.getCoins(player1));
    }

    @Test
    @Order(7)
    void t0_player1() {

        while (GameStateAdapter.getGameState() != GameStateAdapter.GameState.ROLL_DICE) {
            Provider.gameEventThread.handleNextEvent();
        }
        assertEquals(GameStateAdapter.GameState.ROLL_DICE, GameStateAdapter.getGameState());
        assertEquals(player1, Provider.game.getActivePlayer());

        EventAdapter.rollDiceEvent(1, 0);

        assertEquals(6, PlayerAdapter.getCoins(player0));
        assertEquals(6, PlayerAdapter.getCoins(player1));

        PlayerAdapter.buyCard(player1, CardAdapter.getCard(1));
        assertEquals(3, player0.getCardList().size());
        assertEquals(3, player1.getCardList().size());
        assertArrayEquals(new int[]{1, 2, 1}, PlayerAdapter.getPlayer(0).getCardList().stream().mapToInt(Card::getID).toArray());
        assertArrayEquals(new int[]{1, 2, 1}, PlayerAdapter.getPlayer(1).getCardList().stream().mapToInt(Card::getID).toArray());
        assertEquals(2, PlayerAdapter.getCardStacks(player0).size());
        assertEquals(2, PlayerAdapter.getCardStacks(player1).size());
        assertEquals(6, PlayerAdapter.getCoins(player0));
        assertEquals(5, PlayerAdapter.getCoins(player1));

        assertEquals(GameStateAdapter.GameState.NEW_PLAYER, GameStateAdapter.getGameState());
    }


}
