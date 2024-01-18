package caseyuhrig.micrograd;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import javax.swing.*;
import java.awt.image.BufferedImage;

import static guru.nidi.graphviz.model.Factory.node;

public class DotPanel extends JPanel {

    private Graph graph = null;
    private BufferedImage image = null;


    public DotPanel() {
        super();
        setDoubleBuffered(true);
        Utils.setSizes(this, 2000, 2000);
    }

    public void setValue(final Value value) {
        if (graph == null) this.graph = buildGraph(value);
        if (graph != null && image == null) {
            //SwingUtilities.invokeLater(() -> {
            try {
                image = Graphviz.fromGraph(graph).height(2000).render(Format.PNG).toImage();
                System.out.println("Image: " + image.getWidth() + "x" + image.getHeight());
                Utils.setSizes(this, image.getWidth(), image.getHeight());
                revalidate();
                repaint();
            } catch (final Throwable e) {
                e.printStackTrace();
            }
            //});

        }
    }

    public void paintComponent(final java.awt.Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            final var graphics = Utils.highQuality(g);
            graphics.drawImage(image, 0, 0, this);
        }
    }

    public static Node buildNode(Graph g, final Value value) {
        //System.out.println("buildNode(" + value + ")");
        final Node node = node(value.label);

        for (final var child : value.children) {
            g = g.with(node.link(buildNode(g, child)));
            //buildNode(graph, child);
            //System.out.println("buildChild(" + child.label + ")");
        }
        //graph = graph.with(node);
        return node;
    }

    public static Graph buildGraph(final Value value) {
        Graph g = Factory.graph("example2").directed();
        for (final Value v : value.order()) {
            //final var n = buildNode(g, value);

            if (!v.children.isEmpty()) {
                //final MutableNode root = Factory.mutNode(value.id.toString()).add(Label.of(value.label));
                //g.add(root);
                for (final var c : v.children) {
                    g = g.with(node(c.id.toString()).with(Label.of(c.label)).link(v.id.toString()).with(Label.of(v.label)));
                    //final MutableNode n = Factory.mutNode(c.id.toString()).add(Label.of(c.label));
                    //n.addLink(root);
                    //g.add(n);
                    //g.add(Factory.mutEdge(root, n));
                    //g.addLink(n, root);

                }
            } else {
                g = g.with(node(v.id.toString()).with(Label.of(v.label)));
                //final MutableNode root = Factory.mutNode(value.id.toString()).add(Label.of(value.label));
                //g.add(root);
            }
            //g = g.with(node("main").link(node("a")));
        }
        return g;
    }
}
