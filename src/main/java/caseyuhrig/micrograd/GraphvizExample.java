package caseyuhrig.micrograd;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;

import static guru.nidi.graphviz.attribute.Label.Justification.LEFT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

public class GraphvizExample {

    public static void main(final String[] args) throws Exception {
        demo1();
        demo2();
    }

    public static void demo1() throws Exception {
        System.out.println("Hello, Neuron!");

        final Graph g = graph("example1").directed()
                .graphAttr().with(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .with(
                        node("a").with(Color.RED).link(node("b")),
                        node("b").link(
                                Factory.to(node("c")).with(Attributes.attr("weight", 5), Style.DASHED)
                        )
                );
        Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new File("C://tmp/ex1.png"));
    }

    public static void demo2() throws Exception {
        final Node
                main = node("main").with(Label.html("<b>main</b><br/>start"), Color.rgb("1020d0").font());
        final Node init = node(Label.markdown("**_init_**"));
        final Node execute = node("execute");
        final Node compare = node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0));
        final Node mkString = node("mkString").with(Label.lines(LEFT, "make", "a", "multi-line"));
        final Node printf = node("printf");

        final Graph g = graph("example2").directed().with(
                main.link(
                        to(node("parse").link(execute)).with(LinkAttr.weight(8)),
                        to(init).with(Style.DOTTED),
                        node("cleanup"),
                        to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED)),
                execute.link(
                        graph().with(mkString, printf),
                        to(compare).with(Color.RED)),
                init.link(mkString));

        Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("C://tmp/ex2.png"));
    }
}
