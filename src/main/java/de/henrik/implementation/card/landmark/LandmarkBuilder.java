package de.henrik.implementation.card.landmark;

import de.henrik.engine.base.GameImage;
import de.henrik.engine.card.Card;
import de.henrik.implementation.card.playingcard.CardClass;
import de.henrik.implementation.card.playingcard.CardType;
import de.henrik.implementation.card.playingcard.PlayingCardBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LandmarkBuilder {
    public static List<Landmark> buildLandmarkFromCSV(String csv) {
        List<Landmark> landmarks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(PlayingCardBuilder.class.getResourceAsStream(csv))))) {
            String l;
            while ((l = reader.readLine()) != null) {
                try {
                    var line = l.split(",");
                    if (line.length != 4) {
                        System.err.println("Could not parse line, skipping: " + Arrays.toString(line));
                        continue;
                    }
                    landmarks.add(new Landmark(
                            Integer.parseInt(line[0]),
                            Integer.parseInt(line[1]),
                            CardClass.LANDMARK,
                            CardType.LANDMARK,
                            new GameImage("/cards/" + line[2]),
                            new GameImage("/cards/" + line[3])
                    ));
                } catch (RuntimeException e) {
                    System.err.println("Failed to load card: " + l);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return landmarks;
    }
}
