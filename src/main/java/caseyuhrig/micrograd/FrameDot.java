package caseyuhrig.micrograd;

import javax.swing.*;
import java.awt.*;

public class FrameDot extends JFrame {

    private final JScrollPane scrollPane;

    private final DotPanel dotPanel;

    public FrameDot() {
        super("Dot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);
        dotPanel = new DotPanel();

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(dotPanel);
        Utils.installPanningHandler(scrollPane);

        setContentPane(scrollPane);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(final java.awt.event.ComponentEvent evt) {
                try {
                    if (dotPanel != null) {
                        dotPanel.revalidate();
                        dotPanel.repaint();
                    }
                } catch (final Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setValue(final Value value) {
        dotPanel.setValue(value);
    }

    public void center() {
        final var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final var width = (int) (screenSize.width * 0.6);
        final var height = (int) (screenSize.height * 0.6);
        setSize(width, height);
        setLocationRelativeTo(null);
    }


}
