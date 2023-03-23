package testEngine;


import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.game.Game;
import org.junit.jupiter.api.Test;
import testAdapter.CardAdapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    @Test
    void test_card_e0_variables() {
        for (int i = 1; i < 15; i++) {
            Card card = CardAdapter.getCard(i);
            assertEquals(i, card.getID());
            assertEquals(card.getBackOfCard(), new GameImage("cards/back.png").getScaledInstance(card.getWidth(), card.getHeight()));
            assertEquals(card.getFrontOfCard(), new GameImage("cards/c" + i + ".png").getScaledInstance(card.getWidth(), card.getHeight()));
            assertEquals(CardAdapter.getCost(card),switch (i){
                case 1,2,3->1;
                case 4,5,15 ->2;
                case 6,11,13,14 ->3;
                case 10 -> 5;
                case 7,12 -> 6;
                case 8 -> 7;
                case 9 -> 8;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            });
            assertEquals(CardAdapter.getCardClassID(card), switch (i){
                case 1,2,6,12,14->1;
                case 3,5,10,11,15->2;
                case 4,13->3;
                case 7,8,9->4;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            });
            assertEquals(CardAdapter.getCardTypeID(card), switch (i){
                case 1,14 -> 1;
                case 2 -> 2;
                case 3,5,15->3;
                case 6,12 -> 4;
                case 4,13 -> 5;
                case 7,8,9 -> 6;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            });

        }
    }

}
