package denis.korchagin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CandleCharts {
    private List<Candle> candles;

    public CandleCharts(double w, Map<Double, Double> data, int n) {
        Map<Double, Double> sortedData = new TreeMap<>();
        sortedData.putAll(data);
        double interval = w / n;
        candles = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            candles.add(i, new Candle((i + 0.1) * interval, 0.9 * interval, calcListValue(sortedData, interval, i)));
        }
    }

    private List<Double> calcListValue(Map<Double, Double> map, double interval, int n) {
        List<Double> list = new ArrayList<>();
        for (Double key : map.keySet()) {
            if ((key >= interval * n) && (key <= interval * (n + 1))) {
                list.add(map.get(key));
            }
        }
        return list;
    }

    public List<Candle> getCandles() {
        return candles;
    }

}
