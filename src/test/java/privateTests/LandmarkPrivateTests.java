package privateTests;

import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testAdapter.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LandmarkPrivateTests {
    static Player player0;
    static Player player1;
    static Player player2;
    static Player player3;

    @BeforeAll
    static void init() {
        Provider.init();
        LandmarkAdapter.init();
        CardAdapter.init();
        Provider.setGameState();
        player0 = PlayerAdapter.getPlayer( 0);
        player1 = PlayerAdapter.getPlayer(1);
        player2 = PlayerAdapter.getPlayer( 2);
        player3 = PlayerAdapter.getPlayer( 3);
    }

    @BeforeEach
    void resetPlayers() {
        Provider.gameEventThread.clearEvents();
        PlayerAdapter.setCoins(player0, 5);
        PlayerAdapter.setCoins(player1, 5);
        PlayerAdapter.setCoins(player2, 5);
        PlayerAdapter.setCoins(player3, 5);
        PlayerAdapter.clearCards(player0);
        PlayerAdapter.clearCards(player1);
        PlayerAdapter.clearCards(player2);
        PlayerAdapter.clearCards(player3);
        PlayerAdapter.clearLandmarks(player0);
        PlayerAdapter.clearLandmarks(player1);
        PlayerAdapter.clearLandmarks(player2);
        PlayerAdapter.clearLandmarks(player3);
        Provider.gameEventThread.submitEvent(EventAdapter.setPlayerEvent(player0));
        Provider.gameEventThread.handleNextEvent();
    }

    @Test
    void buyLandmark() {
        System.out.println("Note that if this test fails the implementation still could be right, if its handled over what is clickable in the GUI");
        PlayerAdapter.setCoins(player1, 16);
        PlayerAdapter.buyLandmark(player1, 17);
        assert (PlayerAdapter.hasLandmark(player1, 17));
        assertEquals(6, PlayerAdapter.getCoins(player1));
        PlayerAdapter.setCoins(player2, 16);
        PlayerAdapter.buyLandmark(player2, 18);
        assert (PlayerAdapter.hasLandmark(player2, 18));
        assertEquals(0, PlayerAdapter.getCoins(player2));
        PlayerAdapter.setCoins(player3, 50);
        PlayerAdapter.buyLandmark(player3, 19);
        assert (PlayerAdapter.hasLandmark(player3, 19));
        assertEquals(28, PlayerAdapter.getCoins(player3));
        PlayerAdapter.buyLandmark(player3, 19);
        assert (PlayerAdapter.hasLandmark(player3, 19));
        assertEquals(28, PlayerAdapter.getCoins(player3));
    }

    @Test
    void test_landmark_c16(){
        assertEquals(1,PlayerAdapter.availableDice(player1));
        PlayerAdapter.freeLandmark(player1,16);
        assertEquals(2, PlayerAdapter.availableDice(player1));
        assertEquals(5, PlayerAdapter.getCoins(player1));
    }

    @Test
    void test_landmark_c17(){
        PlayerAdapter.freeLandmark(player0,17);
        PlayerAdapter.freeLandmark(player1,17);
        PlayerAdapter.freeCard(player0, CardAdapter.getCard(1));
        PlayerAdapter.freeCard(player0, CardAdapter.getCard(14));
        PlayerAdapter.setCoins(player0, 0);
        CardAdapter.event(CardAdapter.getCard(3), player0, player0, 2);
        assertEquals(2, PlayerAdapter.getCoins(player0));
        CardAdapter.event(CardAdapter.getCard(5), player0, player0, 4);
        assertEquals(6, PlayerAdapter.getCoins(player0));
        CardAdapter.event(CardAdapter.getCard(15), player0, player0, 11);
        assertEquals(12, PlayerAdapter.getCoins(player0));
        PlayerAdapter.setCoins(player0, 20);
        PlayerAdapter.setCoins(player1, 0);
        CardAdapter.event(CardAdapter.getCard(4), player0, player1, 3);
        assertEquals(18, PlayerAdapter.getCoins(player0));
        assertEquals(2, PlayerAdapter.getCoins(player1));
        CardAdapter.event(CardAdapter.getCard(13), player0, player1, 9);
        assertEquals(15, PlayerAdapter.getCoins(player0));
        assertEquals(5, PlayerAdapter.getCoins(player1));
        CardAdapter.event(CardAdapter.getCard(15), player1, player1, 11);
        assertEquals(5, PlayerAdapter.getCoins(player1));
    }

    @Test
    void test_landmark_c18(){
        PlayerAdapter.freeLandmark(player0, 18);
        EventAdapter.setPlayerEvent(player0);
        Provider.gameEventThread.handleNextEvent();
        EventAdapter.rollDiceEvent(1, 1);
        Provider.gameEventThread.handleNextEvent();
        assertEquals(player0, Provider.game.getActivePlayer());
        EventAdapter.nextPlayerEvent();
        assertEquals(player0, Provider.game.getActivePlayer());
        assertEquals(5, PlayerAdapter.getCoins(player0));
    }

    @Test
    void test_landmark_c19() {
        PlayerAdapter.freeLandmark(player0, 19);
        EventAdapter.setPlayerEvent(player0);
        Provider.gameEventThread.handleNextEvent();
        EventAdapter.rollDiceEvent(1, 0);
        Provider.gameEventThread.handleNextEvent();
        GameEvent event = Provider.gameEventThread.getNextEvent();
        EventAdapter.optionEvent(event, 1);
        Provider.gameEventThread.handleNextEvent();
        Provider.gameEventThread.handleNextEvent();
        assertEquals(player0, Provider.game.getActivePlayer());
        assertEquals(GameStateAdapter.GameState.BUY_CARD, GameStateAdapter.getGameState());
    }
}