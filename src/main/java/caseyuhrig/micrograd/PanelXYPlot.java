package caseyuhrig.micrograd;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PanelXYPlot extends JPanel {

    private final boolean darkMode = false;
    private final Font font = new Font("Cario", Font.PLAIN, 12);

    private final ArrayList<Point2D.Double> points = new ArrayList<>();

    public void add(final double x, final double y) {
        //System.out.println("plot(" + x + ", " + y + ")");
        points.add(new Point2D.Double(x, y));
        //this.font = new Font("Cario", Font.PLAIN, 12);
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final var g = (Graphics2D) graphics;
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //paintBackground(g);
        paintSolidBackground(g);
        g.setColor(Color.BLACK);
        if (!points.isEmpty()) paintPoints(g);
    }

    private void paintPoints(final Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final double margin = 40.0;
        final double inset = 20.0;
        final double width = getWidth();
        final double height = getHeight();
        final double xScale = width / 10.0;
        final double yScale = 20.0; //height / 10.0;
        final double xOrigin = 10.0;
        final double yOrigin = 10.0;

        //System.out.println("width: " + width);
        //System.out.println("height: " + height);

        final var marginRect = new Rectangle((int) margin, (int) margin, (int) (width - (margin * 2.0)), (int) (height - (margin * 2.0)));
        final var insetRect = new Rectangle((int) (margin + inset), (int) (margin + inset), (int) (marginRect.width - (inset * 2.0)), (int) (marginRect.height - (inset * 2.0)));

        final double maxLogPointX = maxLogPointX();
        final double maxPointX = maxPointX();
        final double maxPointY = maxPointY();

        final var startPoint = points.getFirst();
        //final double sx = startPoint.x * xScale + xOrigin;
        //final double sy = startPoint.y * yScale + yOrigin;
        final var sx = insetRect.x + (startPoint.x * insetRect.width / maxLogPointX);
        final var sy = insetRect.y + insetRect.height - (startPoint.y * insetRect.height / maxPointY);

        g.setStroke(new BasicStroke(1));
        g.setColor(Color.GRAY);
        g.draw(marginRect);
        g.draw(insetRect);



        // draw the x-axis numbers
        final double maxY = (int) (maxPointY);
        for (int los = 1; los < maxY + 1; los++) {
            final var y = insetRect.y + insetRect.height - (los * insetRect.height / maxPointY);
            g.drawLine(insetRect.x - 2, (int) y, insetRect.x + 5, (int) y);
            final var numRect = new Rectangle(insetRect.x - 12, (int) y - 6, 10, 12);
            Utils.centerText(g, String.format("%d", los), numRect);
        }

        // draw the y-axis numbers
        final double maxX = (int) (maxPointX);
        var step = (int) maxX / 8;
        if (step <= 0) step = (int) maxX;
        int count = 0;
        for (int epoch = 0; epoch < maxX + 1; epoch += step) {
            final var x = insetRect.x + (epoch * insetRect.width / maxPointX);
            g.drawLine((int) x, insetRect.y + insetRect.height + 2, (int) x, insetRect.y + insetRect.height - 5);
            final var numRect = new Rectangle((int) x - 6, insetRect.y + insetRect.height + 2, 12, 12);
            Utils.centerText(g, String.format("%d", epoch), numRect);
            if (count > 100) {
                System.out.println("count: " + count);
                break;
            }
            count++;
        }

        var footerRect = new Rectangle(0, (int) (height - margin), (int) width, (int) margin);
        g.setColor(Color.BLACK);
        g.setFont(font);
        Utils.centerText(g, "Epoch", footerRect);

        // x-axis label
        var sideBarRect = new Rectangle(0, 0, (int) margin, (int) height);
        g.setColor(Color.BLACK);
        g.setFont(font);
        Utils.centerText(g, "Loss", sideBarRect);


        final Path2D.Double path = new Path2D.Double();
        path.moveTo(sx, sy);

        for (final var point : points) {
            if (point.x < 0) {
                //System.out.println("LESS THAN ZERO!!!");
            }
            //final var x = point.x + xOrigin;
            //final var x = maxPointX * point.x / insetRect.width + insetRect.x;
            //final var x = insetRect.x + (point.x * insetRect.width / maxPointX);
            final var logX = insetRect.x + (Math.log(point.x) * insetRect.width / maxLogPointX);
            //final var y = insetRect.y + insetRect.height - (point.y * yScale);
            final var y = insetRect.y + insetRect.height - (point.y * insetRect.height / maxPointY);
            //double logX = Math.log(x);
            path.lineTo(logX, y);
        }
        //path.closePath();

        g.setStroke(new BasicStroke(1));
        g.draw(path);
    }

    private double maxPointX() {
        return points.stream()
                .mapToDouble(point -> point.x)
                .max()
                .orElse(0.0);
    }
    private double maxLogPointX() {
        return points.stream()
                .mapToDouble(point -> Math.log(point.x))
                .max()
                .orElse(0.0);
    }

    private double maxPointY() {
        return points.stream()
                .mapToDouble(point -> point.y)
                .max()
                .orElse(0.0);
    }

    private double maxX() {
        double max = Double.MIN_VALUE;
        for (final var point : points) {
            if (point.x > max) {
                max = point.x;
            }
        }
        return max;
    }

    private Color getLineColor() {
        return darkMode ? new Color(255, 255, 255, 64) : new Color(0, 0, 0, 64);
    }

    private Color getBackgroundColor() {
        return darkMode ? Color.BLACK : Color.WHITE;
    }

    // fill the background with a white rectangle
    private void paintSolidBackground(final Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintBackground(final Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        // Draw a grid with dashed lines like graph paper
        final var width = getWidth();
        final var height = getHeight();
        final var cellWidth = 100;
        final var cellHeight = 100;
        final var rows = height / cellHeight + 1;
        final var columns = width / cellWidth + 1;
        for (var i = 0; i < rows; i++) {
            final var y = i * cellHeight;
            for (var j = 0; j < columns; j++) {
                final var x = j * cellWidth;
                final var rectangle = new Rectangle(x, y, cellWidth, cellHeight);
                //g.setColor(Color.WHITE);
                g.setColor(getBackgroundColor());
                //g.setColor(new Color(0, 51, 153, 255));
                g.fill(rectangle);
                //g.setColor(Color.WHITE);
                //g.setColor(new Color(255, 255, 255, 32));
                g.setColor(getLineColor());
                //g.setColor(new Color(15, 51, 255, 255));
                //g.setColor(new Color(0, 0, 0, 64));
                // set the line style to dashed
                g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
                g.draw(rectangle);
            }
        }
    }


}
