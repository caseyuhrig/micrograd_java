package caseyuhrig.micrograd;

import java.util.ArrayList;



public class Layer {

    public ArrayList<Neuron> neurons;
    int nin;
    int nout;

    private Layer() {
        neurons = new ArrayList<>();
        nin = 0;
        nout = 0;
    }

    public Layer(final int nin, final int nout) {
        super();
        neurons = new ArrayList<>();
        this.nin = nin;
        this.nout = nout;
        for (var i = 0; i < nout; i++) {
            neurons.add(new Neuron(nin));
        }
    }

    public ArrayList<Value> parameters() {
        final var parameters = new ArrayList<Value>();
        for (final var neuron : neurons) {
            parameters.addAll(neuron.parameters());
        }
        return parameters;
    }

    public ArrayList<Value> call(final ArrayList<Value> x) {
        final var results = new ArrayList<Value>();
        for (var i = 0; i < neurons.size(); i++) {
            final var out = neurons.get(i).call(x);
            out.label = String.format("o[%d]", i);
            results.add(out);
        }
        return results;
    }
}
