package denis.korchagin;

public class RealPoint {
    private double x;
    private double y;

    public RealPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public RealPoint minus(RealPoint p) {
        double x1 = p.getX() - x;
        double y1 = p.getY() - y;
        return new RealPoint(x1, y1);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
