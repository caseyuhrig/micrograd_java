package caseyuhrig.micrograd;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.IntStream;


public class Utils {

    public static Logger initLogger() {
        try {
            final var logConfigFileName = "log4j2.xml";
            final var file = new File(".").toPath().normalize().toFile();
            final var logConfigFile = file.toPath().resolve(logConfigFileName).normalize().toFile();
            System.out.println("FILE: " + logConfigFile.getAbsolutePath());
            if (logConfigFile.exists()) {
                try (final var in = new FileInputStream(logConfigFile)) {
                    final var context = Configurator.initialize(Utils.class.getClassLoader(), new ConfigurationSource(in));

                    final var logger = context.getRootLogger();
                    logger.setLevel(Level.INFO);
                    logger.info("LOGGING INIT: file: " + logConfigFile.getAbsolutePath());
                    return logger;
                }
            } else {
                System.out
                        .println("Setting up default logger, no external configuration found: " + logConfigFile.getAbsolutePath());
                final var resourcePath = String.format("/caseyuhrig/%s", logConfigFileName);
                try (final var logInputStream = Utils.class.getResourceAsStream(resourcePath)) {
                    if (logInputStream == null) {
                        final var message = String.format("LOGGING INIT: Unable to find resource: %s", resourcePath);
                        System.out.println(message);
                        throw new RuntimeException(message);
                    }
                    final var message = String.format("LOGGING INIT: resource: %s", resourcePath);
                    System.out.println(message);
                    final var context = Configurator.initialize(null, new ConfigurationSource(logInputStream));
                    final var logger = context.getRootLogger();
                    logger.setLevel(Level.INFO);
                    logger.info("LOGGING INIT: resource: " + resourcePath);
                    return logger;
                } catch (final Throwable throwable) {
                    System.err.println(throwable.getLocalizedMessage());
                    throwable.printStackTrace(System.err);
                    throw new RuntimeException(throwable.getLocalizedMessage(), throwable);
                }
            }
        } catch (final Throwable t) {
            System.err.println(t.getLocalizedMessage());
            t.printStackTrace(System.err);
            throw new RuntimeException(t.getLocalizedMessage(), t);
        }
    }


    public static Logger getLogger(final Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }


    public static ArrayList<ArrayList<Value>> of(final double[][] xs) {
        final var results = new ArrayList<ArrayList<Value>>();
        for (final double[] x : xs) {
            results.add(of(x));
        }
        return results;
    }

    public static ArrayList<Value> of(final double[] xs) {
        final var result = new ArrayList<Value>();
        for (final double x : xs) {
            result.add(new Value(x));
        }
        return result;
    }

    public static int[] cat(final int valueToPrepend, final int[] originalArray) {
        return IntStream.concat(IntStream.of(valueToPrepend), Arrays.stream(originalArray)).toArray();
    }

    public static double truncate(final double precision, double value) {
        final double factor = Math.pow(10, precision);
        value = value * factor;
        value = Math.floor(value);
        return value / factor;
    }

    public static String wrap(final String text, final int wrapLength) {
        final StringBuilder wrappedText = new StringBuilder();
        int lastIndex = 0;

        while (lastIndex < text.length()) {
            // Check if the rest of the string is shorter than wrapLength
            if (lastIndex + wrapLength > text.length()) {
                wrappedText.append(text.substring(lastIndex));
                break;
            }

            final int end = lastIndex + wrapLength;
            int spaceIndex = text.lastIndexOf(' ', end);

            // If no space is found, force a break at wrapLength
            if (spaceIndex <= lastIndex) {
                spaceIndex = end;
            }

            wrappedText.append(text, lastIndex, spaceIndex).append("\n");
            lastIndex = spaceIndex + 1; // Skip the space
        }

        return wrappedText.toString();
    }


    public static Timestamp now() {
        return new Timestamp(new Date().getTime());
    }


    public static Timestamp timestamp(final int year,
                                      final int month,
                                      final int dayOfMonth,
                                      final int hour,
                                      final int minute,
                                      final int second) {
        final var local = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
        return Timestamp.valueOf(local);
    }

    public static String threadDump(final boolean lockedMonitors, final boolean lockedSynchronizers) {
        final var threadDump = new StringBuilder(System.lineSeparator());
        final var threadMXBean = ManagementFactory.getThreadMXBean();
        for (final var threadInfo : threadMXBean.dumpAllThreads(lockedMonitors, lockedSynchronizers)) {
            threadDump.append(threadInfo.toString());
        }
        return threadDump.toString();
    }

    public static void centerText(final Graphics g, final String text, final Rectangle rect) {
        final var fm = g.getFontMetrics();
        final int textWidth = fm.stringWidth(text);
        final int textAscent = fm.getAscent();
        final int textDescent = fm.getDescent();
        // Centering the text horizontally
        final int textX = rect.x + (rect.width - textWidth) / 2;
        // Centering the text vertically
        final int textY = rect.y + (rect.height - (textAscent + textDescent)) / 2 + textAscent;
        // Draw the centered text
        g.drawString(text, textX, textY);
    }

    public static Graphics2D highQuality(final Graphics g) {
        final var graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        return graphics;
    }

    public static void setSizes(final JComponent component, final int width, final int height) {
        final var dimension = new Dimension(width, height);
        component.setSize(dimension);
        component.setPreferredSize(dimension);
        component.setMinimumSize(dimension);
        component.setMaximumSize(dimension);
    }

    public static void installPanningHandler(final JScrollPane scrollPane) {
        final Point[] lastPoint = {new Point()};

        // Cursor for grabbing
        final Cursor grabbingCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        final Cursor defaultCursor = Cursor.getDefaultCursor();

        scrollPane.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                lastPoint[0] = e.getPoint();
                scrollPane.getViewport().setCursor(grabbingCursor); // Change to grabbing cursor
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                scrollPane.getViewport().setCursor(defaultCursor); // Change back to default cursor
            }
        });

        scrollPane.getViewport().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                final JViewport viewport = scrollPane.getViewport();
                final Point currentPoint = e.getPoint();

                final Point viewPosition = viewport.getViewPosition();
                final int deltaX = lastPoint[0].x - currentPoint.x;
                final int deltaY = lastPoint[0].y - currentPoint.y;

                viewPosition.translate(deltaX, deltaY);

                // Bound checking
                final Rectangle viewRect = viewport.getViewRect();
                final Dimension viewSize = viewport.getViewSize();

                if (viewPosition.x < 0) viewPosition.x = 0;
                if (viewPosition.y < 0) viewPosition.y = 0;
                if (viewPosition.x > viewSize.width - viewRect.width) viewPosition.x = viewSize.width - viewRect.width;
                if (viewPosition.y > viewSize.height - viewRect.height)
                    viewPosition.y = viewSize.height - viewRect.height;

                viewport.setViewPosition(viewPosition);
                lastPoint[0] = currentPoint;
            }
        });
    }
}
