package tests;

import de.henrik.engine.game.Game;
import de.henrik.engine.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testAdapter.LandmarkAdapter;
import testAdapter.PlayerAdapter;
import testAdapter.Provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LandmarkTest {
    static Player player0;
    static Player player1;
    static Player player2;
    static Player player3;
    static Game game;

    @BeforeAll
    static void init() {
        Provider.init();
        LandmarkAdapter.init();
        Provider.setGameState();
        game = Provider.game;
        player0 = PlayerAdapter.getPlayer(game, 0);
        player1 = PlayerAdapter.getPlayer(game, 1);
        player2 = PlayerAdapter.getPlayer(game, 2);
        player3 = PlayerAdapter.getPlayer(game, 3);
    }

    @Test
    void test_landmark_e0_variables() {
        for (int i = 16; i <=19; i++) {
            System.out.println("Testing (Landmark) Card " + i);
            assertEquals(i, LandmarkAdapter.getLandmark(i).getID());
            assertEquals("/cards/c" + i + "_f.png", LandmarkAdapter.getLandmark(i).getFrontOfCard().getPath());
            assertEquals("/cards/c" + i + "_b.png", LandmarkAdapter.getLandmark(i).getBackOfCard().getPath());
            assertEquals(switch (i) {
                case 16 -> 4;
                case 17 -> 10;
                case 18 -> 16;
                case 19 -> 22;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }, LandmarkAdapter.getLandmark(i).getCost());
        }
    }
}
