package de.henrik.implementation.boards;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.components.Button;
import de.henrik.engine.components.Label;
import de.henrik.engine.game.Game;
import de.henrik.engine.game.GameBoard;

import java.awt.*;

public class MainMenu extends GameBoard {
    int playerCount = 2;


    public MainMenu(GameImage backgroundImage) {
        super(backgroundImage);

        Label title = new Label("Machi Koro", 50, 50, 1000, 80);

        Label playerCountLabel = new Label("Player: " + playerCount, 1000, 350, 500, 50);

        Button startGame = new Button("StartGame", 100, 200, 500, 50);
        startGame.addActionListener(e -> {
            System.out.println("Start Game");
            Game.game.switchGameBoard("game");
        });

        Button addPlayer = new Button("Add Player", 100, 300, 500, 50);
        addPlayer.addActionListener(e -> {
            System.out.println("Add Player");
            if (increasePlayerCount()) {
                playerCountLabel.setDescription("Player: " + playerCount);
            }
        });

        Button removePlayer = new Button("Remove Player", 100, 400, 500, 50);
        removePlayer.addActionListener(e -> {
            System.out.println("Remove Player");
            if (decreasePlayerCount()) {
                playerCountLabel.setDescription("Player: " + playerCount);
            }
        });

        add(title);
        add(playerCountLabel);
        add(startGame);
        add(addPlayer);
        add(removePlayer);
    }

    private boolean decreasePlayerCount() {
        if (playerCount > 2) {
            playerCount--;
            return true;
        } else
            return false;
    }

    private boolean increasePlayerCount() {
        if (playerCount < 4) {
            playerCount++;
            return true;
        } else
            return false;
    }


}
