package testAdapter;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.implementation.game.DrawStacks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DrawStacksAdapter {
    /**
     *
     * @return all cards on the draw stack
     */
    public static List<Card> getAllCards() {
        var list = new ArrayList<Card>();
        list.addAll(Provider.gameBoard.drawStacks.getDrawStack());
        for (var stack : Provider.gameBoard.drawStacks.getCardStacks()) {
            list.addAll(stack.getCards());
        }
        return list;
    }

    /**
     *
     * @return all draw stacks.
     */
    public static Collection<CardStack> getDrawStacks() {
        return Provider.gameBoard.drawStacks.getCardStacks();
    }

    /**
     * Simulates a draw of a Card but just removes the first card from the first stack
     */
    public static void drawCard() {
        Provider.gameBoard.drawStacks.getCardStacks().get(0).removeCard();
        Provider.gameBoard.drawStacks.fillDrawStacks();
    }
}
