package de.henrik.implementation.card.playingcard;

import de.henrik.engine.card.Card;
import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.CardStack;
import de.henrik.implementation.GameEvent.*;
import de.henrik.implementation.card.BasicCard;
import de.henrik.implementation.player.PlayerImpl;
import de.henrik.implementation.player.PlayerPaneImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PlayingCardBuilder {
    private int id;
    private int cost;
    private CardClass cardClass;
    private CardType type;
    private GameImage back, front;
    private CardEventListener listener;

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
                    cardBuilder.setFront(new GameImage("/cards/c" + line[0] + ".png"));
                    cardBuilder.setBack(new GameImage("/cards/back.png"));
                    cardBuilder.setAction(getEventListener(cardBuilder.id));
                    for (int i = 0; i < Integer.parseInt(line[6]); i++) {
                        cards.add(cardBuilder.build());
                    }
                } catch (RuntimeException e) {
                    System.err.println("Failed to load card: " + l);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cards;
    }

    private static CardEventListener getEventListener(int id) {

        // TODO: 18.01.2023 implement card actions
        switch (id) {
            case 1:
                return event -> {
                    if (event.roll == 1) {
                        event.owner.addCoins(1);
                    }
                };
            case 2:
                return event -> {
                    if (event.roll == 2) {
                        event.owner.addCoins(2);
                    }
                };
            case 3:
                return event -> {
                    if ((event.roll == 2 || event.roll == 3) && event.activePlayer == event.owner) {
                        event.owner.addCoins(1);
                    }
                };
            case 4:
                return event -> {
                    if (event.roll == 3 && event.activePlayer != event.owner) {
                        event.owner.addCoins(event.activePlayer.removeCoins(1));
                    }
                };
            case 5:
                return event -> {
                    if (event.roll == 4 && event.activePlayer == event.owner) {
                        event.owner.addCoins(3);
                    }
                };
            case 6:
                return event -> {
                    if (event.roll == 5) {
                        event.owner.addCoins(3);
                    }
                };
            case 7:
                return event -> {
                    if (event.roll == 6 && event.owner == event.activePlayer) {
                        event.gameBoard.event(
                                new ChoiceEvent(
                                        gameComponent -> gameComponent instanceof PlayerPaneImpl &&
                                                gameComponent != event.activePlayer.getPlayerPane(),
                                        event1 -> event.owner.addCoins(((PlayerPaneImpl) event1.selected).getPlayer().removeCoins(5))));

                    }
                };
            case 8:
                return event -> {
                    if (event.roll == 6) {
                        for (PlayerImpl player : event.gameBoard.getPlayer()) {
                            if (player != event.owner) {
                                event.owner.addCoins(player.removeCoins(2));
                            }
                        }
                    }
                };
            case 9:
                return event -> {
                    if (event.roll == 6 && event.owner == event.activePlayer) {
                        event.gameBoard.event(
                                new ChoiceEvent(
                                        gameComponent -> gameComponent instanceof CardStack &&
                                                !event.gameBoard.drawStacks.getCardStacks().contains(gameComponent) &&
                                                event.owner.getCardStacks().contains(gameComponent),
                                        event1 -> new ChoiceEvent(gameComponent -> gameComponent instanceof CardStack &&
                                                !event.gameBoard.drawStacks.getCardStacks().contains(gameComponent) &&
                                                !event.owner.getCardStacks().contains(gameComponent),
                                                event2 -> {
                                                    event2.owner.addCard((PlayingCard) ((CardStack) event1.selected).removeCard());
                                                    event.owner.addCard((PlayingCard) ((CardStack) event2.selected).removeCard());
                                                }
                                        )
                                ));
                    }
                };

            default:
                return event -> {
                    System.out.println("CARD " + id + " EVENT");

                };
//                throw new IllegalArgumentException("This id is not a valid Card ID: " + id);
        }
    }

    public BasicCard build() {
        return new PlayingCard(id, cost, cardClass, type, front, back, listener);
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

    public PlayingCardBuilder setAction(CardEventListener listener) {
        this.listener = listener;
        return this;
    }
}
