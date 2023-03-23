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
        for (int i = 1; i < 15; i++) {
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

}
