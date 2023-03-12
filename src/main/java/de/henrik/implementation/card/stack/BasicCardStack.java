package de.henrik.implementation.card.stack;

import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;

public class BasicCardStack extends CardStack {
    boolean usable = true;

    public BasicCardStack(String name, Card allowedCardType, int maxStackSize) {
        super(name, RP_ALL_CARDS_TURNED, card -> card.equals(allowedCardType), maxStackSize);
    }

    public BasicCardStack(String name, int maxStackSize) {
        super(name, RP_ALL_CARDS_TURNED, card -> true, maxStackSize);
    }

//    @Override
//    public void paint(GameGraphics g) {
//        super.paint(g);
//        Card topCard = getCard();
//        if (usable && Game.isRunning() && topCard != null) {
//            g.setColor(new Color(50, 168, 90));
//            BasicStroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);
//            g.getGraphics().setStroke(dashed);
//            g.setClip(topCard.getClip());
//            g.getGraphics().drawRoundRect(topCard.getX()+4, topCard.getY()+4, topCard.getWidth()-5, topCard.getHeight()-5, 5, 5);
//        }
//    }
}
