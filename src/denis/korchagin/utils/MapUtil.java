package denis.korchagin.utils;

import java.io.*;
import java.util.*;

public class MapUtil {
    public static Map<Double, Double> createMap(int n) {
        Map<Double, Double> map = new TreeMap<>();
        for(Integer i = 0; i < n; i++) {
            map.put(i.doubleValue(), Math.random() * i);
        }

        return map;
    }

    public static Map<Double, Double> getMapFromFile(String nameFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(nameFile));
        String line = null;
        Scanner scanner = null;
        int index = 0;
        Map<Double, Double> data = new TreeMap<>();

        while ((line = reader.readLine()) != null) {
            scanner = new Scanner(line);
            scanner.useDelimiter(",");
            Double key = 0.0;
            Double value = 0.0;
            while (scanner.hasNext()) {
                String x = scanner.next();
                if (index == 0) {
                    key = Double.parseDouble(x);
                } else {
                    value = Double.parseDouble(x);
                }
                index++;
            }
            index = 0;
            data.put(key, value);
        }
        reader.close();
        return data;
    }
}
