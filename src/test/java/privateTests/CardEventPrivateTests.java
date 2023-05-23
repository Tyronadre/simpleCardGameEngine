package privateTests;

import TestUtil.Util;
import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.Card;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testAdapter.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static TestUtil.Util.arrayFromArrays;

public class CardEventPrivateTests {
    static Player player0;
    static Player player1;
    static Player player2;
    static Player player3;

    @BeforeAll()
    static void init() {
        Provider.init();
        CardAdapter.init();
        Provider.setGameState();
        player0 = PlayerAdapter.getPlayer(0);
        player1 = PlayerAdapter.getPlayer(1);
        player2 = PlayerAdapter.getPlayer(2);
        player3 = PlayerAdapter.getPlayer(3);
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

    @Nested
    class e0 {

        @Test
        void c1() {
            Card card = CardAdapter.getCard(1);
            testCardEventPrimary(1, List.of(1), card);
        }

        @Test
        void c2() {
            Card card = CardAdapter.getCard(2);
            testCardEventPrimary(1, List.of(2), card);
        }

        @Test
        void c3() {
            Card card = CardAdapter.getCard(3);
            testCardEventSecondary(1, List.of(2, 3), card);
        }

        @Test
        void c4() {
            Card card = CardAdapter.getCard(4);
            testAllPermutationsGetCoinsRestaurants(1, 3, card);
            testAllPermutationsGetCoinsPartially(1, 3, card);
        }

        @Test
        void c5() {
            Card card = CardAdapter.getCard(5);
            testCardEventSecondary(3, List.of(4), card);
        }

        @Test
        void c6() {
            Card card = CardAdapter.getCard(6);
            testCardEventPrimary(1, List.of(5), card);
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

            CardAdapter.event(card, player1, player1, 6);
            event = Provider.gameEventThread.getEvents().get(0);
            assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
            assertArrayEquals(new GameComponent[]{player0.getPlayerPane(), player2.getPlayerPane(), player3.getPlayerPane()}, EventAdapter.getChoiceEventChoices(new Player[]{player0, player1, player2, player3}, event));
            EventAdapter.setChoiceEventChoice(event, player2.getPlayerPane(), player2);
            Provider.gameEventThread.handleNextEvent();
            testCoins(15, 9, 0, 0);
        }

        @Test
        void c8() {
            System.out.println(Provider.gameBoard.getPlayers());
            Card card = CardAdapter.getCard(8);
            setCoins(10, 5, 3, 1);
            CardAdapter.event(card, player0, player0, 7);
            testCoins(15, 3, 1, 0);
            CardAdapter.event(card, player0, player0, 7);
            testCoins(18, 1, 0, 0);
            CardAdapter.event(card, player0, player0, 7);
            testCoins(19, 0, 0, 0);
        }

        @Test
        void c9() {
            Card card = CardAdapter.getCard(9);
            Card tradeCard1 = CardAdapter.getCard(1);
            Card tradeCard2 = CardAdapter.getCard(2);
            Card SpaceStationCard = CardAdapter.getCard(7);
            PlayerAdapter.freeCard(player0, tradeCard1);
            PlayerAdapter.freeCard(player1, tradeCard2);
            PlayerAdapter.freeCard(player2, SpaceStationCard);
            CardAdapter.event(card, player0, player0, 8);
            GameEvent event = Provider.gameEventThread.getEvents().get(0);
            assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
            assertArrayEquals(PlayerAdapter.getAllNonSpaceStationCardStacks(player0), EventAdapter.getChoiceEventChoices(new Player[]{player0, player1, player2, player3}, event));
            EventAdapter.setChoiceEventChoice(event, PlayerAdapter.getCardStack(player1, tradeCard2), player1);
            Provider.gameEventThread.handleNextEvent();

            event = Provider.gameEventThread.getEvents().get(0);
            assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
            assertArrayEquals(arrayFromArrays(PlayerAdapter.getAllNonSpaceStationCardStacks(player1), PlayerAdapter.getAllNonSpaceStationCardStacks(player2), PlayerAdapter.getAllNonSpaceStationCardStacks(player3)), EventAdapter.getChoiceEventChoices(new Player[]{player0, player1, player2, player3}, event));
            EventAdapter.setChoiceEventChoice(event, PlayerAdapter.getCardStack(player0, tradeCard1), player0);
            Provider.gameEventThread.handleNextEvent();

            testCoins(0, 0, 0, 0);
            assert (player0.getCardList().contains(tradeCard2));
            assert (!player0.getCardList().contains(tradeCard1));
            assert (player1.getCardList().contains(tradeCard1));
            assert (!player1.getCardList().contains(tradeCard2));
            assert (player2.getCardList().contains(SpaceStationCard));
        }

        @Test
        void c10() {
            Card card = CardAdapter.getCard(10);
            Card farmCard = CardAdapter.getCard(2);
            PlayerAdapter.freeCard(player0, farmCard);
            farmCard = CardAdapter.getCard(2);
            PlayerAdapter.freeCard(player1, farmCard);
            farmCard = CardAdapter.getCard(2);
            PlayerAdapter.freeCard(player1, farmCard);
            CardAdapter.event(card, player0, player0, 7);
            testCoins(3, 0, 0, 0);
            CardAdapter.event(card, player1, player1, 7);
            testCoins(3, 6, 0, 0);
        }

        @Test
        void c11() {
            Card card = CardAdapter.getCard(11);
            Card supplyCard = CardAdapter.getCard(6);
            PlayerAdapter.freeCard(player0, supplyCard);
            supplyCard = CardAdapter.getCard(12);
            PlayerAdapter.freeCard(player1, supplyCard);
            supplyCard = CardAdapter.getCard(6);
            PlayerAdapter.freeCard(player1, supplyCard);
            supplyCard = CardAdapter.getCard(6);
            PlayerAdapter.freeCard(player2, supplyCard);
            supplyCard = CardAdapter.getCard(6);
            PlayerAdapter.freeCard(player2, supplyCard);
            supplyCard = CardAdapter.getCard(12);
            PlayerAdapter.freeCard(player2, supplyCard);
            supplyCard = CardAdapter.getCard(12);
            PlayerAdapter.freeCard(player2, supplyCard);

            CardAdapter.event(card, player0, player0, 8);
            testCoins(3, 0, 0, 0);
            CardAdapter.event(card, player1, player1, 8);
            testCoins(3, 6, 0, 0);
            CardAdapter.event(card, player2, player2, 8);
            testCoins(3, 6, 12, 0);
        }

        @Test
        void c12() {
            testCardEventPrimary(5, List.of(9), CardAdapter.getCard(12));
        }

        @Test
        void c13() {
            Card card = CardAdapter.getCard(13);
            testAllPermutationsGetCoinsRestaurants(2, 9, card);
            testAllPermutationsGetCoinsPartially(2, 9, card);
            testAllPermutationsGetCoinsRestaurants(2, 10, card);
            testAllPermutationsGetCoinsPartially(2, 10, card);
        }

        @Test
        void c14() {
            testCardEventPrimary(3, List.of(10), CardAdapter.getCard(14));
        }

        @Test
        void c15() {
            Card card = CardAdapter.getCard(15);
            Card supplyCard = CardAdapter.getCard(1);
            PlayerAdapter.freeCard(player0, supplyCard);
            supplyCard = CardAdapter.getCard(1);
            PlayerAdapter.freeCard(player1, supplyCard);
            supplyCard = CardAdapter.getCard(14);
            PlayerAdapter.freeCard(player1, supplyCard);
            supplyCard = CardAdapter.getCard(1);
            PlayerAdapter.freeCard(player2, supplyCard);
            supplyCard = CardAdapter.getCard(1);
            PlayerAdapter.freeCard(player2, supplyCard);
            supplyCard = CardAdapter.getCard(14);
            PlayerAdapter.freeCard(player2, supplyCard);
            supplyCard = CardAdapter.getCard(14);
            PlayerAdapter.freeCard(player2, supplyCard);

            CardAdapter.event(card, player0, player0, 11);
            testCoins(2, 0, 0, 0);
            CardAdapter.event(card, player1, player1, 12);
            testCoins(2, 4, 0, 0);
            CardAdapter.event(card, player2, player2, 11);
            testCoins(2, 4, 8, 0);
        }
    }

    @Nested
    class e1 {

    }

    private void testAllPermutationsGetCoinsRestaurants(int amount, int activation, Card card) {
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player0, activation);
        testCoins(100, 100, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player1, activation);
        testCoins(100 - amount, 100 + amount, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player2, activation);
        testCoins(100 - amount, 100, 100 + amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player3, activation);
        testCoins(100 - amount, 100, 100, 100 + amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player0, activation);
        testCoins(100 + amount, 100 - amount, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player1, activation);
        testCoins(100, 100, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player2, activation);
        testCoins(100, 100 - amount, 100 + amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player3, activation);
        testCoins(100, 100 - amount, 100, 100 + amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player0, activation);
        testCoins(100 + amount, 100, 100 - amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player1, activation);
        testCoins(100, 100 + amount, 100 - amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player2, activation);
        testCoins(100, 100, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player3, activation);
        testCoins(100, 100, 100 - amount, 100 + amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player0, activation);
        testCoins(100 + amount, 100, 100, 100 - amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player1, activation);
        testCoins(100, 100 + amount, 100, 100 - amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player2, activation);
        testCoins(100, 100, 100 + amount, 100 - amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player3, activation);
        testCoins(100, 100, 100, 100);
    }

    private void testAllPermutationsGetCoinsPartially(int amount, int activation, Card card) {
        for (int i = 0; i < amount; i++) {
            System.out.println("testAllPermutationsGetCoinsPartially: " + i + " of CardID" + card.getID());
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player0, activation);
            testCoins(i, i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player1, activation);
            testCoins(0, 2 * i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player2, activation);
            testCoins(0, i, 2 * i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player3, activation);
            testCoins(0, i, i, 2 * i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player0, activation);
            testCoins(2 * i, 0, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player1, activation);
            testCoins(i, i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player2, activation);
            testCoins(i, 0, 2 * i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player3, activation);
            testCoins(i, 0, i, 2 * i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player0, activation);
            testCoins(2 * i, i, 0, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player1, activation);
            testCoins(i, 2 * i, 0, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player2, activation);
            testCoins(i, i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player3, activation);
            testCoins(i, i, 0, 2 * i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player0, activation);
            testCoins(2 * i, i, i, 0);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player1, activation);
            testCoins(i, 2 * i, i, 0);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player2, activation);
            testCoins(i, i, 2 * i, 0);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player3, activation);
            testCoins(i, i, i, i);
        }
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
        assertEquals(coins0, PlayerAdapter.getShownCoins(player0));
        assertEquals(coins1, PlayerAdapter.getShownCoins(player1));
        assertEquals(coins2, PlayerAdapter.getShownCoins(player2));
        assertEquals(coins3, PlayerAdapter.getShownCoins(player3));
    }

    private void testCardEventPrimary(int amount, List<Integer> activation, Card card) {
        int activated = 0;
        for (int i = 1; i <= 12; i++) {
            System.out.println("Testing primary card " + card.getID() + " with roll " + i);
            if (activation.contains(i)) {
                CardAdapter.event(card, player0, player0, i);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player1, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player0, player2, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player0, player3, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * (activated + 1));
                CardAdapter.event(card, player1, player0, i);
                testCoins(amount * (activated + 2), amount * (activated + 1), amount * (activated + 1), amount * (activated + 1));
                CardAdapter.event(card, player1, player1, i);
                testCoins(amount * (activated + 2), amount * (activated + 2), amount * (activated + 1), amount * (activated + 1));
                CardAdapter.event(card, player1, player2, i);
                testCoins(amount * (activated + 2), amount * (activated + 2), amount * (activated + 2), amount * (activated + 1));
                CardAdapter.event(card, player1, player3, i);
                testCoins(amount * (activated + 2), amount * (activated + 2), amount * (activated + 2), amount * (activated + 2));
                CardAdapter.event(card, player2, player0, i);
                testCoins(amount * (activated + 3), amount * (activated + 2), amount * (activated + 2), amount * (activated + 2));
                CardAdapter.event(card, player2, player1, i);
                testCoins(amount * (activated + 3), amount * (activated + 3), amount * (activated + 2), amount * (activated + 2));
                CardAdapter.event(card, player2, player2, i);
                testCoins(amount * (activated + 3), amount * (activated + 3), amount * (activated + 3), amount * (activated + 2));
                CardAdapter.event(card, player2, player3, i);
                testCoins(amount * (activated + 3), amount * (activated + 3), amount * (activated + 3), amount * (activated + 3));
                CardAdapter.event(card, player3, player0, i);
                testCoins(amount * (activated + 4), amount * (activated + 3), amount * (activated + 3), amount * (activated + 3));
                CardAdapter.event(card, player3, player1, i);
                testCoins(amount * (activated + 4), amount * (activated + 4), amount * (activated + 3), amount * (activated + 3));
                CardAdapter.event(card, player3, player2, i);
                testCoins(amount * (activated + 4), amount * (activated + 4), amount * (activated + 4), amount * (activated + 3));
                CardAdapter.event(card, player3, player3, i);
                testCoins(amount * (activated + 4), amount * (activated + 4), amount * (activated + 4), amount * (activated + 4));
                activated += 4;

            } else {
                CardAdapter.event(card, player0, player0, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player1, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player2, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player3, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player0, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player1, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player2, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player3, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player0, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player1, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player2, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player3, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player0, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player1, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player2, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player3, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
            }
        }
    }

    private void testCardEventSecondary(int amount, List<Integer> activation, Card card) {
        int activated = 0;
        for (int i = 1; i <= 12; i++) {
            System.out.println("Testing secondary card " + card.getID() + " with roll " + i);
            if (activation.contains(i)) {
                CardAdapter.event(card, player0, player0, i);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player1, i);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player2, i);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player3, i);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player0, i);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player1, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player1, player2, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player1, player3, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player2, player0, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player2, player1, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player2, player2, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player2, player3, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player3, player0, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player3, player1, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player3, player2, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player3, player3, i);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * (activated + 1));
                activated++;
            } else {
                CardAdapter.event(card, player0, player0, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player1, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player2, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player3, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player0, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player1, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player2, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player3, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player0, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player1, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player2, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player3, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player0, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player1, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player2, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player3, i);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
            }
        }
    }
}
