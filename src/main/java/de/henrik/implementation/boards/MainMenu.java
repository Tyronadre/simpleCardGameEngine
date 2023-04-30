package de.henrik.implementation.boards;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.components.Button;
import de.henrik.engine.components.Label;
import de.henrik.engine.components.Pane;
import de.henrik.engine.game.Board;
import de.henrik.engine.game.Game;
import de.henrik.implementation.game.Options;

import java.awt.*;

public class MainMenu extends Board {
    public static final int BUTTON_WIDTH = 750;


    public MainMenu() {
        super(new GameImage("/background/mainMenu.jpg").getScaledInstance(Options.getWidth(), Options.getHeight()));

        Label title = new Label("Title", 50, 50, 1000, 80);
        Button startGame = new Button("StartGame", 100, 200, BUTTON_WIDTH, 50);
        Button quit = new Button("Quit", 100, 700, BUTTON_WIDTH, 50);

        quit.addActionListener(e -> {
            System.out.println("Quit");
            System.exit(0);
        });
        startGame.addActionListener(e -> {
            System.out.println("Start Game");
            Pane pane = new Pane(new GameImage(new Color(0, 0, 0, 0.5f)), 0, 0, Options.getWidth(), Options.getHeight());
            pane.add(new Label("Loading...", 200, Options.getHeight() / 2 - 30, Options.getWidth(), 60));
            add(pane);
            repaint();
            Game.game.switchGameBoard("game");
            remove(pane);
        });


        add(title);
        add(startGame);
        add(quit);
    }

    @Override
    public void activate() {
        super.activate();
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }
}
