package caseyuhrig.micrograd;

import javax.swing.*;
import java.awt.*;

public class FrameChart extends JFrame {

    private final PanelXYPlot plotPanel;

    public FrameChart() {
        super("Loss/Time");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);
        plotPanel = new PanelXYPlot();
        setContentPane(plotPanel);
    }

    public void add(final double x, final double y) {
        SwingUtilities.invokeLater(() -> {
            plotPanel.add(x, y);
            revalidate();
            repaint();
        });
    }

    public void center() {
        final var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final var width = (int) (screenSize.width * 0.8);
        final var height = (int) (screenSize.height * 0.4);
        setSize(width, height);
        setLocationRelativeTo(null);
    }
}
