package tests;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.Card;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import testAdapter.CardAdapter;
import testAdapter.EventAdapter;
import testAdapter.PlayerAdapter;
import testAdapter.Provider;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardEventTests {
    static Player player0;
    static Player player1;
    static Player player2;
    static Player player3;

    @BeforeAll()
    static void init() {
        Provider.init();
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
        setCoins(0, 0, 0, 0);
        PlayerAdapter.clearCards(player0);
        PlayerAdapter.clearCards(player1);
        PlayerAdapter.clearCards(player2);
        PlayerAdapter.clearCards(player3);
    }

    @Test
    void c1() {
        Card card = CardAdapter.getCard(1);
        CardAdapter.event(card, player0, player0, 1);
        testCoins(1, 0, 0, 0);
    }

    @Test
    void c2() {
        Card card = CardAdapter.getCard(2);
        CardAdapter.event(card, player0, player0, 2);
        testCoins(1, 0, 0, 0);
    }

    @Test
    void c3() {
        Card card = CardAdapter.getCard(3);
        CardAdapter.event(card, player0, player0, 2);
        testCoins(1, 0, 0, 0);
    }

    @Test
    void c4() {
        Card card = CardAdapter.getCard(4);
        setCoins(1, 0, 0, 0);
        CardAdapter.event(card, player0, player1, 3);
        testCoins(0, 1, 0, 0);
    }

    @Test
    void c5() {
        Card card = CardAdapter.getCard(5);
        CardAdapter.event(card, player0, player0, 4);
        testCoins(3, 0, 0, 0);
    }

    @Test
    void c6() {
        Card card = CardAdapter.getCard(6);
        CardAdapter.event(card, player0, player0, 5);
        testCoins(1, 0, 0, 0);
    }

    @Test
    void c7() {
        Card card = CardAdapter.getCard(7);
        setCoins(10, 10, 4, 0);
        CardAdapter.event(card, player0, player0, 6);
        GameEvent event = Provider.gameEventThread.getEvents().get(0);

        assertEquals(EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent), event.getName());
        assertArrayEquals(new GameComponent[]{player1.getPlayerPane(), player2.getPlayerPane(), player3.getPlayerPane()}, EventAdapter.getChoiceEventChoices(new Player[]{player0, player1, player2, player3}, event));
        EventAdapter.setChoiceEventChoice(event, player1.getPlayerPane(), player1);
        Provider.gameEventThread.handleNextEvent();
        testCoins(15, 5, 4, 0);
    }

    @Test
    void c8() {
        Card card = CardAdapter.getCard(8);
        setCoins(10, 5, 3, 1);
        CardAdapter.event(card, player0, player0, 7);
        testCoins(15, 3, 1, 0);
    }

    //Test für Karte 9 nur für Tutoren
    @Disabled("Private Test")
    @Test
    void c9() {
    }

    @Test
    void c10() {
        Card card = CardAdapter.getCard(10);
        Card farmCard = CardAdapter.getCard(2);
        PlayerAdapter.freeCard(player0, farmCard);
        CardAdapter.event(card, player0, player0, 7);
        testCoins(3, 0, 0, 0);
    }

    @Test
    void c11() {
        Card card = CardAdapter.getCard(11);
        Card supplyCard = CardAdapter.getCard(6);
        PlayerAdapter.freeCard(player0, supplyCard);
        CardAdapter.event(card, player0, player0, 8);
        testCoins(3, 0, 0, 0);
    }

    @Test
    void c12() {
        Card card = CardAdapter.getCard(12);
        CardAdapter.event(card, player0, player0, 9);
        testCoins(5, 0, 0, 0);
    }

    @Test
    void c13() {
        Card card = CardAdapter.getCard(13);
        setCoins(2, 0, 0, 0);
        CardAdapter.event(card, player0, player1, 10);
        testCoins(0, 2, 0, 0);
    }

    @Test
    void c14() {
        Card card = CardAdapter.getCard(14);
        CardAdapter.event(card, player0, player0, 10);
        testCoins(3, 0, 0, 0);
    }

    @Test
    void c15() {
        Card card = CardAdapter.getCard(15);
        Card supplyCard = CardAdapter.getCard(1);
        PlayerAdapter.freeCard(player0, supplyCard);
        CardAdapter.event(card, player0, player0, 11);
        testCoins(2, 0, 0, 0);
    }


    private void setCoins(int coins0, int coins1, int coins2, int coins3) {
        PlayerAdapter.setCoins(player0, coins0);
        PlayerAdapter.setCoins(player1, coins1);
        PlayerAdapter.setCoins(player2, coins2);
        PlayerAdapter.setCoins(player3, coins3);
    }

    private void testCoins(int coins0, int coins1, int coins2, int coins3) {
        assertEquals(coins0, PlayerAdapter.getCoins(player0));
        assertEquals(coins1, PlayerAdapter.getCoins(player1));
        assertEquals(coins2, PlayerAdapter.getCoins(player2));
        assertEquals(coins3, PlayerAdapter.getCoins(player3));
    }
}
