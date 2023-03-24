package tests;


import de.henrik.engine.card.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testAdapter.CardAdapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardBuilderTests {

    @BeforeAll
    static void init() {
        CardAdapter.init();
    }

    @Test
    void test_card_e0_variables() {
        for (int i = 1; i <= 15; i++) {
            System.out.println("Testing Card " + i);
            Card card = CardAdapter.getCard(i);
            assertEquals(i, card.getID());
            assertEquals("/cards/back.png", card.getBackOfCard().getPath());
            assertEquals("/cards/c" + i + ".png", card.getFrontOfCard().getPath());
            assertEquals(switch (i) {
                case 1, 2, 3 -> 1;
                case 4, 5, 15 -> 2;
                case 6, 11, 13, 14 -> 3;
                case 10 -> 5;
                case 7, 12 -> 6;
                case 8 -> 7;
                case 9 -> 8;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, CardAdapter.getCost(card));
            assertEquals(switch (i) {
                case 1, 2, 6, 12, 14 -> 1;
                case 3, 5, 10, 11, 15 -> 2;
                case 4, 13 -> 3;
                case 7, 8, 9 -> 4;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, CardAdapter.getCardTypeID(card));
            assertEquals(switch (i) {
                case 1, 14 -> 1;
                case 2 -> 2;
                case 3, 5, 15 -> 3;
                case 6, 12 -> 4;
                case 10, 11 -> 5;
                case 7, 8, 9 -> 6;
                case 4, 13 -> 7;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, CardAdapter.getCardClassID(card));

        }
    }

    @Test
    void test_card_e1_variables() {
        for (int i = 20; i <= 29; i++) {
            System.out.println("Testing Card " + i);
            Card card = CardAdapter.getCard(i);
            assertEquals(i, card.getID());
            assertEquals("/cards/back.png", card.getBackOfCard().getPath());
            assertEquals("/cards/c" + i + ".png", card.getFrontOfCard().getPath());
            assertEquals(switch (i) {
                case 20, 21, 24 -> 2;
                case 22, 28 -> 5;
                case 23, 26, 27 -> 1;
                case 25, 29 -> 4;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, CardAdapter.getCost(card));
            assertEquals(switch (i) {
                case 20, 21, 22 -> 1;
                case 23, 24 -> 2;
                case 25, 26, 27 -> 3;
                case 28, 29 -> 4;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, CardAdapter.getCardTypeID(card));
            assertEquals(switch (i) {
                case 20 -> 1;
                case 21, 22 -> 8;
                case 23 -> 3;
                case 24 -> 5;
                case 25,26,27 -> 7;
                case 28,29 -> 6;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, CardAdapter.getCardClassID(card));
        }
    }

    @Test
    void test_card_e2_variables() {
        for (int i = 33; i <= 44; i++) {
            System.out.println("Testing Card " + i);
            Card card = CardAdapter.getCard(i);
            assertEquals(i, card.getID());
            assertEquals("/cards/back.png", card.getBackOfCard().getPath());
            assertEquals("/cards/c" + i + ".png", card.getFrontOfCard().getPath());
            assertEquals(switch (i) {
                case 37 -> -5;
                case 35 -> 0;
                case 33,36,39 -> 2;
                case 34,38,41,43 -> 3;
                case 42, 44 -> 4;
                case 40 -> 5;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, CardAdapter.getCost(card));
            assertEquals(switch (i) {
                case 33, 34 -> 1;
                case 35,36 ,37 ,38 ,39, 40 -> 2;
                case 41, 42 -> 3;
                case 43, 44 -> 4;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, CardAdapter.getCardTypeID(card));
            assertEquals(switch (i) {
                case 33, 34 -> 1;
                case 35 -> 3;
                case 38, 40 -> 5;
                case 43,44 -> 6;
                case 41,42 -> 7;
                case 36,37,39 -> 9;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, CardAdapter.getCardClassID(card));
        }
    }

}
