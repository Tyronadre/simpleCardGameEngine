package de.henrik.implementation.GameEvent;

import de.henrik.engine.base.GameComponent;
import de.henrik.engine.card.Card;
import de.henrik.engine.events.GameEvent;
import de.henrik.engine.game.Player;
import de.henrik.implementation.boards.GameBoard;
import de.henrik.implementation.card.playingcard.PlayingCard;
import de.henrik.implementation.player.PlayerImpl;

public class CardEvent extends GameEvent {
    public Card card;
    public PlayerImpl owner;
    public PlayerImpl activePlayer;
    public int roll;
    public GameBoard gameBoard;


    public CardEvent(PlayerImpl owner, PlayerImpl activePlayer, int roll, GameBoard gameBoard, Card card) {
        super( "cardEvent", null);
        this.owner = owner;
        this.roll = roll;
        this.gameBoard = gameBoard;
        this.activePlayer = activePlayer;
        this.card = card;
    }
}
