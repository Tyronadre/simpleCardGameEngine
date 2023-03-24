package testAdapter;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.CardEvent;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.card.playingcard.CardClass;
import de.henrik.implementation.card.playingcard.CardType;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.card.playingcard.PlayingCardBuilder;
import de.henrik.implementation.game.Options;
import de.henrik.implementation.player.PlayerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class CardAdapter {
    static HashMap<Integer, PlayingCardBuilder> cardMap;
    private static boolean init = false;

    public static void init() {
        if (init) return;
        init = true;
        Options.setWidth(1920);
        Options.setHeight(1080);
        cardMap = new HashMap<>(42);
        loadCards("/cardsE0.csv");
        loadCards("/cardsE1.csv");
        loadCards("/cardsE2.csv");
    }

    public static void loadCards(String csv) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(PlayingCardBuilder.class.getResourceAsStream(csv))))) {
            String l;
            while ((l = reader.readLine()) != null) {
                try {
                    var line = l.split(",");
                    if (line.length != 5) {
                        System.err.println("Could not parse line, skipping: " + Arrays.toString(line));
                        continue;
                    }
                    PlayingCardBuilder cardBuilder = new PlayingCardBuilder().setID(Integer.parseInt(line[0])).setType(switch (Integer.parseInt(line[1])) {
                        case 1 -> CardType.PRIMARY_INDUSTRY;
                        case 2 -> CardType.SECONDARY_INDUSTRY;
                        case 3 -> CardType.SUPPLIER;
                        case 4 -> CardType.MAYOR_ESTABLISHMENT;
                        default -> throw new IllegalStateException("Unexpected value: " + line[2]);
                    }).setCardClass(switch (Integer.parseInt(line[2])) {
                        case 1 -> CardClass.ASTEROID;
                        case 2 -> CardClass.SPACE_FARM;
                        case 3 -> CardClass.SPACE_SHOP;
                        case 4 -> CardClass.SPACE_HARBOR;
                        case 5 -> CardClass.SPACE_FACTORY;
                        case 6 -> CardClass.SPACE_STATION;
                        case 7 -> CardClass.SUPPLY_FACTORY;
                        case 8 -> CardClass.SPACE_SHIP;
                        case 9 -> CardClass.SPACE_OFFICE;
                        default -> throw new IllegalStateException("Unexpected value: " + line[2]);
                    }).setCost(Integer.parseInt(line[3])).setFront(new GameImage("/cards/c" + line[0] + ".png")).setBack(new GameImage("/cards/back.png")).setAction(PlayingCardBuilder.getEventListener(Integer.parseInt(line[0])));
                    cardMap.put(Integer.parseInt(line[0]), cardBuilder);
                } catch (RuntimeException e) {
                    System.err.println("Failed to load card: " + l);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Card getCard(int id) {
        return cardMap.get(id).build();
    }

    public static int getCost(Card card) {
        return ((PlayingCard)card).getCost();
    }

    public static int getCardClassID(Card card) {
        return ((PlayingCard) card).getCardClass().ordinal() + 1;
    }

    public static int getCardTypeID(Card card) {
        return ((PlayingCard) card).getCardType().ordinal() + 1;
    }

    public static void event(Card card, Player activePlayer, Player eventOwner, int roll) {
        ((PlayingCard) card).event(new CardEvent((PlayerImpl) eventOwner,(PlayerImpl) activePlayer, roll, (GameBoard) Provider.game.getActiveGameBoard(), card));
    }
}
