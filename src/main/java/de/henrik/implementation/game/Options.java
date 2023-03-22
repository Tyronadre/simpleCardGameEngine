package de.henrik.implementation.game;

import de.henrik.engine.game.Game;


/**
 * Saves Settings and other static stuff
 */
public class Options {
    public static boolean expansion1Selected;
    public static boolean expansion2Selected;
    public static String player1Name;
    public static String player2Name;
    public static String player3Name;
    public static String player4Name;
    public static int drawStacks;
    static int playerCount;
    private static int width;
    private static int height;
    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public static void setPlayerCount(int playerCount) {
        Options.playerCount = playerCount;
    }


    public static void init() {
        expansion1Selected = false;
        expansion2Selected = false;
        playerCount = 2;
        width = Game.game.getWidth();
        height = Game.game.getHeight();
    }
}
