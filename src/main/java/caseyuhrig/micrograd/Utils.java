package caseyuhrig.micrograd;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Utils {

    public static ArrayList<ArrayList<Value>> of(final double[][] xs) {
        final var results = new ArrayList<ArrayList<Value>>();
        for (var i = 0; i < xs.length; i++) {
            results.add(of(xs[i]));
        }
        return results;
    }

    public static ArrayList<Value> of(final double[] xs) {
        final var result = new ArrayList<Value>();
        for (var i = 0; i < xs.length; i++) {
            result.add(new Value(xs[i]));
        }
        return result;
    }

    public static int[] cat(final int valueToPrepend, final int[] originalArray) {
        return IntStream.concat(IntStream.of(valueToPrepend), Arrays.stream(originalArray)).toArray();
    }

    private static double truncate(final double precision, double value) {
        final double factor = Math.pow(10, precision);
        value = value * factor;
        value = Math.floor(value);
        return value / factor;
    }

    public static String wrap(String text, int wrapLength) {
        StringBuilder wrappedText = new StringBuilder();
        int lastIndex = 0;

        while (lastIndex < text.length()) {
            // Check if the rest of the string is shorter than wrapLength
            if (lastIndex + wrapLength > text.length()) {
                wrappedText.append(text.substring(lastIndex));
                break;
            }

            int end = lastIndex + wrapLength;
            int spaceIndex = text.lastIndexOf(' ', end);

            // If no space is found, force a break at wrapLength
            if (spaceIndex <= lastIndex) {
                spaceIndex = end;
            }

            wrappedText.append(text.substring(lastIndex, spaceIndex)).append("\n");
            lastIndex = spaceIndex + 1; // Skip the space
        }

        return wrappedText.toString();
    }

    public static String wrapColon(String text, int wrapLength) {
        StringBuilder wrappedText = new StringBuilder();
        int lastIndex = 0;

        while (lastIndex < text.length()) {
            if (lastIndex + wrapLength >= text.length()) {
                // Append the rest of the text if it's shorter than the wrap length
                wrappedText.append(text.substring(lastIndex));
                break;
            }

            int end = Math.min(lastIndex + wrapLength, text.length());
            int colonIndex = text.indexOf(':', lastIndex);

            if (colonIndex != -1 && colonIndex < end) {
                // Find the first space before the colon
                int spaceIndex = text.lastIndexOf(' ', colonIndex);
                if (spaceIndex >= lastIndex) {
                    end = spaceIndex; // Wrap at the space if it exists
                } else {
                    // No space found; find the next colon or wrap at the end of the wrap length
                    int nextColonIndex = text.indexOf(':', colonIndex + 1);
                    if (nextColonIndex != -1 && nextColonIndex < lastIndex + wrapLength) {
                        end = nextColonIndex;
                    } else {
                        end = lastIndex + wrapLength;
                    }
                }
            }

            // Append the substring and a newline
            wrappedText.append(text.substring(lastIndex, end)).append("\n");
            lastIndex = end + 1; // Move to the character after the space or colon
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
