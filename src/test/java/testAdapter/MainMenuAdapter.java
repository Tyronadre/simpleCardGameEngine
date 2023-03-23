package testAdapter;

import de.henrik.implementation.game.Options;

public class MainMenuAdapter {
    public static void setPlayerAmount(int amount) {
        Options.setPlayerCount(amount);
    }

    public static void setPlayerName(int player, String name) {
        switch (player) {
            case 1 -> Options.player1Name = name;
            case 2 -> Options.player2Name = name;
            case 3 -> Options.player3Name = name;
            case 4 -> Options.player4Name = name;
        }
    }

    public static int getPlayerCount() {
        return Options.getPlayerCount();
    }

    public static String getPlayerName(int player) {
        return switch (player) {
            case 1 -> Options.player1Name;
            case 2 -> Options.player2Name;
            case 3 -> Options.player3Name;
            case 4 -> Options.player4Name;
            default -> throw new IllegalStateException("Unexpected value: " + player);
        };
    }
}
