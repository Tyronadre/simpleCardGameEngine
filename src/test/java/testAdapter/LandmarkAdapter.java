package testAdapter;

import de.henrik.engine.base.GameImage;
import de.henrik.implementation.card.landmark.Landmark;
import de.henrik.implementation.card.playingcard.CardClass;
import de.henrik.implementation.card.playingcard.CardType;
import de.henrik.implementation.card.playingcard.PlayingCardBuilder;
import de.henrik.implementation.game.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LandmarkAdapter {

    static HashMap<Integer, Landmark> landmarkMap;

    public static void init() {
        Options.setWidth(1920);
        Options.setHeight(1080);
        landmarkMap = new HashMap<>(8);
        loadLandmarks("/landmarksE0.csv");
        loadLandmarks("/landmarksE1.csv");
        loadLandmarks("/landmarksE2.csv");
    }

    static void loadLandmarks(String csv) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(PlayingCardBuilder.class.getResourceAsStream(csv))))) {
                String l;
                while ((l = reader.readLine()) != null) {
                    try {
                        var line = l.split(",");
                        if (line.length != 2) {
                            System.err.println("Could not parse line, skipping: " + Arrays.toString(line));
                            continue;
                        }
                        landmarkMap.put(Integer.parseInt(line[0]),
                                new Landmark(
                                        Integer.parseInt(line[0]),
                                        Integer.parseInt(line[1]),
                                        CardClass.PLANET,
                                        CardType.LANDMARK,
                                        new GameImage("/cards/c" + line[0] + "_f.png"),
                                        new GameImage("/cards/c" + line[0] + "_b.png")
                                ));
                    } catch (RuntimeException e) {
                        System.err.println("Failed to load card: " + l);
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

        }
    }

    public static Landmark getLandmark(int id) {
        return landmarkMap.get(id);
    }
}
