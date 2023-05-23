package testAdapter;

import de.henrik.implementation.game.Options;

public class MainMenuAdapter {
    /**
     * Sets the amount of players
     *
     * @param amount the amount of players
     */
    public static void setPlayerAmount(int amount) {
        Options.setPlayerCount(amount);
    }

    /**
     * Sets the name of a player
     *
     * @param player the player
     * @param name   the name of the player
     */
    public static void setPlayerName(int player, String name) {
        switch (player) {
            case 0 -> Options.player1Name = name;
            case 1 -> Options.player2Name = name;
            case 2 -> Options.player3Name = name;
            case 3 -> Options.player4Name = name;
        }
    }

    /**
     * Returns the amount of players
     *
     * @return the amount of players
     */
    public static int getPlayerCount() {
        return Options.getPlayerCount();
    }

    /**
     * Returns the name of a player
     *
     * @param player the player
     * @return the name of a player
     */
    public static String getPlayerName(int player) {
        return switch (player) {
            case 0 -> Options.player1Name;
            case 1 -> Options.player2Name;
            case 2 -> Options.player3Name;
            case 3 -> Options.player4Name;
            default -> throw new IllegalStateException("Unexpected value: " + player);
        };
    }

    /**
     * Enables or disables the expansion 1
     * @param b true if the expansion 1 should be enabled
     */
    public static void setExpansionE1(boolean b) {
        Options.expansion1Selected = b;
    }

    /**
     * Enables or disables the expansion 2
     * @param b true if the expansion 2 should be enabled
     */
    public static void setExpansionE2(boolean b) {
        Options.expansion2Selected = b;
    }
}
