package caseyuhrig.micrograd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

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

    public double train(ArrayList<ArrayList<Value>> xs, ArrayList<Value> ys, final int epochs) {

        final FrameChart chart = new FrameChart();
        chart.center();
        chart.setVisible(true);

        // Create new MLP with 2 inputs, 2 hidden layers with 4 neurons each, and 1 output
        //final var m = new MLP(3, new int[]{5, 4, 1});
        final var m = new MLP(3, new int[]{4, 4, 1});
        //final var m = new MLP(2, new int[]{40, 40, 30, 40, 1});

        double saveLoss = Double.MAX_VALUE;
        long saveEpoch = 0;
        double endLoss = 0.0;

        for (long epoch = 0; epoch < epochs; epoch++) {
            final var ypred = new ArrayList<Value>();
            for (var i = 0; i < xs.size(); i++) {
                final var yp = m.call(xs.get(i));
                ypred.add(yp.getFirst());
            }

            var totalLoss = new Value(0.0);
            for (var i = 0; i < xs.size(); i++) {
                final var yp = ypred.get(i);
                final var yt = ys.get(i);
                final var loss = yp.sub(yt).pow(2);
                totalLoss = totalLoss.add(loss);
                //System.out.printf("%d:\t%f\t%f\t%f\n", i, yp.data, loss.data, yt.data);
            }

            /*
            final var truncatedLoss = truncate(6, totalLoss.data);
            if (truncatedLoss != saveLoss) {
                if (truncatedLoss > 0.01) chart.add(epoch, truncatedLoss);
                saveLoss = truncatedLoss;
                saveEpoch = epoch;
            }

             */
            chart.add(epoch, totalLoss.data);

            // IMPORTANT: zero the gradients!
            m.zeroGradients();
            // (re)compute the gradients
            totalLoss.backward();
            endLoss = totalLoss.data;

            m.updateParameters(0.02);
        }
        return endLoss;
    }

    private static double truncate(final double precision, double value) {
        final double factor = Math.pow(10, precision);
        value = value * factor;
        value = Math.floor(value);
        return value / factor;
    }


    public ArrayList<Layer> layers = new ArrayList<>();


    private MLP() {
    }

    public MLP(final int nin, final int[] nouts) {
        super();
        final var sz = cat(nin, nouts);
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

    public ArrayList<Value> call(ArrayList<Value> x) {
        final var results = new ArrayList<Value>();
        for (var i = 0; i < layers.size(); i++) {
            //System.out.println("LAYER " + i);
            final var layer = layers.get(i);
            x = layer.call(x);
            //x = results.stream().mapToDouble(v -> v.data).toArray();
        }
        return x; //results;
    }

    public static int[] cat(final int valueToPrepend, final int[] originalArray) {
        return IntStream.concat(IntStream.of(valueToPrepend), Arrays.stream(originalArray)).toArray();
    }

    public static int[] catOld(final int valueToPrepend, final int[] originalArray) {
        // Create a new array with one more element than the original
        final int[] newArray = new int[originalArray.length + 1];
        // Set the first element of the new array as the value to prepend
        newArray[0] = valueToPrepend;
        // Copy the original array elements to the new array, starting from the second position
        System.arraycopy(originalArray, 0, newArray, 1, originalArray.length);
        return newArray;
    }
}
