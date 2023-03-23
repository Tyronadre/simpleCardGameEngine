package de.henrik.implementation.boards;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.components.Button;
import de.henrik.engine.components.Label;
import de.henrik.engine.game.Board;
import de.henrik.engine.game.Game;
import de.henrik.engine.components.Pane;
import de.henrik.engine.components.TextField;
import de.henrik.implementation.game.Options;

import java.awt.*;

public class MainMenu extends Board {
    public static final int BUTTON_WIDTH = 750;


    public MainMenu() {
        super(new GameImage("/background/mainMenu.jpg").getScaledInstance(Options.getWidth(), Options.getHeight()));

        // Player Names
        Label p1 = new Label("Player 1:", 100 + BUTTON_WIDTH + 100, 200, 200, 50);
        Label p2 = new Label("Player 2:", 100 + BUTTON_WIDTH + 100, 300, 200, 50);
        Label p3 = new Label("Player 3:", 100 + BUTTON_WIDTH + 100, 400, 200, 50);
        Label p4 = new Label("Player 4:", 100 + BUTTON_WIDTH + 100, 500, 300, 50);
        Label drawStacks = new Label("Draw Stacks:", 100 + BUTTON_WIDTH + 100, 600, 200, 50);
        TextField p1Tf = new TextField("p1", 100 + BUTTON_WIDTH + 100 + 200, 200, 300, 50);
        TextField p2Tf = new TextField("p2", 100 + BUTTON_WIDTH + 100 + 200, 300, 300, 50);
        TextField p3Tf = new TextField("p3", 100 + BUTTON_WIDTH + 100 + 200, 400, 300, 50);
        TextField p4Tf = new TextField("p4", 100 + BUTTON_WIDTH + 100 + 200, 500, 300, 50);
        TextField drawStacksTf = new TextField("10", 100 + BUTTON_WIDTH + 100 + 300, 600, 300, 50);
        p3.setVisible(false);
        p4.setVisible(false);
        p3Tf.setVisible(false);
        p4Tf.setVisible(false);

        Label[] playerLabel = new Label[]{p1, p2, p3, p4};
        TextField[] playerTextField = new TextField[]{p1Tf, p2Tf, p3Tf, p4Tf};
        add(p1);
        add(p2);
        add(p3);
        add(p4);
        add(p1Tf);
        add(p2Tf);
        add(p3Tf);
        add(p4Tf);
        add(drawStacks);
        add(drawStacksTf);


        Label title = new Label("Solar System Rush", 50, 50, 1000, 80);

        Button startGame = new Button("StartGame", 100, 200, BUTTON_WIDTH, 50);
        startGame.addActionListener(e -> {
            System.out.println("Start Game");
            Options.player1Name = p1Tf.getText();
            Options.player2Name = p2Tf.getText();
            Options.player3Name = p3Tf.getText();
            Options.player4Name = p4Tf.getText();
            Options.drawStacks = Integer.parseInt(drawStacksTf.getText());
            Pane pane = new Pane(new GameImage(new Color(0, 0, 0, 0.5f)), 0, 0, Options.getWidth(), Options.getHeight());
            pane.add(new Label("Loading...", 200, Options.getHeight() / 2 - 30, Options.getWidth(), 60));
            add(pane);
            repaint();
            Game.game.switchGameBoard("game");
            remove(pane);
        });

        Button addPlayer = new Button("Add Player", 100, 300, BUTTON_WIDTH, 50);
        Button removePlayer = new Button("Remove Player", 100, 400, BUTTON_WIDTH, 50);

        addPlayer.addActionListener(e -> {
            System.out.println("Add Player");
            if (increasePlayerCount()) {
                playerLabel[Options.playerCount - 1].setVisible(true);
                playerTextField[Options.playerCount - 1].setVisible(true);
                if (Options.playerCount == 4) {
                    addPlayer.disable();
                    addPlayer.repaint();
                }
                if (!removePlayer.isEnabled()) {
                    removePlayer.enable();
                    removePlayer.repaint();
                }
            }
        });
        removePlayer.addActionListener(e -> {
            System.out.println("Remove Player");
            if (decreasePlayerCount()) {
                playerLabel[Options.playerCount].setVisible(false);
                playerTextField[Options.playerCount].setVisible(false);
                if (Options.playerCount == 2) {
                    removePlayer.disable();
                    removePlayer.repaint();
                }
                if (!addPlayer.isEnabled()) {
                    addPlayer.enable();
                    removePlayer.repaint();
                }
            }
        });
        removePlayer.disable();

        Pane enabledE1 = new Pane(new GameImage("/other/disabled.png"), 50, 500, 50, 50);
        Button enableE1 = new Button("Toggle Expansion-Pack 1", 100, 500, BUTTON_WIDTH, 50);
        enableE1.addActionListener(e -> {
            System.out.println("toggle e1");
            if (Options.expansion1Selected) {
                enabledE1.setBackground(new GameImage("/other/disabled.png"));
                Options.expansion1Selected = false;
            } else {
                enabledE1.setBackground(new GameImage("/other/enabled.png"));
                Options.expansion1Selected = true;
            }
        });

        Pane enabledE2 = new Pane(new GameImage("/other/disabled.png"), 50, 600, 50, 50);
        Button enableE2 = new Button("Toggle Expansion-Pack 2", 100, 600, BUTTON_WIDTH, 50);
        enableE2.addActionListener(e -> {
            System.out.println("toggle e2");
            if (Options.expansion2Selected) {
                enabledE2.setBackground(new GameImage("/other/disabled.png"));
                Options.expansion2Selected = false;
            } else {
                enabledE2.setBackground(new GameImage("/other/enabled.png"));
                Options.expansion2Selected = true;
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
    }

    private boolean decreasePlayerCount() {
        if (Options.playerCount > 2) {
            Options.playerCount--;
            return true;
        } else return false;
    }

    private boolean increasePlayerCount() {
        if (Options.playerCount < 4) {
            Options.playerCount++;
            return true;
        } else return false;
    }


}
