package de.henrik.implementation.card.playingcard;

import de.henrik.engine.card.Card;
import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.game.Border;
import de.henrik.engine.game.Game;
import de.henrik.implementation.GameEvent.*;
import de.henrik.implementation.card.BasicCard;
import de.henrik.implementation.player.PlayerImpl;
import de.henrik.implementation.player.PlayerPaneImpl;

import java.awt.*;
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
    private CardEventListener listener;

    public static List<Card> buildCardsFromCSV(String csv) {
        List<Card> cards = new ArrayList<>();
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
                    }).setCost(Integer.parseInt(line[3])).setFront(new GameImage("/cards/c" + line[0] + ".png")).setBack(new GameImage("/cards/back.png")).setAction(getEventListener(Integer.parseInt(line[0])));
                    for (int i = 0; i < Integer.parseInt(line[4]); i++) {
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

    public static CardEventListener getEventListener(int id) {

        // TODO: 18.01.2023 implement card actions
        return switch (id) {
            case 1 -> event -> {
                if (event.roll == 1) {
                    event.owner.addCoins(1);
                }
            };
            case 2 -> event -> {
                if (event.roll == 2) {
                    event.owner.addCoins(1);
                }
            };
            case 3 -> event -> {
                if ((event.roll == 2 || event.roll == 3) && event.activePlayer == event.owner) {
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(2);
                    else event.owner.addCoins(1);
                }
            };
            case 4 -> event -> {
                if (event.roll == 3 && event.activePlayer != event.owner) {
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(event.activePlayer.removeCoins(2));
                    else event.owner.addCoins(event.activePlayer.removeCoins(1));
                }
            };
            case 5 -> event -> {
                if (event.roll == 4 && event.activePlayer == event.owner) {
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(4);
                    else event.owner.addCoins(3);
                }
            };
            case 6 -> event -> {
                if (event.roll == 5) {
                    event.owner.addCoins(1);
                }
            };
            case 7 -> event -> {
                if (event.roll == 6 && event.owner == event.activePlayer) {
                    event.card.setBorder(new Border(Color.GREEN, false, 2, 3));
                    event.card.repaint();
                    Game.game.event(new ChoiceEvent(gameComponent -> gameComponent instanceof PlayerPaneImpl && gameComponent != event.activePlayer.getPlayerPane(), event1 -> {
                        event.owner.addCoins(((PlayerPaneImpl) event1.selected).getPlayer().removeCoins(5));
                        event.card.setBorder(null);
                        event.card.repaint();
                        Game.game.setWaitForEvent(false);
                    }));

                }
            };
            case 8 -> event -> {
                if (event.roll == 7) {
                    for (PlayerImpl player : event.gameBoard.getPlayers()) {
                        if (player != event.owner) {
                            event.owner.addCoins(player.removeCoins(2));
                        }
                    }
                }
            };
            case 9 -> event -> {
                if (event.roll == 8 && event.owner == event.activePlayer) {
                    event.card.setBorder(new Border(Color.GREEN, false, 2, 3));
                    event.card.repaint();
                    Game.game.event(new ChoiceEvent(gameComponent -> gameComponent instanceof CardStack cardStack && !event.gameBoard.drawStacks.getCardStacks().contains(gameComponent) && event.owner.getCardStacks().contains(gameComponent) && cardStack.getCard() != null && ((BasicCard) cardStack.getCard()).getCardType() != CardType.MAYOR_ESTABLISHMENT, event1 -> {
                        event1.selected.setBorder(new Border(Color.BLUE, false, 2, 3));
                        event1.selected.repaint();
                        Game.game.forceEvent(new ChoiceEvent(gameComponent -> gameComponent instanceof CardStack cardStack && !event.gameBoard.drawStacks.getCardStacks().contains(gameComponent) && !event.owner.getCardStacks().contains(gameComponent) && cardStack.getCard() != null && ((BasicCard) cardStack.getCard()).getCardType() != CardType.MAYOR_ESTABLISHMENT, event2 -> {
                            event1.selected.setBorder(null);
                            event1.selected.repaint();
                            event.card.setBorder(null);
                            event.card.repaint();
                            event2.owner.freeCard(event1.owner.removeCard((PlayingCard) ((CardStack) event1.selected).getCard()));
                            event1.owner.freeCard(event2.owner.removeCard((PlayingCard) ((CardStack) event2.selected).getCard()));
                            Game.game.setWaitForEvent(false);
                        }));
                    }));
                }
            };
            case 10 -> event -> {
                if (event.roll == 7 && event.owner == event.activePlayer) {
                    for (Card card : event.owner.getCardList()) {
                        if (((PlayingCard) card).getCardClass() == CardClass.SPACE_FARM) {
                            event.owner.addCoins(3);
                        }
                    }
                }
            };
            case 11 -> event -> {
                if (event.roll == 8 && event.owner == event.activePlayer) {
                    for (Card card : event.owner.getCardList()) {
                        if (((PlayingCard) card).getCardClass() == CardClass.SPACE_HARBOR) {
                            event.owner.addCoins(3);
                        }
                    }
                }
            };
            case 12 -> event -> {
                if (event.roll == 9) {
                    event.owner.addCoins(5);
                }
            };
            case 13 -> event -> {
                if (event.roll == 9 || event.roll == 10) {
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(event.activePlayer.removeCoins(3
                    ));
                    else event.owner.addCoins(event.activePlayer.removeCoins(2));
                }
            };
            case 14 -> event -> {
                if (event.roll == 10) {
                    event.owner.addCoins(3);
                }
            };
            case 15 -> event -> {
                if (event.roll == 11 || event.roll == 12) {
                    for (Card card : event.owner.getCardList()) {
                        if (((PlayingCard) card).getCardClass() == CardClass.ASTEROID) {
                            if (event.owner.hasLandmark(17)) event.owner.addCoins(3);
                            else event.owner.addCoins(2);
                        }
                    }
                }
            };
            default -> event -> {
                System.out.println("CARD " + id + " EVENT");

            };
//                throw new IllegalArgumentException("This id is not a valid Card ID: " + id);
        };
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
