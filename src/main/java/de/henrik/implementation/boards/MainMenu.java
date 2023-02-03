package de.henrik.implementation.boards;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.components.Button;
import de.henrik.engine.components.Label;
import de.henrik.engine.game.Board;
import de.henrik.engine.game.Game;
import de.henrik.engine.components.Pane;
import de.henrik.engine.components.TextField;
import de.henrik.implementation.game.Options;

public class MainMenu extends Board {
    public static final int BUTTON_WIDTH = 750;
    int playerCount = 2;


    public MainMenu(GameImage backgroundImage) {
        super(backgroundImage);

        // Player Names

        Label p1 = new Label("Player 1:", 100 + BUTTON_WIDTH + 100, 200, 200, 50);
        Label p2 = new Label("Player 2:", 100 + BUTTON_WIDTH + 100, 300, 200, 50);
        Label p3 = new Label("Player 3:", 100 + BUTTON_WIDTH + 100, 400, 200, 50);
        Label p4 = new Label("Player 4:", 100 + BUTTON_WIDTH + 100, 500, 200, 50);
        TextField p1Tf = new TextField("p1", 100 + BUTTON_WIDTH + 100 + 200, 200, 300, 50);
        TextField p2Tf = new TextField("p2", 100 + BUTTON_WIDTH + 100 + 200, 300, 300, 50);
        TextField p3Tf = new TextField("p3", 100 + BUTTON_WIDTH + 100 + 200, 400, 300, 50);
        TextField p4Tf = new TextField("p4", 100 + BUTTON_WIDTH + 100 + 200, 500, 300, 50);
        p3.setVisible(false);
        p4.setVisible(false);
        p3Tf.setVisible(false);
        p4Tf.setVisible(false);

        Label[] playerLabel = new Label[] {p1,p2,p3,p4};
        TextField[] playerTextField = new TextField[]{p1Tf, p2Tf, p3Tf, p4Tf};
        add(p1);
        add(p2);
        add(p3);
        add(p4);
        add(p1Tf);
        add(p2Tf);
        add(p3Tf);
        add(p4Tf);


        Label title = new Label("Machi Koro", 50, 50, 1000, 80);

        Button startGame = new Button("StartGame", 100, 200, BUTTON_WIDTH, 50);
        startGame.addActionListener(e -> {
            System.out.println("Start Game");
            Options.player1Name = p1Tf.getText();
            Options.player2Name = p2Tf.getText();
            Options.player3Name = p3Tf.getText();
            Options.player4Name = p4Tf.getText();
            Game.game.switchGameBoard("game");
        });

        Button addPlayer = new Button("Add Player", 100, 300, BUTTON_WIDTH, 50);
        addPlayer.addActionListener(e -> {
            System.out.println("Add Player");
            if (increasePlayerCount()) {
                playerLabel[playerCount-1].setVisible(true);
                playerTextField[playerCount-1].setVisible(true);
            }
        });

        Button removePlayer = new Button("Remove Player", 100, 400, BUTTON_WIDTH, 50);
        removePlayer.addActionListener(e -> {
            System.out.println("Remove Player");
            if (decreasePlayerCount()) {
                playerLabel[playerCount].setVisible(false);
                playerTextField[playerCount].setVisible(false);
            }
        });


        Pane enabledE1 = new Pane(new GameImage("other/disabled"),50, 500,50,50);
        Button enableE1 = new Button("Toggle Expansion-Pack 1", 100, 500, BUTTON_WIDTH, 50);
        enableE1.addActionListener(e -> {
            System.out.println("toggle e1");
            if (Options.expansion1Selected) {
                enabledE1.setBackground(new GameImage("other/enabled"));
                Options.expansion1Selected = false;
            } else {
                enabledE1.setBackground(new GameImage("other/disabled"));
                Options.expansion1Selected = true;
            }
        });

        Pane enabledE2 = new Pane(new GameImage("other/disabled"),50,600,50,50);
        Button enableE2 = new Button("Toggle Expansion-Pack 2", 100, 600, BUTTON_WIDTH, 50);
        enableE2.addActionListener(e -> {
            System.out.println("toggle e2");
            if (Options.expansion1Selected) {
                enabledE2.setBackground(new GameImage("other/enabled"));
                Options.expansion1Selected = false;
            } else {
                enabledE2.setBackground(new GameImage("other/disabled"));
                Options.expansion1Selected = true;
            }
        });


        add(title);
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
