package TestUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
