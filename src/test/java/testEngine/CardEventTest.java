package testEngine;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.Card;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import testAdapter.CardAdapter;
import testAdapter.EventAdapter;
import testAdapter.PlayerAdapter;
import testAdapter.Provider;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testEngine.Util.arrayFromArrays;

public class CardEventTest {
    Player player0;
    Player player1;
    Player player2;
    Player player3;
    Game game;

    @Before
    void getPlayer() {
        Game game = Provider.getGame();
        player0 = PlayerAdapter.getPlayer(game, 0);
        player1 = PlayerAdapter.getPlayer(game, 1);
        player2 = PlayerAdapter.getPlayer(game, 2);
        player3 = PlayerAdapter.getPlayer(game, 3);
        setCoins(0,0,0,0);
    }

    @Test
    void c1() {
        testAllPermutationsGetCoinsPrimary(1, CardAdapter.getCard(1));
    }

    @Test
    void c2() {
        testAllPermutationsGetCoinsPrimary(1, CardAdapter.getCard(2));
    }

    @Test
    void c3() {
        testAllPermutationsGetCoinsSecondary(1, CardAdapter.getCard(3));
    }

    @Test
    void c4() {
        Card card = CardAdapter.getCard(4);
        testAllPermutationsGetCoinsRestaurants(1, card);
        testAllPermutationsGetCoinsPartially(1, card);
    }

    @Test
    void c5() {
        testAllPermutationsGetCoinsSecondary(3, CardAdapter.getCard(5));
    }

    @Test
    void c6() {
        testAllPermutationsGetCoinsPrimary(1, CardAdapter.getCard(6));
    }

    @Test
    void c7() {
        Card card = CardAdapter.getCard(7);
        setCoins(10, 10, 4, 0);
        CardAdapter.event(card, player0, player0, game);
        GameEvent event = Provider.getLastEvent();
        assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
        assertArrayEquals(EventAdapter.getChoiceEventChoices(), new GameComponent[]{player1.getPlayerPane(), player2.getPlayerPane(), player3.getPlayerPane()});
        EventAdapter.setChoiceEventChoice(player1.getPlayerPane());
        testCoins(15, 5, 4, 0);
        CardAdapter.event(card, player1, player1, game);
        event = Provider.getLastEvent();
        assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
        assertArrayEquals(EventAdapter.getChoiceEventChoices(), new GameComponent[]{player0.getPlayerPane(), player2.getPlayerPane(), player3.getPlayerPane()});
        testCoins(15, 9, 0, 0);
    }

    @Test
    void c8() {
        Card card = CardAdapter.getCard(8);
        setCoins(10, 5, 3, 1);
        CardAdapter.event(card, player0, player0, game);
        testCoins(15, 3, 1, 0);
        CardAdapter.event(card, player1, player1, game);
        testCoins(18, 1, 0, 0);
        CardAdapter.event(card, player2, player2, game);
        testCoins(19, 0, 0, 0);
    }

    void c9() {
        Card card = CardAdapter.getCard(9);
        Card tradeCard1 = CardAdapter.getCard(1);
        Card tradeCard2 = CardAdapter.getCard(2);
        Card SpaceStationCard = CardAdapter.getCard(7);
        PlayerAdapter.addCard(player0, tradeCard1);
        PlayerAdapter.addCard(player1, tradeCard2);
        PlayerAdapter.addCard(player2, SpaceStationCard);
        CardAdapter.event(card, player0, player0, game);
        GameEvent event = Provider.getLastEvent();
        assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
        assertArrayEquals(EventAdapter.getChoiceEventChoices(), arrayFromArrays(PlayerAdapter.getAllNonSpaceStationCardStacks(player1), PlayerAdapter.getAllNonSpaceStationCardStacks(player2), PlayerAdapter.getAllNonSpaceStationCardStacks(player3)));
        EventAdapter.setChoiceEventChoice(PlayerAdapter.getCardStack(player1, tradeCard2));
        event = Provider.getLastEvent();
        assertEquals(event.getName(), EventAdapter.getEventName(EventAdapter.EventType.ChoiceEvent));
        assertArrayEquals(EventAdapter.getChoiceEventChoices(), PlayerAdapter.getAllNonSpaceStationCardStacks(player0));
        EventAdapter.setChoiceEventChoice(PlayerAdapter.getCardStack(player0, tradeCard1));
        testCoins(0, 0, 0, 0);
        assert(player0.getCardList().contains(tradeCard2));
        assert(!player0.getCardList().contains(tradeCard1));
        assert(player1.getCardList().contains(tradeCard1));
        assert(!player1.getCardList().contains(tradeCard2));
        assert(player2.getCardList().contains(SpaceStationCard));
    }

    @Test
    void c10() {
        Card card = CardAdapter.getCard(10);
        Card farmCard = CardAdapter.getCard(2);
        PlayerAdapter.addCard(player0, farmCard);
        farmCard = CardAdapter.getCard(2);
        PlayerAdapter.addCard(player1, farmCard);
        farmCard = CardAdapter.getCard(2);
        PlayerAdapter.addCard(player1, farmCard);
        CardAdapter.event(card, player0, player0, game);
        testCoins(3,0,0,0);
        CardAdapter.event(card, player1, player1, game);
        testCoins(3, 6, 0, 0);
    }

    @Test
    void c11() {
        Card card = CardAdapter.getCard(11);
        Card supplyCard = CardAdapter.getCard(4);
        PlayerAdapter.addCard(player0, supplyCard);
        supplyCard = CardAdapter.getCard(4);
        PlayerAdapter.addCard(player1, supplyCard);
        supplyCard = CardAdapter.getCard(13);
        PlayerAdapter.addCard(player1, supplyCard);
        supplyCard = CardAdapter.getCard(4);
        PlayerAdapter.addCard(player2, supplyCard);
        supplyCard = CardAdapter.getCard(4);
        PlayerAdapter.addCard(player2, supplyCard);
        supplyCard = CardAdapter.getCard(13);
        PlayerAdapter.addCard(player2, supplyCard);
        supplyCard = CardAdapter.getCard(13);
        PlayerAdapter.addCard(player3, supplyCard);

        CardAdapter.event(card, player0, player0, game);
        testCoins(3,0,0,0);
        CardAdapter.event(card, player1, player1, game);
        testCoins(3, 6, 0, 0);
        CardAdapter.event(card, player2, player2, game);
        testCoins(3, 6, 12, 0);
    }

    @Test
    void c12(){
        testAllPermutationsGetCoinsPrimary(5, CardAdapter.getCard(12));
    }

    @Test
    void c13(){
        Card card = CardAdapter.getCard(13);
        testAllPermutationsGetCoinsRestaurants(2, card);
        testAllPermutationsGetCoinsPartially(2, card);
    }

    @Test
    void c14(){
        testAllPermutationsGetCoinsPrimary(3, CardAdapter.getCard(14));
    }

    @Test
    void c15(){
        Card card = CardAdapter.getCard(15);
        Card supplyCard = CardAdapter.getCard(1);
        PlayerAdapter.addCard(player0, supplyCard);
        supplyCard = CardAdapter.getCard(1);
        PlayerAdapter.addCard(player1, supplyCard);
        supplyCard = CardAdapter.getCard(14);
        PlayerAdapter.addCard(player1, supplyCard);
        supplyCard = CardAdapter.getCard(1);
        PlayerAdapter.addCard(player2, supplyCard);
        supplyCard = CardAdapter.getCard(1);
        PlayerAdapter.addCard(player2, supplyCard);
        supplyCard = CardAdapter.getCard(14);
        PlayerAdapter.addCard(player2, supplyCard);
        supplyCard = CardAdapter.getCard(14);
        PlayerAdapter.addCard(player3, supplyCard);

        CardAdapter.event(card, player0, player0, game);
        testCoins(2,0,0,0);
        CardAdapter.event(card, player1, player1, game);
        testCoins(2, 4, 0, 0);
        CardAdapter.event(card, player2, player2, game);
        testCoins(2, 4, 8, 0);
    }

    private void testAllPermutationsGetCoinsPrimary(int amount, Card card) {
        setCoins(0, 0, 0, 0);
        CardAdapter.event(card, player0, player0, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), amount);
        CardAdapter.event(card, player0, player1, game);
        assertEquals(PlayerAdapter.getCoins(player0), 2 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 2 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 2 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 2 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 2 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 2 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 2 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 2 * amount);
        CardAdapter.event(card, player0, player2, game);
        assertEquals(PlayerAdapter.getCoins(player0), 3 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 3 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 3 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 3 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 3 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 3 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 3 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 3 * amount);
        CardAdapter.event(card, player0, player3, game);
        assertEquals(PlayerAdapter.getCoins(player0), 4 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 4 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 4 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 4 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 4 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 4 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 4 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 4 * amount);
        CardAdapter.event(card, player1, player0, game);
        assertEquals(PlayerAdapter.getCoins(player0), 5 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 5 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 5 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 5 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 5 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 5 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 5 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 5 * amount);
        CardAdapter.event(card, player1, player1, game);
        assertEquals(PlayerAdapter.getCoins(player0), 6 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 6 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 6 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 6 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 6 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 6 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 6 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 6 * amount);
        CardAdapter.event(card, player1, player2, game);
        assertEquals(PlayerAdapter.getCoins(player0), 7 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 7 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 7 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 7 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 7 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 7 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 7 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 7 * amount);
        CardAdapter.event(card, player1, player3, game);
        assertEquals(PlayerAdapter.getCoins(player0), 8 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 8 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 8 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 8 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 8 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 8 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 8 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 8 * amount);
        CardAdapter.event(card, player2, player0, game);
        assertEquals(PlayerAdapter.getCoins(player0), 9 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 9 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 9 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 9 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 9 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 9 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 9 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 9 * amount);
        CardAdapter.event(card, player2, player1, game);
        assertEquals(PlayerAdapter.getCoins(player0), 10 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 10 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 10 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 10 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 10 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 10 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 10 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 10 * amount);
        CardAdapter.event(card, player2, player2, game);
        assertEquals(PlayerAdapter.getCoins(player0), 11 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 11 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 11 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 11 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 11 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 11 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 11 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 11 * amount);
        CardAdapter.event(card, player2, player3, game);
        assertEquals(PlayerAdapter.getCoins(player0), 12 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 12 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 12 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 12 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 12 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 12 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 12 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 12 * amount);
        CardAdapter.event(card, player3, player0, game);
        assertEquals(PlayerAdapter.getCoins(player0), 13 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 13 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 13 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 13 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 13 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 13 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 13 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 13 * amount);
        CardAdapter.event(card, player3, player1, game);
        assertEquals(PlayerAdapter.getCoins(player0), 14 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 14 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 14 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 14 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 14 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 14 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 14 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 14 * amount);
        CardAdapter.event(card, player3, player2, game);
        assertEquals(PlayerAdapter.getCoins(player0), 15 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 15 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 15 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 15 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 15 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 15 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 15 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 15 * amount);
        CardAdapter.event(card, player3, player3, game);
        assertEquals(PlayerAdapter.getCoins(player0), 16 * amount);
        assertEquals(PlayerAdapter.getCoins(player1), 16 * amount);
        assertEquals(PlayerAdapter.getCoins(player2), 16 * amount);
        assertEquals(PlayerAdapter.getCoins(player3), 16 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), 16 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 16 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 16 * amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 16 * amount);
    }

    private void testAllPermutationsGetCoinsSecondary(int amount, Card card) {
        setCoins(0, 0, 0, 0);
        CardAdapter.event(card, player0, player0, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player0, player1, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player0, player2, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player0, player3, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player1, player0, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player1, player1, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player1, player2, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player1, player3, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player2, player0, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player2, player1, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), 0);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), 0);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player2, player2, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player2, player3, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player3, player0, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player3, player1, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player3, player2, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), 0);
        CardAdapter.event(card, player3, player3, game);
        assertEquals(PlayerAdapter.getCoins(player0), amount);
        assertEquals(PlayerAdapter.getCoins(player1), amount);
        assertEquals(PlayerAdapter.getCoins(player2), amount);
        assertEquals(PlayerAdapter.getCoins(player3), amount);
        assertEquals(PlayerAdapter.getShownCoins(player0), amount);
        assertEquals(PlayerAdapter.getShownCoins(player1), amount);
        assertEquals(PlayerAdapter.getShownCoins(player2), amount);
        assertEquals(PlayerAdapter.getShownCoins(player3), amount);
    }

    private void testAllPermutationsGetCoinsRestaurants(int amount, Card card) {
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player0, game);
        testCoins(100, 100, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player1, game);
        testCoins(100 - amount, 100 + amount, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player2, game);
        testCoins(100 - amount, 100, 100 + amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player0, player3, game);
        testCoins(100 - amount, 100, 100, 100 + amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player0, game);
        testCoins(100 + amount, 100 - amount, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player1, game);
        testCoins(100, 100, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player2, game);
        testCoins(100, 100 - amount, 100 + amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player1, player3, game);
        testCoins(100, 100 - amount, 100, 100 + amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player0, game);
        testCoins(100 + amount, 100, 100 - amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player1, game);
        testCoins(100, 100 + amount, 100 - amount, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player2, game);
        testCoins(100, 100, 100, 100);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player2, player3, game);
        testCoins(100, 100, 100 - amount, 100 + amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player0, game);
        testCoins(100 + amount, 100, 100, 100 - amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player1, game);
        testCoins(100, 100 + amount, 100, 100 - amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player2, game);
        testCoins(100, 100, 100 + amount, 100 - amount);
        setCoins(100, 100, 100, 100);
        CardAdapter.event(card, player3, player3, game);
        testCoins(100, 100, 100, 100);
    }

    private void testAllPermutationsGetCoinsPartially(int amount, Card card) {
        for (int i = 0; i < amount; ) {
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player0, game);
            testCoins(i, i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player1, game);
            testCoins(0, 2 * i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player2, game);
            testCoins(0, i, 2 * i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player0, player3, game);
            testCoins(0, i, i, 2 * i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player0, game);
            testCoins(2 * i, 0, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player1, game);
            testCoins(i, i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player2, game);
            testCoins(i, 0, 2 * i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player1, player3, game);
            testCoins(i, 0, i, 2 * i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player0, game);
            testCoins(2 * i, i, 0, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player1, game);
            testCoins(i, 2 * i, 0, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player2, game);
            testCoins(i, i, i, i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player2, player3, game);
            testCoins(i, i, 0, 2 * i);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player0, game);
            testCoins(2 * i, i, i, 0);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player1, game);
            testCoins(i, 2 * i, i, 0);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player2, game);
            testCoins(i, i, 2 * i, 0);
            setCoins(i, i, i, i);
            CardAdapter.event(card, player3, player3, game);
            testCoins(i, i, i, i);
        }

        assertEquals(PlayerAdapter.getCoins(player0), 0);
        assertEquals(PlayerAdapter.getCoins(player1), 0);
        assertEquals(PlayerAdapter.getShownCoins(player0), 0);
        assertEquals(PlayerAdapter.getShownCoins(player1), 0);
    }

    private void setCoins(int coins0, int coins1, int coins2, int coins3) {
        PlayerAdapter.setCoins(player0, coins0);
        PlayerAdapter.setCoins(player1, coins1);
        PlayerAdapter.setCoins(player2, coins2);
        PlayerAdapter.setCoins(player3, coins3);
    }

    private void testCoins(int coins0, int coins1, int coins2, int coins3) {
        assertEquals(PlayerAdapter.getCoins(player0), coins0);
        assertEquals(PlayerAdapter.getCoins(player1), coins1);
        assertEquals(PlayerAdapter.getCoins(player2), coins2);
        assertEquals(PlayerAdapter.getCoins(player3), coins3);
        assertEquals(PlayerAdapter.getShownCoins(player0), coins0);
        assertEquals(PlayerAdapter.getShownCoins(player1), coins1);
        assertEquals(PlayerAdapter.getShownCoins(player2), coins2);
        assertEquals(PlayerAdapter.getShownCoins(player3), coins3);
    }


}
