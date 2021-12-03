package denis.korchagin;

import denis.korchagin.utils.MapUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;


public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private ScreenConverter sc;
    private Map<Double, Double> data;
    private List<Candle> candles;
    private List<Candle> newCandles = new ArrayList<>();
    private Candle currentCandle = null;
    private Candle editingCandle = null;
    private Double maxKey;
    private Double minKey;
    private Double maxValue;
    private Double minValue;
    private CandleCharts candleCharts;

    public DrawPanel() {
        data = MapUtil.createMap(100);
        maxKey = Collections.max(data.keySet());
        minKey = Collections.min(data.keySet());
        maxValue = Collections.max(data.values());
        minValue = Collections.min(data.values());
        sc = new ScreenConverter(minKey - 0.05 * Math.abs(maxKey - minKey), maxValue, Math.abs(maxKey - minKey) * 1.05, Math.abs(maxValue - minValue) * 1.05, 800, 600);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    private void changeParameters() {
        maxKey = Collections.max(data.keySet());
        minKey = Collections.min(data.keySet());
        maxValue = Collections.max(data.values());
        minValue = Collections.min(data.values());
        sc.setCx(minKey - 0.05 * Math.abs(maxKey - minKey));
        sc.setCy(maxValue);
        sc.setRw(Math.abs(maxKey - minKey) * 1.05);
        sc.setRh(Math.abs(maxValue - minValue) * 1.05);
    }

    public void setData(Map<Double, Double> data) {
        this.data = data;
        changeParameters();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics gOrig) {
        sc.setSw(getWidth());
        sc.setSh(getHeight());
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0, getWidth(), getHeight());
        drawCandleChart(g, sc, 25);
        for (Candle c : newCandles) {
            drawCandle(g, sc, c, false);
        }

        if (currentCandle != null) {
            drawCandle(g, sc, currentCandle, false);
        }
        Area area = new Area(new Rectangle2D.Double(10, 10, 30, 30));
        area.add(new Area(new Ellipse2D.Double(15, 15, 30, 40)));
        g.setColor(Color.RED);
        //g.fill(area);
        g.draw(area);
        //graphics.drawOval(0, 0, 30, 30);
        gOrig.drawImage(bi, 0,0,null);
        g.dispose();
    }

    private void drawCandleChart(Graphics g, ScreenConverter sc, int n) {
        Color oldColor = g.getColor();
        g.setColor(Color.BLACK);
        candleCharts = new CandleCharts(sc.getRw() / 1.05, data, n);
        candles = candleCharts.getCandles();
        for (Candle candle : candles) {
            drawCandle(g, sc, candle, true);
        }

        ScreenPoint s1 = new ScreenPoint(sc.r2sX(minKey), (int)(sc.getSh() * 0.96));
        g.drawString(String.format("%.1f", minKey), s1.getC(), s1.getR());
        for (int i = 0; i <= n; i++) {
            ScreenPoint s2 = new ScreenPoint(0, sc.r2sY((sc.getCy() - sc.getRh() * i / n)));
            g.drawString(String.format("%.1f", ((sc.getCy() - sc.getRh() * i / n) )), s2.getC(), s2.getR());
        }
        g.setColor(oldColor);
    }

    private void drawCandle(Graphics g, ScreenConverter sc, Candle candle, boolean isChart) {
        if (!candle.isEmpty()) {
            Graphics2D  gr = (Graphics2D)g;
            Color oldColor = gr.getColor();
            Stroke oldStroke = gr.getStroke();
            gr.setStroke(new BasicStroke(3));
            ScreenPoint p1 = sc.r2s(candle.getRp());
            int w = sc.r2sW(candle.getW());
            int h = sc.r2sH(candle.getH());
            gr.setColor(candle.getC());
            gr.fillRect(p1.getC(), p1.getR(), w, h);
            ScreenPoint pMax = sc.r2s(candle.getMax());
            ScreenPoint pMin = sc.r2s(candle.getMin());
            gr.setColor(Color.BLACK);
            g.drawLine(pMax.getC(), pMax.getR(), pMax.getC(), p1.getR());
            g.drawLine(pMin.getC(), pMin.getR(), pMin.getC(), p1.getR() + h);
            if (isChart) {
                ScreenPoint s1 = new ScreenPoint(sc.r2sX(candle.getRp().getX() + candle.getW() * 0.85), (int) (sc.getSh() * 0.96));
                g.drawString(String.format("%.1f", candle.getRp().getX() + candle.getW()), s1.getC(), s1.getR());
            }
            gr.setColor(oldColor);
            gr.setStroke(oldStroke);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private Candle findCandle(List<Candle> candles, RealPoint p) {
        for (Candle c : candles) {
            if (p.getX() >= c.getRp().getX() &&
            p.getX() <= c.getRp().getX() + c.getW() &&
            p.getY() <= c.getRp().getY() &&
            p.getY() >= c.getRp().getY() - c.getH()) {
                return c;
            }
        }

        return null;
    }

    private ScreenPoint prevPoint = null;
    private ScreenPoint startPoint = null;
    private ScreenPoint movePoint = null;
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (editingCandle == null) {
                prevPoint = new ScreenPoint(e.getX(), e.getY());
            } else {
                Candle x = findCandle(newCandles, sc.s2r(new ScreenPoint(e.getX(), e.getY())));
                if (x != null) {
                    if (x.equals(editingCandle)) {
                        movePoint = new ScreenPoint(e.getX(), e.getY());
                    } else {
                        editingCandle = null;
                    }
                } else {
                    editingCandle = null;
                }
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            Candle x = findCandle(newCandles, sc.s2r(new ScreenPoint(e.getX(), e.getY())));
            if (editingCandle == null) {
                if (x != null) {
                    editingCandle = x;
                } else {
                    startPoint = new ScreenPoint(e.getX(), e.getY());
                    currentCandle = sc.createNewCandle(sc.s2r(startPoint), sc.s2r(startPoint));
                }
            } else {
                if (x != null) {
                    if (x.equals(editingCandle)) {
                        newCandles.remove(editingCandle);
                    }
                }
                editingCandle = null;
            }
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (editingCandle == null) {
                prevPoint = null;
            } else {
                ScreenPoint curP = new ScreenPoint(e.getX(), e.getY());
                RealPoint delta = sc.minus(sc.s2r(movePoint), sc.s2r(curP));
                RealPoint newRp = new RealPoint(editingCandle.getRp().getX() - delta.getX(), editingCandle.getRp().getY() - delta.getY());
                editingCandle.setRp(newRp);
                editingCandle.setMax(new RealPoint(editingCandle.getRp().getX() + editingCandle.getW() / 2, editingCandle.getRp().getY()));
                editingCandle.setMin(new RealPoint(editingCandle.getRp().getX() + editingCandle.getW() / 2, editingCandle.getRp().getY() - editingCandle.getH()));
                editingCandle = null;
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (startPoint != null) {
                RealPoint p = sc.s2r(new ScreenPoint(e.getX(), e.getY()));
                currentCandle = sc.createNewCandle(p, sc.s2r(startPoint));
                newCandles.add(currentCandle);
                currentCandle = null;
                startPoint = null;
            }
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private Candle newCandle;

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (editingCandle == null) {
                ScreenPoint currPoint = new ScreenPoint(e.getX(), e.getY());
                RealPoint p1 = sc.s2r(currPoint);
                RealPoint p2 = sc.s2r(prevPoint);
                RealPoint delta = p1.minus(p2);
                sc.moveCorner(delta);
                prevPoint = currPoint;
            } else {
                ScreenPoint curP = new ScreenPoint(e.getX(), e.getY());
                RealPoint delta = sc.minus(sc.s2r(movePoint), sc.s2r(curP));
                RealPoint newRp = new RealPoint(editingCandle.getRp().getX() - delta.getX(), editingCandle.getRp().getY() - delta.getY());
                editingCandle.setRp(newRp);
                editingCandle.setMax(new RealPoint(editingCandle.getRp().getX() + editingCandle.getW() / 2, editingCandle.getRp().getY()));
                editingCandle.setMin(new RealPoint(editingCandle.getRp().getX() + editingCandle.getW() / 2, editingCandle.getRp().getY() - editingCandle.getH()));
                movePoint = curP;
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (currentCandle != null) {
                RealPoint p = sc.s2r(new ScreenPoint(e.getX(), e.getY()));
                currentCandle = sc.createNewCandle(p, sc.s2r(startPoint));
            }
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private static final double SCALE_STEP = 0.02;
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicks = e.getWheelRotation();
        ScreenPoint p = new ScreenPoint(e.getX(), e.getY());
        double coef = 1 + SCALE_STEP * (clicks < 0 ? -1 : 1);
        double scale = 1;
        for (int i = Math.abs(clicks); i > 0; i--) {
            scale *= coef;
        }
        RealPoint rp = sc.s2r(p);
        sc.changeScale(scale, rp);
        repaint();
    }
}
