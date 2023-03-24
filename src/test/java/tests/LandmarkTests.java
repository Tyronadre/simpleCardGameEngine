package tests;

import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import testAdapter.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LandmarkTests {
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
        player1 = PlayerAdapter.getPlayer( 1);
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
    void test_landmark_e0_variables() {
        for (int i = 16; i <= 19; i++) {
            System.out.println("Testing (Landmark) Card " + i);
            assertEquals(i, LandmarkAdapter.getLandmark(i).getID());
            assertEquals("/cards/c" + i + "_f.png", LandmarkAdapter.getLandmark(i).getFrontOfCard().getPath());
            assertEquals("/cards/c" + i + "_b.png", LandmarkAdapter.getLandmark(i).getBackOfCard().getPath());
            assertEquals(switch (i) {
                case 16 -> 4;
                case 17 -> 10;
                case 18 -> 16;
                case 19 -> 22;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, LandmarkAdapter.getLandmark(i).getCost());
            assert (!PlayerAdapter.hasLandmark(player0, i));
        }
    }

    @Test
    void get_landmark() {
        PlayerAdapter.freeLandmark(player0, 16);
        assert (PlayerAdapter.hasLandmark(player0, 16));
    }

    @Test
    void buy_landmark() {
        PlayerAdapter.setCoins(player0, 4);
        PlayerAdapter.buyLandmark(player0, 16);
        assert (PlayerAdapter.hasLandmark(player0, 16));
        assertEquals(0, PlayerAdapter.getCoins(player0));
    }

    @Test
    void test_landmark_effect_c16() {
        PlayerAdapter.freeLandmark(player0, 16);
        assertEquals(2, PlayerAdapter.availableDice(player0));
        assertEquals(5, PlayerAdapter.getCoins(player0));
    }

    @Test
    void test_landmark_effect_c17() {
        PlayerAdapter.freeLandmark(player0, 17);
        CardAdapter.event(CardAdapter.getCard(3), player0, player0, 2);
        assertEquals(7, PlayerAdapter.getCoins(player0));
    }

    @Disabled("Private Test Case")
    @Test
    void test_landmark_effect_c18() {
    }

    @Test
    void test_landmark_effect_c19() {
        PlayerAdapter.freeLandmark(player0, 19);
        EventAdapter.setPlayerEvent(player0);
        Provider.gameEventThread.handleNextEvent();
        EventAdapter.rollDiceEvent(1, 0);
        Provider.gameEventThread.handleNextEvent();
        GameEvent event = Provider.gameEventThread.getNextEvent();
        EventAdapter.optionEvent(event, 0);
        Provider.gameEventThread.handleNextEvent();
        assertEquals(player0, Provider.game.getActivePlayer());
        Provider.gameEventThread.handleNextEvent();
        EventAdapter.rollDiceEvent(1, 0);
        event = Provider.gameEventThread.getNextEvent();
        assertEquals(EventAdapter.getEventName(EventAdapter.EventType.RollDiceEvent), event.getName());
        assertEquals(5, PlayerAdapter.getCoins(player0));
    }
}
