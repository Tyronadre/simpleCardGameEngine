package de.henrik.implementation.card.stack;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.components.Pane;
import de.henrik.engine.events.GameMouseListenerAdapter;
import de.henrik.engine.game.Game;
import de.henrik.implementation.game.Options;

import java.awt.event.MouseEvent;

public class BasicCardStack extends CardStack {

    public BasicCardStack(String name, Card allowedCardType, int maxStackSize) {
        super(name, RP_ALL_CARDS_TURNED, card -> card.equals(allowedCardType), maxStackSize);
        magnifyCards();
    }

    private void magnifyCards() {
        addMouseListener(new GameMouseListenerAdapter(){
            Pane largeCard = null;
            boolean visibleBefore = false;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (largeCard == null && getCard() != null) {
                    largeCard = new Pane(getRenderPolicy() == RP_ALL_CARDS_UNTURNED ? getCard().getBackOfCard() : getCard().getFrontOfCard(),
                            Options.getWidth() / 5,
                            Options.getHeight() / 50,
                            866,
                            1300);
                    largeCard.setVisible(false);
                    Game.game.getActiveGameBoard().add(largeCard);
                }
                if (largeCard != null && !visibleBefore) {
                    largeCard.setVisible(true);
                    visibleBefore = false;
                    Game.game.getActiveGameBoard().addMouseListener(new GameMouseListenerAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (largeCard != null) {
                                largeCard.setVisible(false);
                                visibleBefore = true;
                            }
                            Game.game.getActiveGameBoard().removeMouseListener(this);
                        }
                    });
                } else if (largeCard != null) {
                    visibleBefore = false;
                }
            }
        });

    }

    public BasicCardStack(String name, int maxStackSize) {
        super(name, RP_ALL_CARDS_TURNED, card -> true, maxStackSize);
        magnifyCards();
    }
}
