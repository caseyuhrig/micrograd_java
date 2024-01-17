package caseyuhrig.micrograd;

import java.util.ArrayList;

public class Main {

    public static void main(final String[] args) {
        System.out.println("Hello, MicroGrad Java!");

        final ArrayList<ArrayList<Value>> xs = Utils.of(new double[][]{
                {2.0, 3.0, -1.0},
                {3.0, -1.0, 0.5},
                {0.5, 1.0, 1.0},
                {1.0, 1.0, -1.0},
        });
        final ArrayList<Value> ys = Utils.of(new double[]{1.0, -1.0, -1.0, 1.0}); // desired targets

        // Create new MLP with 3 inputs, 2 hidden layers with 4 neurons each, and 1 output
        final var m = new MLP(3, new int[]{4, 4, 1});

        final double loss = m.train(xs, ys, 5000);

        //System.out.println(m.parameters());
        System.out.println(Utils.wrap(m.parameters().toString(), 128));
        System.out.printf("LOSS: %.18f\n", loss);
    }
}
