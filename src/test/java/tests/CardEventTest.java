package tests;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.Card;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testAdapter.CardAdapter;
import testAdapter.EventAdapter;
import testAdapter.PlayerAdapter;
import testAdapter.Provider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static TestUtil.Util.arrayFromArrays;

public class CardEventTest {
    static Player player0;
    static Player player1;
    static Player player2;
    static Player player3;
    static Game game;

    @BeforeAll()
    static void init() {
        Provider.init();
        CardAdapter.init();
        Provider.setGameState();
        game = Provider.game;
        player0 = PlayerAdapter.getPlayer(game, 0);
        player1 = PlayerAdapter.getPlayer(game, 1);
        player2 = PlayerAdapter.getPlayer(game, 2);
        player3 = PlayerAdapter.getPlayer(game, 3);
    }

    @BeforeEach
    void resetPlayers() {
        setCoins(0, 0, 0, 0);
        PlayerAdapter.clearCards(player0);
        PlayerAdapter.clearCards(player1);
        PlayerAdapter.clearCards(player2);
        PlayerAdapter.clearCards(player3);
    }

    @Test
    void c1() {
        testCardEventPrimary(1, List.of(1), CardAdapter.getCard(1));
//        testAllPermutationsGetCoinsPrimary(1, 1, CardAdapter.getCard(1));
    }

    @Test
    void c2() {
        testCardEventPrimary(1, List.of(2), CardAdapter.getCard(2));
//        testAllPermutationsGetCoinsPrimary(1, 2, CardAdapter.getCard(2));
    }

    @Test
    void c3() {
        testCardEventSecondary(1, List.of(2, 3), CardAdapter.getCard(3));
    }

    @Test
    void c4() {
        Card card = CardAdapter.getCard(4);
        testAllPermutationsGetCoinsRestaurants(1, 3, card);
        testAllPermutationsGetCoinsPartially(1, 3, card);
    }

    @Test
    void c5() {
        testCardEventSecondary(3, List.of(4), CardAdapter.getCard(5));
//        testAllPermutationsGetCoinsSecondary(3, 4, CardAdapter.getCard(5));
    }

    @Test
    void c6() {
        testCardEventPrimary(1, List.of(5), CardAdapter.getCard(6));
//        testAllPermutationsGetCoinsPrimary(1, 5, CardAdapter.getCard(6));
    }

    @Test
    void c7() {
        Card card = CardAdapter.getCard(7);
        setCoins(10, 10, 4, 0);
        CardAdapter.event(card, player0, player0, 6, game);
        GameEvent event = Provider.gameEventThread.getEvents().get(0);

        assertEquals(EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent), event.getName());
        assertArrayEquals(new GameComponent[]{player1.getPlayerPane(), player2.getPlayerPane(), player3.getPlayerPane()}, EventAdapter.getChoiceEventChoices(new Player[]{player0, player1, player2, player3}, event));
        EventAdapter.setChoiceEventChoice(event, player1.getPlayerPane(), player1);
        testCoins(15, 5, 4, 0);
        CardAdapter.event(card, player1, player1, 6, game);
        event = Provider.gameEventThread.getEvents().get(0);
        assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
        assertArrayEquals(new GameComponent[]{player0.getPlayerPane(), player2.getPlayerPane(), player3.getPlayerPane()}, EventAdapter.getChoiceEventChoices(new Player[]{player0, player1, player2, player3}, event));
        EventAdapter.setChoiceEventChoice(event, player2.getPlayerPane(), player2);
        testCoins(15, 9, 0, 0);
    }

    @Test
    void c8() {
        Card card = CardAdapter.getCard(8);
        setCoins(10, 5, 3, 1);
        CardAdapter.event(card, player0, player0, 7, game);
        testCoins(15, 3, 1, 0);
        CardAdapter.event(card, player0, player0, 7, game);
        testCoins(18, 1, 0, 0);
        CardAdapter.event(card, player0, player0, 7, game);
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
        CardAdapter.event(card, player0, player0, 8, game);
        GameEvent event = Provider.gameEventThread.getEvents().get(0);
        assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
        assertArrayEquals(PlayerAdapter.getAllNonSpaceStationCardStacks(player0), EventAdapter.getChoiceEventChoices(new Player[]{player0, player1, player2, player3}, event));
        EventAdapter.setChoiceEventChoice(event, PlayerAdapter.getCardStack(player1, tradeCard2), player1);
        event = Provider.gameEventThread.getEvents().get(0);
        assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
        assertArrayEquals(arrayFromArrays(PlayerAdapter.getAllNonSpaceStationCardStacks(player1), PlayerAdapter.getAllNonSpaceStationCardStacks(player2), PlayerAdapter.getAllNonSpaceStationCardStacks(player3)), EventAdapter.getChoiceEventChoices(new Player[]{player0, player1, player2, player3}, event));
        EventAdapter.setChoiceEventChoice(event, PlayerAdapter.getCardStack(player0, tradeCard1), player0);
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
        CardAdapter.event(card, player0, player0, 7, game);
        testCoins(3, 0, 0, 0);
        CardAdapter.event(card, player1, player1, 7, game);
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

        CardAdapter.event(card, player0, player0, 8, game);
        testCoins(3, 0, 0, 0);
        CardAdapter.event(card, player1, player1, 8, game);
        testCoins(3, 6, 0, 0);
        CardAdapter.event(card, player2, player2, 8, game);
        testCoins(3, 6, 12, 0);
    }

    @Test
    void c12() {
        testCardEventPrimary(5, List.of(9), CardAdapter.getCard(12));
//        testAllPermutationsGetCoinsPrimary(5, 9, CardAdapter.getCard(12));
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

//        testAllPermutationsGetCoinsPrimary(3, 10, CardAdapter.getCard(14));
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

        CardAdapter.event(card, player0, player0, 11, game);
        testCoins(2, 0, 0, 0);
        CardAdapter.event(card, player1, player1, 12, game);
        testCoins(2, 4, 0, 0);
        CardAdapter.event(card, player2, player2, 11, game);
        testCoins(2, 4, 8, 0);
    }

    private void testAllPermutationsGetCoinsPrimary(int amount, int activation, Card card) {
        setCoins(0, 0, 0, 0);
        CardAdapter.event(card, player0, player0, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), amount);
        CardAdapter.event(card, player0, player1, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 2 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 2 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 2 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 2 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 2 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 2 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 2 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 2 * amount);
        CardAdapter.event(card, player0, player2, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 3 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 3 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 3 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 3 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 3 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 3 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 3 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 3 * amount);
        CardAdapter.event(card, player0, player3, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 4 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 4 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 4 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 4 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 4 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 4 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 4 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 4 * amount);
        CardAdapter.event(card, player1, player0, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 5 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 5 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 5 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 5 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 5 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 5 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 5 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 5 * amount);
        CardAdapter.event(card, player1, player1, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 6 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 6 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 6 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 6 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 6 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 6 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 6 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 6 * amount);
        CardAdapter.event(card, player1, player2, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 7 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 7 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 7 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 7 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 7 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 7 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 7 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 7 * amount);
        CardAdapter.event(card, player1, player3, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 8 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 8 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 8 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 8 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 8 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 8 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 8 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 8 * amount);
        CardAdapter.event(card, player2, player0, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 9 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 9 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 9 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 9 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 9 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 9 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 9 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 9 * amount);
        CardAdapter.event(card, player2, player1, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 10 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 10 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 10 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 10 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 10 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 10 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 10 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 10 * amount);
        CardAdapter.event(card, player2, player2, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 11 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 11 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 11 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 11 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 11 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 11 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 11 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 11 * amount);
        CardAdapter.event(card, player2, player3, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 12 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 12 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 12 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 12 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 12 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 12 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 12 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 12 * amount);
        CardAdapter.event(card, player3, player0, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 13 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 13 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 13 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 13 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 13 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 13 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 13 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 13 * amount);
        CardAdapter.event(card, player3, player1, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 14 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 14 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 14 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 14 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 14 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 14 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 14 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 14 * amount);
        CardAdapter.event(card, player3, player2, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 15 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 15 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 15 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 15 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 15 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 15 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 15 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 15 * amount);
        CardAdapter.event(card, player3, player3, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), 16 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 16 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 16 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 16 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 16 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 16 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 16 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 16 * amount);
    }

    private void testAllPermutationsGetCoinsSecondary(int amount, int activation, Card card) {
        setCoins(0, 0, 0, 0);
        CardAdapter.event(card, player0, player0, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player0, player1, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player0, player2, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player0, player3, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player1, player0, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player1, player1, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player1, player2, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player1, player3, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player2, player0, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player2, player1, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player2, player2, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player2, player3, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player3, player0, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player3, player1, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player3, player2, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player3, player3, activation, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), amount);
    }

    private void testAllPermutationsGetCoinsRestaurants(int amount, int activation, Card card) {
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player0, activation, game);
        testCoins(100, 100, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player1, activation, game);
        testCoins(100 - amount, 100 + amount, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player2, activation, game);
        testCoins(100 - amount, 100, 100 + amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player3, activation, game);
        testCoins(100 - amount, 100, 100, 100 + amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player0, activation, game);
        testCoins(100 + amount, 100 - amount, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player1, activation, game);
        testCoins(100, 100, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player2, activation, game);
        testCoins(100, 100 - amount, 100 + amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player3, activation, game);
        testCoins(100, 100 - amount, 100, 100 + amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player0, activation, game);
        testCoins(100 + amount, 100, 100 - amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player1, activation, game);
        testCoins(100, 100 + amount, 100 - amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player2, activation, game);
        testCoins(100, 100, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player3, activation, game);
        testCoins(100, 100, 100 - amount, 100 + amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player0, activation, game);
        testCoins(100 + amount, 100, 100, 100 - amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player1, activation, game);
        testCoins(100, 100 + amount, 100, 100 - amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player2, activation, game);
        testCoins(100, 100, 100 + amount, 100 - amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player3, activation, game);
        testCoins(100, 100, 100, 100);
    }

    private void testAllPermutationsGetCoinsPartially(int amount, int activation, Card card) {
        for (int i = 0; i < amount; i++) {
            System.out.println("testAllPermutationsGetCoinsPartially: " + i + " of CardID" + card.getID());
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player0, activation, game);
            testCoins(i, i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player1, activation, game);
            testCoins(0, 2 * i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player2, activation, game);
            testCoins(0, i, 2 * i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player3, activation, game);
            testCoins(0, i, i, 2 * i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player0, activation, game);
            testCoins(2 * i, 0, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player1, activation, game);
            testCoins(i, i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player2, activation, game);
            testCoins(i, 0, 2 * i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player3, activation, game);
            testCoins(i, 0, i, 2 * i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player0, activation, game);
            testCoins(2 * i, i, 0, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player1, activation, game);
            testCoins(i, 2 * i, 0, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player2, activation, game);
            testCoins(i, i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player3, activation, game);
            testCoins(i, i, 0, 2 * i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player0, activation, game);
            testCoins(2 * i, i, i, 0);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player1, activation, game);
            testCoins(i, 2 * i, i, 0);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player2, activation, game);
            testCoins(i, i, 2 * i, 0);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player3, activation, game);
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
                CardAdapter.event(card, player0, player0, i, game);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player1, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player0, player2, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player0, player3, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * (activated + 1));
                CardAdapter.event(card, player1, player0, i, game);
                testCoins(amount * (activated + 2), amount * (activated + 1), amount * (activated + 1), amount * (activated + 1));
                CardAdapter.event(card, player1, player1, i, game);
                testCoins(amount * (activated + 2), amount * (activated + 2), amount * (activated + 1), amount * (activated + 1));
                CardAdapter.event(card, player1, player2, i, game);
                testCoins(amount * (activated + 2), amount * (activated + 2), amount * (activated + 2), amount * (activated + 1));
                CardAdapter.event(card, player1, player3, i, game);
                testCoins(amount * (activated + 2), amount * (activated + 2), amount * (activated + 2), amount * (activated + 2));
                CardAdapter.event(card, player2, player0, i, game);
                testCoins(amount * (activated + 3), amount * (activated + 2), amount * (activated + 2), amount * (activated + 2));
                CardAdapter.event(card, player2, player1, i, game);
                testCoins(amount * (activated + 3), amount * (activated + 3), amount * (activated + 2), amount * (activated + 2));
                CardAdapter.event(card, player2, player2, i, game);
                testCoins(amount * (activated + 3), amount * (activated + 3), amount * (activated + 3), amount * (activated + 2));
                CardAdapter.event(card, player2, player3, i, game);
                testCoins(amount * (activated + 3), amount * (activated + 3), amount * (activated + 3), amount * (activated + 3));
                CardAdapter.event(card, player3, player0, i, game);
                testCoins(amount * (activated + 4), amount * (activated + 3), amount * (activated + 3), amount * (activated + 3));
                CardAdapter.event(card, player3, player1, i, game);
                testCoins(amount * (activated + 4), amount * (activated + 4), amount * (activated + 3), amount * (activated + 3));
                CardAdapter.event(card, player3, player2, i, game);
                testCoins(amount * (activated + 4), amount * (activated + 4), amount * (activated + 4), amount * (activated + 3));
                CardAdapter.event(card, player3, player3, i, game);
                testCoins(amount * (activated + 4), amount * (activated + 4), amount * (activated + 4), amount * (activated + 4));
                activated += 4;

            } else {
                CardAdapter.event(card, player0, player0, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player1, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player2, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player3, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player0, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player1, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player2, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player3, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player0, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player1, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player2, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player3, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player0, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player1, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player2, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player3, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
            }
        }
    }

    private void testCardEventSecondary(int amount, List<Integer> activation, Card card) {
        int activated = 0;
        for (int i = 1; i <= 12; i++) {
            System.out.println("Testing secondary card " + card.getID() + " with roll " + i);
            if (activation.contains(i)) {
                CardAdapter.event(card, player0, player0, i, game);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player1, i, game);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player2, i, game);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player3, i, game);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player0, i, game);
                testCoins(amount * (activated + 1), amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player1, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player1, player2, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player1, player3, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player2, player0, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player2, player1, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * activated, amount * activated);
                CardAdapter.event(card, player2, player2, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player2, player3, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player3, player0, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player3, player1, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player3, player2, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * activated);
                CardAdapter.event(card, player3, player3, i, game);
                testCoins(amount * (activated + 1), amount * (activated + 1), amount * (activated + 1), amount * (activated + 1));
                activated++;
            } else {
                CardAdapter.event(card, player0, player0, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player1, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player2, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player0, player3, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player0, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player1, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player2, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player1, player3, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player0, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player1, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player2, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player2, player3, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player0, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player1, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player2, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
                CardAdapter.event(card, player3, player3, i, Game.game);
                testCoins(amount * activated, amount * activated, amount * activated, amount * activated);
            }
        }
    }

}
