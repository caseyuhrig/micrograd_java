package caseyuhrig.micrograd;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Utils {

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

}
