package caseyuhrig.micrograd;

import java.util.ArrayList;

/**
 * <h2>Multilayer Perceptron in Java example.</h2>
 * <p>MLP stands for "Multilayer Perceptron". It is one of the most basic types of neural network architectures.</p>
 * <p>Some key points about MLPs:</p>
 * <ul>
 * <li>MLPs consist of an input layer, one or more hidden layers, and an output layer, with each layer fully connected to the next layer.</li>
 * <li>Each node in one layer connects with a certain weight to every node in the following layer.</li>
 * <li>Data flows through the network in a forward direction, from input to output.</li>
 * <li>MLPs utilize backpropagation during training to calculate the gradient of the loss function with respect to the weights in the network. The weights are updated based on the calculated gradient to minimize the loss.</li>
 * <li>They are called "perceptrons" because each node sums its weighted inputs and then applies a non-linear activation function, mimicking how biological neurons work.</li>
 * <li>MLPs are considered "universal approximators", meaning given the right hyperparameters, they can model complex functions and datasets with high accuracy.</li>
 * </ul>
 * <p>So in summary, the term Multilayer Perceptron refers to the basic feedforward artificial neural network architecture with multiple layers and non-linear activation functions.</p>
 */
public class MLP {

    public double train(final ArrayList<ArrayList<Value>> xs, final ArrayList<Value> ys, final int epochs) {

        final FrameChart chart = new FrameChart();
        chart.center();
        chart.setVisible(true);

        final FrameDot dot = new FrameDot();
        dot.center();
        dot.setVisible(true);

        double endLoss = 0.0;

        for (long epoch = 0; epoch < epochs; epoch++) {
            final var ypred = new ArrayList<Value>();
            for (final ArrayList<Value> x : xs) {
                final var yp = call(x);
                ypred.add(yp.getFirst());
            }

            var totalLoss = new Value(0.0);
            for (var i = 0; i < xs.size(); i++) {
                final var yp = ypred.get(i);
                final var yt = ys.get(i);
                final var loss = yp.sub(yt).pow(2);
                totalLoss = totalLoss.add(loss);
            }

            chart.add(epoch, totalLoss.data);

            // IMPORTANT: zero the gradients!
            zeroGradients();
            // (re)compute the gradients
            totalLoss.backward();
            endLoss = totalLoss.data;

            updateParameters(0.02);

            dot.setValue(totalLoss);
        }

        return endLoss;
    }

    public ArrayList<Layer> layers = new ArrayList<>();

    public MLP(final int nin, final int[] nouts) {
        final var sz = Utils.cat(nin, nouts);
        for (var i = 0; i < nouts.length; i++) {
            layers.add(new Layer(sz[i], sz[i + 1]));
        }
    }

    public ArrayList<Value> parameters() {
        final var parameters = new ArrayList<Value>();
        for (final var layer : layers) {
            parameters.addAll(layer.parameters());
        }
        return parameters;
    }

    public void zeroGradients() {
        for (final var p : parameters()) {
            p.grad = 0.0;
        }
    }

    public void updateParameters(final double learningRate) {
        for (final var p : parameters()) {
            p.data += -learningRate * p.grad;
        }
    }

    /**
     * Call executes the forward pass of the network.
     *
     * @param x The input data.
     * @return The output results from processing all the neurons.
     */
    public ArrayList<Value> call(final ArrayList<Value> x) {
        var results = new ArrayList<>(x);
        for (final Layer layer : layers) {
            // pass the output of one layer to the next layers input
            results = layer.call(results);
        }
        return results;
    }
}
