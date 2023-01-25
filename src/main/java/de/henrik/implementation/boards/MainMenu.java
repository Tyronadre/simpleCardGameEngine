package de.henrik.implementation.boards;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.components.Button;
import de.henrik.engine.components.Label;
import de.henrik.engine.base.Board;
import de.henrik.engine.base.Game;
import de.henrik.engine.components.Pane;
import de.henrik.engine.components.TextField;
import de.henrik.implementation.game.Options;
import org.w3c.dom.Text;

public class MainMenu extends Board {
    public static final int BUTTON_WIDTH = 750;
    int playerCount = 2;


    public MainMenu(GameImage backgroundImage) {
        super(backgroundImage);

        Label title = new Label("Machi Koro", 50, 50, 1000, 80);

        Label playerCountLabel = new Label("Player: " + playerCount, 1000, 350, BUTTON_WIDTH, 50);

        Button startGame = new Button("StartGame", 100, 200, BUTTON_WIDTH, 50);
        startGame.addActionListener(e -> {
            System.out.println("Start Game");
            Game.game.switchGameBoard("game");
        });

        Button addPlayer = new Button("Add Player", 100, 300, BUTTON_WIDTH, 50);
        addPlayer.addActionListener(e -> {
            System.out.println("Add Player");
            if (increasePlayerCount()) {
                playerCountLabel.setDescription("Player: " + playerCount);
            }
        });

        Button removePlayer = new Button("Remove Player", 100, 400, BUTTON_WIDTH, 50);
        removePlayer.addActionListener(e -> {
            System.out.println("Remove Player");
            if (decreasePlayerCount()) {
                playerCountLabel.setDescription("Player: " + playerCount);
            }
        });


        Pane enabledE1 = new Pane(new GameImage("other/disabled"),50, 500,50,50);
        Button enableE1 = new Button("Toggle Expansion-Pack 1", 100, 500, BUTTON_WIDTH, 50);
        enableE1.addActionListener(e -> {
            System.out.println("toggle e1");
            if (Options.expansion1Selected) {
                enabledE1.setBackground(new GameImage("other/disabled"));
                Options.expansion1Selected = false;
            } else {
                enabledE1.setBackground(new GameImage("other/enabled"));
                Options.expansion1Selected = true;
            }
        });

        Pane enabledE2 = new Pane(new GameImage("other/disabled"),50,600,50,50);
        Button enableE2 = new Button("Toggle Expansion-Pack 2", 100, 600, BUTTON_WIDTH, 50);
        enableE2.addActionListener(e -> {
            System.out.println("toggle e2");
            if (Options.expansion1Selected) {
                enabledE2.setBackground(new GameImage("other/disabled"));
                Options.expansion1Selected = false;
            } else {
                enabledE2.setBackground(new GameImage("other/enabled"));
                Options.expansion1Selected = true;
            }
        });

        TextField textField = new TextField("Test", 100, 700, BUTTON_WIDTH, 50);

        add(textField);
        add(title);
        add(playerCountLabel);
        add(startGame);
        add(addPlayer);
        add(removePlayer);
        add(enabledE1);
        add(enableE1);
        add(enabledE2);
        add(enableE2);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        Options.setPlayerCount(playerCount);
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
