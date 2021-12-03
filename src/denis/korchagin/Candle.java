package denis.korchagin;

import denis.korchagin.utils.CalcExtremeValue;

import java.awt.*;
import java.util.List;

public class Candle {
    private double w, h;
    private RealPoint rp, max, min;
    private Color c;
    private boolean empty = false;

    public Candle(double x, double w, List<Double> data) {
        if (!data.isEmpty()) {
            double y;
            this.w = w;
            if (data.get(0) > data.get(data.size() - 1)) {
                y = data.get(0);
                c = Color.RED;
            } else {
                y = data.get(data.size() - 1);
                c = Color.BLUE;
            }

            rp = new RealPoint(x, y);

            h = Math.abs(data.get(0) - data.get(data.size() - 1));

            double maxY = CalcExtremeValue.calcMaxValue(data);
            double minY = CalcExtremeValue.calcMinValue(data);

            max = new RealPoint(x + w / 2, maxY);
            min = new RealPoint(x + w / 2, minY);
        } else {
            empty = true;
        }
    }

    public Candle(double w, double h, RealPoint rp, RealPoint max, RealPoint min, Color c) {
        this.w = w;
        this.h = h;
        this.rp = rp;
        this.max = max;
        this.min = min;
        this.c = c;
    }

    public boolean isEmpty() {
        return empty;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public RealPoint getRp() {
        return rp;
    }

    public void setRp(RealPoint rp) {
        this.rp = rp;
    }

    public RealPoint getMax() {
        return max;
    }

    public void setMax(RealPoint max) {
        this.max = max;
    }

    public RealPoint getMin() {
        return min;
    }

    public void setMin(RealPoint min) {
        this.min = min;
    }

    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }

}
