package caseyuhrig.micrograd;


import java.util.ArrayList;

/**
 * AutoGrad in Java example.
 * derivatives by backpropagation
 */
public class Value {

    public static void main(final String[] args) {
        System.out.println("Hello, ML!");

        // inputs x1,x2
        final var x1 = new Value(2.0, "x1");
        final var x2 = new Value(0.0, "x2");
        // weights w1,w2
        final var w1 = new Value(-3.0, "w1");
        final var w2 = new Value(1.0, "w2");
        // bias of the neuron
        final var b = new Value(6.8813735870195432, "b"); //6.8813735870195432, "b");
        // x1*w1 + x2*w2 + b
        final var x1w1 = x1.mul(w1);
        x1w1.label = "x1*w1";
        final var x2w2 = x2.mul(w2);
        x2w2.label = "x2*w2";
        final var x1w1x2w2 = x1w1.add(x2w2);
        x1w1x2w2.label = "x1*w1 + x2*w2";
        final var n = x1w1x2w2.add(b);
        n.label = "n";

        //final var o = n.tanh();

        // implement tanh by hand to see if everything works
        // e = (n * 2).exp()
        final var e = n.mul(new Value(2.0, "2.0")).exp();
        // e = (2 * n).exp() // sanity check the order of operations
        //final var e = new Value(2.0, "2.0").mul(n).exp();
        // o = (e - 1) / (e + 1)
        final var o = e.sub(new Value(1.0, "e-1")).div(e.add(new Value(1.0, "e+1")));

        o.label = "o";
        o.backward();
        o.dump();

        //demo1();
        //demo2();
        //demo3();
    }


    public static void demo1() {
        final var a = new Value(2.0, "a");
        final var b = new Value(-3.0, "b");
        final var c = new Value(10.0, "c");
        final var e = a.mul(b);
        e.label = "e";
        final var d = e.add(c);
        d.label = "d";
        final var f = new Value(-2.0, "f");
        final var L = d.mul(f);
        L.label = "L";
        L.backward();

        L.dump();
    }

    public static void demo2() {
        final var a = new Value(3.0, "a");
        final var b = a.add(a);
        b.label = "b";
        b.backward();
        b.dump();
    }

    public static void demo3() {
        final var a = new Value(2.0, "a");
        final var b = new Value(4.0, "b");
        final var o = a.div(b);
        //final var o = a.sub(b);
        o.label = "Output";
        o.dump();
    }

    public double data = 0.0;
    public double grad = 0.0;
    public final ArrayList<Value> children;
    private final ArrayList<Value> _prev = new ArrayList<>();

    public String label = "n/a";
    public final String op;

    private Runnable _backward = () -> {
    };

    /**
     * Only created the private constructor because of an IDE bug.
     * <a href="https://youtrack.jetbrains.com/issue/IDEA-339606">IDEA-339606</a>
     */
    private Value() {
        this(0.0, "n/a", new ArrayList<>(), "");
    }

    public Value(final double data, final String label, final ArrayList<Value> children, final String op) {
        this.data = data;
        this.children = children;
        this._prev.addAll(children);
        this.label = label;
        this.op = op;
    }

    public Value(final double data, final String label, final ArrayList<Value> children) {
        this(data, label, children, "");
    }

    public Value(final double data, final String label) {
        this(data, label, new ArrayList<>(), "");
    }

    public Value(final double data) {
        this(data, "", new ArrayList<>(), "");
    }

    public Value add(final Value other) {
        final var out = new Value(data + other.data, "+", new ArrayList<>() {{
            add(Value.this);
            add(other);
        }});
        out._backward = () -> {
            grad += out.grad;
            other.grad += out.grad;
        };
        return out;
    }

    public Value sub(final Value other) {
        return this.add(other.neg());
    }

    public Value neg() {
        return this.mul(new Value(-1.0, "(-1)", new ArrayList<>() {{
            //add(Value.this);
        }}
        ));
        /*
        final var out = new Value(-data, "-", new ArrayList<>() {{
            add(Value.this);
        }});
        out._backward = () -> {
            grad += -out.grad;
        };
        return out;
        */
    }

    public Value mul(final Value other) {
        final var out = new Value(data * other.data, "*", new ArrayList<>() {{
            add(Value.this);
            add(other);
        }});
        out._backward = () -> {
            grad += other.data * out.grad;
            other.grad += data * out.grad;
        };
        return out;
    }

    public Value div(final Value other) {
        return this.mul(other.pow(-1));
    }

    public Value tanh() {
        final var t = (Math.exp(2 * data) - 1) / (Math.exp(2 * data) + 1);
        final var out = new Value(t, "tanh", new ArrayList<>() {{
            add(Value.this);
        }});
        out._backward = () -> {
            grad += (1 - t * t) * out.grad;
        };
        return out;
    }

    public Value exp() {
        final var out = new Value(Math.exp(data), "exp", new ArrayList<>() {{
            add(Value.this);
        }});
        out._backward = () -> {
            grad += out.data * out.grad;
        };
        return out;
    }

    public Value pow(final double exponent) {
        final var out = new Value(Math.pow(data, exponent), "pow", new ArrayList<>() {{
            add(Value.this);
        }});
        out._backward = () -> {
            grad += exponent * Math.pow(data, exponent - 1) * out.grad;
        };
        return out;
    }

    public void backward() {
        grad = 1.0;
        order().reversed().forEach(v -> v._backward.run());
        //dump();
    }

    public ArrayList<Value> order() {
        //final var visited = new ArrayList<Value>();
        //final var order = new ArrayList<Value>();
        visited.clear();
        topo.clear();
        build_topo(this);
        return topo;
    }

    private final ArrayList<Value> visited = new ArrayList<>();
    private final ArrayList<Value> topo = new ArrayList<>();

    private void build_topo(final Value value) {
        if (!visited.contains(value)) {
            visited.add(value);
            for (final var child : value._prev) {
                build_topo(child);
            }
            topo.add(value);
        }
    }


    public String toString() {
        //return label + ": Value(data=" + data + ", grad=" + grad + ", children.size=" + children.size() + ", op=" + op + ")";
        //return String.format("%s: Value(data=%.4f, grad=%.4f, children.size=%d, op=%s)", label, data, grad, children.size(), op);
        return String.format("%s: Value(data=%.4f, grad=%.4f, %d)", label, data, grad, children.size());
    }

    public void dump() {
        System.out.println("--------------------");
        order().forEach(System.out::println);
    }
}
