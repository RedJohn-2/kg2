package denis.korchagin.utils;

import java.util.List;
import java.util.Set;

public class CalcExtremeValue {
    public static Double calcMaxValue(List<Double> list) {
        Double max = list.get(0);
        for (Double v : list) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public static Double calcMinValue(List<Double> list) {
        Double min = list.get(0);
        for (Double v : list) {
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

}
