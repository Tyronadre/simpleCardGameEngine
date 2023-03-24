package TestUtil;

import de.henrik.engine.game.Player;
import de.henrik.implementation.player.PlayerImpl;
import testAdapter.PlayerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Util {

    @SuppressWarnings("unchecked")
    public static <T> T[] arrayFromArrays(T[]... array){
        List<T> list = new ArrayList<>();
        for (T[] objects : array) {
            list.addAll(List.of(objects));
        }
        return (T[]) list.toArray();
    }
}
