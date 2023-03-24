package tests;

import de.henrik.engine.game.Player;
import de.henrik.implementation.game.Options;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testAdapter.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainMenuTests {

    @BeforeEach
    void reset() {
        Provider.init();
        CardAdapter.init();
        Provider.setMenuState();
        Options.setPlayerCount(2);
    }

    @Test
    void test_StartInMenu() {
        assertEquals(GameStateAdapter.GameState.MENU, GameStateAdapter.getGameState());
    }

    @Test
    void test_StartGame(){
        Provider.setGameState();
        assertEquals(GameStateAdapter.GameState.NEW_PLAYER, GameStateAdapter.getGameState());
    }

    @Test
    void test_SetPlayerAmount(){
        MainMenuAdapter.setPlayerAmount(2);
        assertEquals(2, MainMenuAdapter.getPlayerCount());
    }

    @Test
    void test_SetPlayerName(){
        MainMenuAdapter.setPlayerName(1, "Test");
        assertEquals("Test", MainMenuAdapter.getPlayerName(1));
    }

    @Test
    void test_SetPlayerAmountAndName(){
        MainMenuAdapter.setPlayerAmount(2);
        MainMenuAdapter.setPlayerName(1, "Test");
        assertEquals(2, MainMenuAdapter.getPlayerCount());
        assertEquals("Test", MainMenuAdapter.getPlayerName(1));
    }

    @Test
    void test_SetPlayerAmountAndNameAndStartGame(){
        MainMenuAdapter.setPlayerAmount(2);
        MainMenuAdapter.setPlayerName(1, "Test");
        assertEquals(2, MainMenuAdapter.getPlayerCount());
        assertEquals("Test", MainMenuAdapter.getPlayerName(1));
        Provider.setGameState();
        assertEquals(GameStateAdapter.GameState.NEW_PLAYER, GameStateAdapter.getGameState());
    }
}
