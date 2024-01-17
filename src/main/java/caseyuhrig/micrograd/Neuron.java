package caseyuhrig.micrograd;

import java.util.ArrayList;

public class Neuron {

    public static void main(final String[] args) {
        System.out.println("Hello, Neuron!");

        final ArrayList<Value> x = new ArrayList<>() {{
            add(new Value(2.0));
            add(new Value(3.0));
        }};
        final var n = new Neuron(2);
        n.call(x);
    }

    public final int size;
    public ArrayList<Value> weights = new ArrayList<>();
    public Value bias;

    private Neuron() {
        size = 0;
    }

    public Neuron(final int size) {
        super();
        this.size = size;
        for (var i = 0; i < size; i++) {
            // create a random value between -1 and 1
            weights.add(new Value(uniform(-1, 1), String.format("w[%d]", i)));
        }
        this.bias = new Value(uniform(-1, 1), "b");
    }

    public ArrayList<Value> parameters() {
        final var parameters = new ArrayList<Value>(weights);
        parameters.add(bias);
        return parameters;
    }

    public ArrayList<WeightAndDouble> zip(final ArrayList<Value> weights, final ArrayList<Value> x) {
        final var results = new ArrayList<WeightAndDouble>();
        for (var i = 0; i < weights.size(); i++) {
            //results.add(weights.get(i).mul(new Value(x[i], STR."i[\{i}]")));
            results.add(new WeightAndDouble(weights.get(i), x.get(i)));
        }
        return results;
    }

    public Value sum(final ArrayList<WeightAndDouble> values) {
        var result = new Value(0.0, "sum");
        for (int i = 0; i < values.size(); i++) {
            final var value = values.get(i);
            final var weight = value.weight;
            final var x = value.x;
            x.label = String.format("x[%d]", i);
            final var v = weight.mul(x);
            v.label = String.format("x[%d]*w[%d]", i, i);
            result = result.add(v);
        }
        result.label = "sum";
        return result;
    }

    public Value call(final ArrayList<Value> x) {
        final var values = zip(weights, x);
        final var activation = sum(values).add(bias);
        activation.label = "+bias";
        final var o = activation.tanh();
        o.label = "o";
        return o;
    }

    public double uniform(final double min, final double max) {
        return Math.random() * (max - min) + min;
    }

    public record WeightAndDouble(Value weight, Value x) {

        public String toString() {
            return "(" + weight + ", " + x + ")";
        }
    }
}
