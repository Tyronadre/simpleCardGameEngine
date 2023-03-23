package tests;

import de.henrik.engine.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testAdapter.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainMenuTests {
    static Player player0;
    static Player player1;
    static Player player2;
    static Player player3;
    @BeforeAll
    static void init() {
        Provider.init();
        CardAdapter.init();
        player0 = PlayerAdapter.getPlayer(Provider.game, 0);
        player1 = PlayerAdapter.getPlayer(Provider.game, 1);
        player2 = PlayerAdapter.getPlayer(Provider.game, 2);
        player3 = PlayerAdapter.getPlayer(Provider.game, 3);
    }

    @BeforeEach
    void reset() {
        Provider.gameEventThread.clearEvents();
        Provider.setMenuState();
    }

    @Test
    void test_StartInMenu() {
        assertEquals(GameStateAdapter.GameState.MENU, GameStateAdapter.getGameState(Provider.game));
    }

    @Test
    void test_StartGame(){
        Provider.setGameState();
        assertEquals(GameStateAdapter.GameState.NEW_PLAYER, GameStateAdapter.getGameState(Provider.game));
    }

    @Test
    void test_SetPlayerAmount(){
        MainMenuAdapter.setPlayerAmount(2);
        assertEquals(2, MainMenuAdapter.getPlayerCount());
        Provider.setGameState();
    }

    @Test
    void test_SetPlayerName(){
        MainMenuAdapter.setPlayerName(1, "Test");
        assertEquals("Test", MainMenuAdapter.getPlayerName(1));
        Provider.setGameState();
    }

    @Test
    void test_SetPlayerAmountAndName(){
        MainMenuAdapter.setPlayerAmount(2);
        MainMenuAdapter.setPlayerName(1, "Test");
        assertEquals(2, MainMenuAdapter.getPlayerCount());
        assertEquals("Test", MainMenuAdapter.getPlayerName(1));
        Provider.setGameState();
    }

    @Test
    void test_SetPlayerAmountAndNameAndStartGame(){
        MainMenuAdapter.setPlayerAmount(2);
        MainMenuAdapter.setPlayerName(1, "Test");
        assertEquals(2, MainMenuAdapter.getPlayerCount());
        assertEquals("Test", MainMenuAdapter.getPlayerName(1));
        Provider.setGameState();
        assertEquals(GameStateAdapter.GameState.NEW_PLAYER, GameStateAdapter.getGameState(Provider.game));
    }
}
