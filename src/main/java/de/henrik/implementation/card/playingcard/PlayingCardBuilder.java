package de.henrik.implementation.card.playingcard;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.engine.card.CardStack;
import de.henrik.engine.game.Border;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import de.henrik.implementation.GameEvent.CardEvent;
import de.henrik.implementation.GameEvent.CardEventListener;
import de.henrik.implementation.GameEvent.ChoiceEvent;
import de.henrik.implementation.card.BasicCard;
import de.henrik.implementation.card.landmark.Landmark;
import de.henrik.implementation.player.PlayerImpl;
import de.henrik.implementation.player.PlayerPaneImpl;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.*;

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
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins(1);
                }
            };
            case 2 -> event -> {
                if (event.roll == 2) {
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins(1);
                }
            };
            case 3 -> event -> {
                if ((event.roll == 2 || event.roll == 3) && event.activePlayer == event.owner) {
                    if (checkDeactivated(event)) return;
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(2);
                    else event.owner.addCoins(1);
                }
            };
            case 4 -> event -> {
                if (event.roll == 3 && event.activePlayer != event.owner) {
                    if (checkDeactivated(event)) return;
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(event.activePlayer.removeCoins(2));
                    else event.owner.addCoins(event.activePlayer.removeCoins(1));
                }
            };
            case 5 -> event -> {
                if (event.roll == 4 && event.activePlayer == event.owner) {
                    if (checkDeactivated(event)) return;
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(4);
                    else event.owner.addCoins(3);
                }
            };
            case 6 -> event -> {
                if (event.roll == 5) {
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins(1);
                }
            };
            case 7 -> event -> {
                if (event.roll == 6 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
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
                    if (checkDeactivated(event)) return;
                    for (PlayerImpl player : event.gameBoard.getPlayers()) {
                        if (player != event.owner) {
                            event.owner.addCoins(player.removeCoins(2));
                        }
                    }
                }
            };
            case 9 -> event -> {
                if (event.roll == 8 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
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
                    if (checkDeactivated(event)) return;
                    for (Card card : event.owner.getCardList()) {
                        if (((PlayingCard) card).getCardClass() == CardClass.SPACE_FARM) {
                            event.owner.addCoins(3);
                        }
                    }
                }
            };
            case 11 -> event -> {
                if (event.roll == 8 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    for (Card card : event.owner.getCardList()) {
                        if (((PlayingCard) card).getCardClass() == CardClass.SPACE_HARBOR) {
                            event.owner.addCoins(3);
                        }
                    }
                }
            };
            case 12 -> event -> {
                if (event.roll == 9) {
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins(5);
                }
            };
            case 13 -> event -> {
                if (event.roll == 9 || event.roll == 10) {
                    if (checkDeactivated(event)) return;
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(event.activePlayer.removeCoins(3));
                    else event.owner.addCoins(event.activePlayer.removeCoins(2));
                }
            };
            case 14 -> event -> {
                if (event.roll == 10) {
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins(3);
                }
            };
            case 15 -> event -> {
                if (event.roll == 11 || event.roll == 12) {
                    if (checkDeactivated(event)) return;
                    for (Card card : event.owner.getCardList()) {
                        if (((PlayingCard) card).getCardClass() == CardClass.ASTEROID) {
                            if (event.owner.hasLandmark(17)) event.owner.addCoins(3);
                            else event.owner.addCoins(2);
                        }
                    }
                }
            };
            case 20 -> event -> {
                if (event.roll == 4) {
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins(1);
                }
            };
            case 21 -> event -> {
                if (event.roll == 8 && event.owner.hasLandmark(31)) {
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins(3);
                }
            };
            case 22 -> event -> {
                if ((event.roll == 12 || event.roll == 13 || event.roll == 14) && event.owner.hasLandmark(31)) {
                    if (checkDeactivated(event)) return;
                    Random random = new Random();
                    int roll1 = random.nextInt(6) + 1;
                    int roll2 = random.nextInt(6) + 1;
                    event.owner.addCoins(roll1 + roll2);
                }
            };
            case 23 -> event -> {
                if (event.roll == 6 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    for (Card card : event.owner.getCardList()) {
                        if (card.getID() == 20) {
                            if (event.owner.hasLandmark(17)) event.owner.addCoins(2);
                            else event.owner.addCoins(1);
                        }
                    }
                }
            };
            case 24 -> event -> {
                if (event.roll == 9 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins(2 * event.owner.getCardList(CardType.SUPPLIER).size());
                }
            };
            case 25 -> event -> {
                if ((event.roll == 1) && event.owner != event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    if (event.owner.hasLandmark(31))
                        if (event.owner.hasLandmark(17)) event.owner.addCoins(event.activePlayer.removeCoins(4));
                        else event.owner.addCoins(event.activePlayer.removeCoins(3));
                }
            };
            case 26 -> event -> {
                if (event.roll == 7 && event.owner != event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(event.activePlayer.removeCoins(2));
                    else event.owner.addCoins(event.activePlayer.removeCoins(1));
                }
            };
            case 27 -> event -> {
                if (event.roll == 8 && event.owner != event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    if (event.owner.hasLandmark(17)) event.owner.addCoins(event.activePlayer.removeCoins(2));
                    else event.owner.addCoins(event.activePlayer.removeCoins(1));
                }
            };
            case 28 -> event -> {
                if (event.roll == 7 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    for (Card card : event.owner.getCardList()) {
                        if (((PlayingCard) card).getCardClass() == CardClass.SUPPLY_FACTORY || ((PlayingCard) card).getCardClass() == CardClass.SPACE_SHOP) {
                            for (Player player : event.gameBoard.getPlayers()) {
                                if (player != event.owner) {
                                    event.owner.addCoins(event.activePlayer.removeCoins(1));
                                }
                            }
                        }
                    }
                }
            };
            case 29 -> event -> {
                if ((event.roll == 8 || event.roll == 9) && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    for (PlayerImpl player : event.gameBoard.getPlayers()) {
                        if (player != event.owner && player.getCoins() >= 10) {
                            event.owner.addCoins(player.removeCoins(player.getCoins() / 2));
                        }
                    }
                }
            };
            case 33 -> event -> {
                if ((event.roll == 2 || event.roll == 3)) {
                    if (checkDeactivated(event)) return;
                    if (event.owner.amountActiveLandmarks() - (event.owner.hasLandmark(30) ? 1 : 0) < 2) {
                        event.owner.addCoins(1);
                    }
                }
            };
            case 34 -> event -> {
                if (event.roll == 7) {
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins(3);
                }
            };
            case 35 -> event -> {
                if (event.roll == 2 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    if (event.owner.amountActiveLandmarks() - (event.owner.hasLandmark(30) ? 1 : 0) < 2)
                        if (event.owner.hasLandmark(17)) event.owner.addCoins(3);
                        else event.owner.addCoins(2);
                }
            };
            case 36 -> event -> {
                if ((event.roll == 1 || event.roll == 9) && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    event.card.setBorder(new Border(Color.GREEN, false, 2, 3));
                    event.card.repaint();
                    Game.game.event(new ChoiceEvent(gameComponent -> gameComponent instanceof CardStack cardStack && !event.gameBoard.drawStacks.getCardStacks().contains(gameComponent) && event.owner.getCardStacks().contains(gameComponent) && cardStack.getCard() != null && ((BasicCard) cardStack.getCard()).getCardType() != CardType.MAYOR_ESTABLISHMENT, event1 -> {
                        event1.selected.setBorder(new Border(Color.BLUE, false, 2, 3));
                        event1.selected.repaint();
                        Game.game.forceEvent(new ChoiceEvent(gameComponent -> gameComponent instanceof PlayerPaneImpl && gameComponent != event.activePlayer.getPlayerPane(), event2 -> {
                            event1.selected.setBorder(null);
                            event1.selected.repaint();
                            event.card.setBorder(null);
                            event.card.repaint();
                            event2.owner.freeCard(event1.owner.removeCard((PlayingCard) ((CardStack) event1.selected).getCard()));
                            Game.game.setWaitForEvent(false);
                        }));
                    }));
                }
            };
            case 37 -> event -> {
                if (checkDeactivated(event)) return;
                if ((event.roll == 5 || event.roll == 6) && event.owner == event.activePlayer) {
                    event.owner.removeCoins(2);
                }
            };
            case 38 -> event -> {
                if (event.roll == 9 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    event.owner.addCoins((int) (6 * event.owner.getCardList().stream().filter(card -> card.getID() == 34).count()));
                    ((PlayingCard) event.card).setDeactived(true);
                    event.card.repaint();
                }
            };
            case 39 -> event -> {
                if (event.roll == 4 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    event.card.setBorder(new Border(Color.GREEN, false, 2, 3));
                    event.card.repaint();
                    if (event.owner.amountActiveLandmarks() - (event.owner.hasLandmark(30) ? 1 : 0) > 0) {
                        Game.game.event(new ChoiceEvent(gameComponent -> gameComponent instanceof Landmark landmark && event.owner.getLandmarkList().contains(landmark), event1 -> {
                            event.owner.addCoins(8);
                            event.card.setBorder(null);
                            event.card.repaint();
                            event.owner.removeLandmark((Landmark) event1.selected);
                            Game.game.setWaitForEvent(false);
                        }));
                    }
                }
            };
            case 40 -> event -> {
                if (event.roll == 11 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    int restaurantsBuild = 0;
                    for (PlayerImpl player : event.gameBoard.getPlayers()) {
                        restaurantsBuild += player.getCardList(CardType.SUPPLIER).size();
                    }
                    event.owner.addCoins(restaurantsBuild);
                }
            };
            case 41 -> event -> {
                if (event.roll == 5 && event.owner != event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    if (event.activePlayer.amountActiveLandmarks() - (event.activePlayer.hasLandmark(30) ? 1 : 0) >= 2) {
                        if (event.owner.hasLandmark(17)) event.owner.addCoins(event.activePlayer.removeCoins(6));
                        else event.owner.addCoins(event.activePlayer.removeCoins(5));
                    }
                }
            };
            case 42 -> event -> {
                if ((event.roll == 12 || event.roll == 13 || event.roll == 14) && event.owner != event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    if (event.activePlayer.amountActiveLandmarks() - (event.activePlayer.hasLandmark(30) ? 1 : 0) >= 3) {
                        event.owner.addCoins(event.activePlayer.removeCoins(event.activePlayer.getCoins()));
                    }
                }
            };
            case 43 -> event -> {
                if ((event.roll == 11 || event.roll == 12 || event.roll == 13) && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    int coins = 0;
                    for (PlayerImpl player : event.gameBoard.getPlayers()) {
                        coins += player.getCoins();
                    }
                    while (coins % event.gameBoard.getPlayers().size() != 0) {
                        coins++;
                    }
                    for (PlayerImpl player : event.gameBoard.getPlayers()) {
                        player.setCoins(coins / event.gameBoard.getPlayers().size());
                    }
                }
            };
            case 44 -> event -> {
                if (event.roll == 8 && event.owner == event.activePlayer) {
                    if (checkDeactivated(event)) return;
                    var possibleBuildings = new ArrayList<>();
                    for (PlayerImpl player : event.gameBoard.getPlayers()) {
                        for (Card card : player.getCardList()) {
                            if (card instanceof BasicCard basicCard && basicCard.getCardType() != CardType.MAYOR_ESTABLISHMENT) {
                                possibleBuildings.add(basicCard);
                            }
                        }
                    }
                    event.card.setBorder(new Border(Color.GREEN, false, 2, 3));
                    event.card.repaint();
                    new ChoiceEvent(possibleBuildings::contains, event1 -> {
                        for (PlayerImpl player : event.gameBoard.getPlayers()) {
                            for (Card card : player.getCardList()) {
                                if (card.getID() == ((Card) event1.selected).getID()) {
                                    ((BasicCard) card).setDeactived(true);
                                    card.repaint();
                                    event.owner.addCoins(1);
                                    event.card.setBorder(null);
                                    event.card.repaint();
                                    Game.game.setWaitForEvent(false);
                                }
                            }
                        }
                    });
                }
            };

            default -> throw new IllegalArgumentException("This id is not a valid Card ID: " + id);
        };
    }

    private static boolean checkDeactivated(CardEvent event) {
        if (((PlayingCard) event.card).isDeactived()) {
            ((PlayingCard) event.card).setDeactived(false);
            event.card.repaint();
            return true;
        }
        return false;
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
