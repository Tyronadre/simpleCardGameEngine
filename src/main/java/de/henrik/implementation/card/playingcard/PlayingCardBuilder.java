package de.henrik.implementation.card.playingcard;

import de.henrik.engine.card.Card;
import de.henrik.engine.base.GameImage;
import de.henrik.implementation.card.BasicCard;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class PlayingCardBuilder {
    private int id;
    private int cost;
    private CardClass cardClass;
    private CardType type;
    private GameImage back, front;
    private ActionListener action;

    public static List<Card> buildCardsFromCSV(String csv) {
        List<Card> cards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(PlayingCardBuilder.class.getResourceAsStream(csv))))) {
            String l;
            while ((l = reader.readLine()) != null) {
                try {
                    var line = l.split(",");
                    if (line.length != 7) {
                        System.err.println("Could not parse line, skipping: " + Arrays.toString(line));
                        continue;
                    }
                    var cardBuilder = new PlayingCardBuilder();
                    cardBuilder.setID(Integer.parseInt(line[0]));
                    cardBuilder.setType(switch (Integer.parseInt(line[1])) {
                        case 1 -> CardType.PRIMARY_INDUSTRY;
                        case 2 -> CardType.SECONDARY_INDUSTRY;
                        case 3 -> CardType.RESTAURANTS;
                        case 4 -> CardType.MAYOR_ESTABLISHMENT;
                        case 5 -> CardType.LANDMARK;
                        default -> throw new IllegalStateException("Unexpected value: " + line[2]);
                    });
                    cardBuilder.setCardClass(switch (line[2]) {
                        case "feld" -> CardClass.WHEAT;
                        case "tier" -> CardClass.COW;
                        case "laden" -> CardClass.SUITCASE;
                        case "resource" -> CardClass.GEAR;
                        case "factory" -> CardClass.FACTORY;
                        case "major" -> CardClass.MAJOR;
                        case "restaurant" -> CardClass.CUP;
                        default -> throw new IllegalStateException("Unexpected value: " + line[2]);
                    });
                    cardBuilder.setCost(Integer.parseInt(line[3]));
                    cardBuilder.setFront(new GameImage("/cards/" + line[4]));
                    cardBuilder.setBack(new GameImage("/cards/" + line[5]));
                    cardBuilder.setAction(getAction(line[0]));
                    for (int i = 0; i < Integer.parseInt(line[6]); i++) {
                        cards.add(cardBuilder.build());
                    }
                } catch (RuntimeException  e) {
                    System.err.println("Failed to load card: " + l);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cards;
    }

    private static ActionListener getAction(String cardName) {
        return e -> System.out.println(cardName);
    }

    public BasicCard build() {
        return new PlayingCard(id, cost, cardClass, type, front, back, action);
    }

    public PlayingCardBuilder setID(int id) {
        this.id = id;
        return this;
    }

    public PlayingCardBuilder setFront(GameImage front) {
        this.front = front;
        return this;
    }

    public PlayingCardBuilder setBack(GameImage back) {
        this.back = back;
        return this;
    }

    public PlayingCardBuilder setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public PlayingCardBuilder setCardClass(CardClass cardClass) {
        this.cardClass = cardClass;
        return this;
    }

    public PlayingCardBuilder setType(CardType type) {
        this.type = type;
        return this;
    }

    public PlayingCardBuilder setAction(ActionListener action) {
        this.action = action;
        return this;
    }
}
