package denis.korchagin;

import java.awt.*;

public class ScreenConverter {
    private double cx, cy, rw, rh;
    private int sw, sh;

    public ScreenConverter(double cx, double cy, double rw, double rh, int sw, int sh) {
        this.cx = cx;
        this.cy = cy;
        this.rw = rw;
        this.rh = rh;
        this.sw = sw;
        this.sh = sh;
    }

    public ScreenPoint r2s(RealPoint p) {
        double x = (p.getX() - cx) / rw * sw;
        double y = (cy - p.getY()) / rh * sh;
        return new ScreenPoint((int)x, (int)y);
    }

    public RealPoint s2r(ScreenPoint p) {
        double x = p.getC() * rw / sw + cx;
        double y = cy - p.getR() * rh / sh;
        return new RealPoint(x, y);
    }

    public int r2sX(double x) {
        return (int)((x - cx) / rw * sw);
    }

    public int r2sY(double y) {
        return (int)((cy - y) / rh * sh);
    }

    public void moveCorner(RealPoint delta) {
        cx += delta.getX();
        cy += delta.getY();
    }

    public int r2sW(Double w) {
        return (int)(w / rw * sw);
    }

    public int r2sH(Double h) {
        return (int)(h / rh * sh);
    }

    public void changeScale(double scale, RealPoint p) {
        RealPoint startPoint = new RealPoint(cx, cy);
        RealPoint delta = p.minus(startPoint);
        cx = p.getX();
        cy = p.getY();
        rw *= scale;
        rh *= scale;
        cx -= Math.abs(delta.getX() * scale);
        cy += Math.abs(delta.getY() * scale);
    }

    public RealPoint minus(RealPoint p1, RealPoint p2) {
        return new RealPoint(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public Candle createNewCandle(RealPoint p1, RealPoint p2) {
        Candle candle;
        double w = Math.abs(p1.getX() - p2.getX());
        if (p2.getY() >= p1.getY()) {
            if (p2.getX() <= p1.getX()) {
                candle = new Candle(w, p2.getY() - p1.getY(), p2, new RealPoint(p2.getX() + w / 2, p2.getY()), new RealPoint(p2.getX() + w / 2, p1.getY()), Color.RED);
            } else {
                candle = new Candle(w, p2.getY() - p1.getY(), new RealPoint(p1.getX(), p2.getY()), new RealPoint(p1.getX() + w / 2, p2.getY()), new RealPoint(p1.getX() + w / 2, p1.getY()), Color.RED);
            }
        } else {
            if (p2.getX() <= p1.getX()) {
                candle = new Candle(w, p1.getY() - p2.getY(), new RealPoint(p2.getX(), p1.getY()), new RealPoint(p2.getX() + w / 2, p1.getY()), new RealPoint(p2.getX() + w / 2, p2.getY()), Color.BLUE);
            } else {
                candle = new Candle(w, p1.getY() - p2.getY(), p1, new RealPoint(p1.getX() + w / 2, p1.getY()), new RealPoint(p1.getX() + w / 2, p2.getY()), Color.BLUE);
            }
        }

        return candle;
    }

    public double getCx() {
        return cx;
    }

    public void setCx(double cx) {
        this.cx = cx;
    }

    public double getCy() {
        return cy;
    }

    public void setCy(double cy) {
        this.cy = cy;
    }

    public double getRw() {
        return rw;
    }

    public void setRw(double rw) {
        this.rw = rw;
    }

    public double getRh() {
        return rh;
    }

    public void setRh(double rh) {
        this.rh = rh;
    }

    public int getSw() {
        return sw;
    }

    public void setSw(int sw) {
        this.sw = sw;
    }

    public int getSh() {
        return sh;
    }

    public void setSh(int sh) {
        this.sh = sh;
    }

}
