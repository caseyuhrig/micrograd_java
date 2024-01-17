package caseyuhrig.micrograd;

import java.awt.*;
import java.util.ArrayList;

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

    public static void centerText(final Graphics g, final String text, final Rectangle rect) {
        /*
        final FontMetrics fm = g.getFontMetrics();
        final int textWidth = fm.stringWidth(text);
        final int textHeight = fm.getAscent();

        // Centering the text horizontally and vertically within the rectangle
        final int textX = rect.x + (rect.width - textWidth) / 2;
        final int textY = rect.y + (rect.height - fm.getAscent()) / 2 + fm.getAscent();

        // Drawing the rectangle and the centered text
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
        g.drawString(text, textX, textY);
*/
        final FontMetrics fm = g.getFontMetrics();
        final int textWidth = fm.stringWidth(text);
        final int textAscent = fm.getAscent();
        final int textDescent = fm.getDescent();

        // Centering the text horizontally
        final int textX = rect.x + (rect.width - textWidth) / 2;

        // Centering the text vertically
        final int textY = rect.y + (rect.height - (textAscent + textDescent)) / 2 + textAscent;

        // Drawing the rectangle and the centered text
        //g.drawRect(rect.x, rect.y, rect.width, rect.height);
        g.drawString(text, textX, textY);
    }

}
