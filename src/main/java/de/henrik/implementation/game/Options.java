package de.henrik.implementation.game;

import de.henrik.engine.base.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Saves Settings and other static stuff
 */
public class Options {
    public static boolean expansion1Selected;
    public static boolean expansion2Selected;
    static int soundLevel;
    static int playerCount;
    static int width;
    static int height;
    static AiLevel aiLevel;
    static List<Player> players;

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setWidth(int width) {
        Options.width = width;
    }

    public static void setHeight(int height) {
        Options.height = height;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public static void setPlayerCount(int playerCount) {
        Options.playerCount = playerCount;
    }

    enum AiLevel {EASY, MEDIUM, HARD}
    enum PlayerType {AI, PLAYER}

    private static final String[] AI_Names = new String[]{"Optix", "Present", "Memory", "Admin", "Holmes", "Synapse", "Aura", "Solace", "Animus", "Sprite", "Mage"};

    public static void init() {
        expansion1Selected = false;
        expansion2Selected = false;
        soundLevel = 100;
        playerCount = 0;
        aiLevel = AiLevel.EASY;
        players = new ArrayList<>(4);
        setWidth(Game.game.getWidth());
        setHeight(Game.game.getHeight());
    }


    public  int addPlayer(String name) {
        if (players.size() == 4) {
            throw new IllegalArgumentException("Only 4 players are allowed");
        }
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Player needs to have a name");
        }
        players.add(new Player(PlayerType.PLAYER, null, name, players.size()));
        return players.size() - 1;
    }

    public  int addAI(String name, AiLevel aiLevel) {
        if (players.size() == 4) {
            throw new IllegalArgumentException("Only 4 players are allowed");
        }
        if (aiLevel == null) {
            throw new IllegalArgumentException("AI needs to have a difficulty level");
        }
        if (name == null || name.equals("")) {
            name = AI_Names[new Random().nextInt(AI_Names.length)];
        }
        players.add(new Player(PlayerType.AI, aiLevel, name, players.size()));
        return players.size() - 1;
    }


    public Player getPlayer(int ID) {
        return players.get(ID);
    }

    public void setSoundLevel(int soundLevel) {
        if (soundLevel < 0 || soundLevel > 100)
            throw new IllegalArgumentException("soundLevel out of range [0,100]: " + soundLevel);
        this.soundLevel = soundLevel;
    }

    private record Player(PlayerType  playerType, AiLevel aiLevel, String name, int ID) {}
}
