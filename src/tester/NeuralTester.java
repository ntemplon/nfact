/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tester;

import com.jupiter.ganymede.math.vector.Vector;
import com.jupiter.ganymede.neural.NeuralNetLayer;
import com.jupiter.ganymede.neural.FeedForwardNetwork;
import com.jupiter.ganymede.neural.FeedForwardNetwork.DefaultWeightSettings;
import com.jupiter.ganymede.neural.HyperbolicTangentNeuron;
import com.jupiter.ganymede.neural.NeuralDecoder;
import com.jupiter.ganymede.neural.NeuralEncoder;
import com.jupiter.ganymede.neural.NeuralInterface;
import com.jupiter.ganymede.neural.SigmoidNeuron;
import com.jupiter.ganymede.neural.TestResults;
import com.jupiter.ganymede.neural.training.BackPropagationTrainer;
import com.jupiter.ganymede.neural.training.BackPropagationTrainer.TrainingRateFormulation;
import com.jupiter.ganymede.neural.training.ExitCriteria;
import com.jupiter.ganymede.neural.training.ManagedTrainer.TrainingPair;
import com.jupiter.ganymede.neural.training.TrainingResults;

/**
 *
 * @author Nathan Templon
 */
public class NeuralTester {

    public static void main(String args[]) {
        NeuralEncoder<String> encoder = (String data) -> {
            double[] output = new double[10];

            for (int i = 0; i < data.length(); i++) {
                output[i] = data.charAt(i) / 128.0;
            }

            return new Vector(output);
        };
        NeuralDecoder<String> decoder = (Vector output) -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= output.getDimension(); i++) {
                sb.append((char) Math.round(output.getComponent(i) * 128));
            }
            return sb.toString();
        };

        FeedForwardNetwork network = new FeedForwardNetwork(10, new DefaultWeightSettings(.1, 0.3), new NeuralNetLayer(20, new HyperbolicTangentNeuron()),
                new NeuralNetLayer(10, new SigmoidNeuron()));

        TrainingPair[] pairs = new TrainingPair[]{
            new TrainingPair(encoder.encode("QAPLA'"), encoder.encode("SUCCESS")),
            new TrainingPair(encoder.encode("BAT'LETH"), encoder.encode("WEAPON")),
            new TrainingPair(encoder.encode("NUQNEH"), encoder.encode("HELLO"))
        };

        for (TrainingPair pair : pairs) {
            System.out.println(decoder.decode(pair.input) + " -> " + decoder.decode(network.evaluate(pair.input)));
        }

        NeuralInterface<String, String> iface = new NeuralInterface<>(encoder, decoder, network);

        final int timesToTrain = 100000;

        TrainingRateFormulation trf = (int epoch) -> Math.max(0.01, 0.75 - ((double) epoch) / 200000.0);
        BackPropagationTrainer<FeedForwardNetwork> trainer = new BackPropagationTrainer<>(0.5);
        ExitCriteria<FeedForwardNetwork> exitCondition = (TrainingResults results, int epoch) -> {
            return epoch >= timesToTrain;
        };
        trainer.addTrainingPairs(pairs);

        int trainingTimes = 0;

        do {
            trainingTimes++;
            
            trainer.train(network, exitCondition);

            System.out.println(System.lineSeparator() + "Results from round: " + trainingTimes);
            for (TrainingPair pair : pairs) {
                System.out.println(decoder.decode(pair.input) + " -> " + decoder.decode(network.evaluate(pair.input)));
            }
        }
        while (trainingTimes <= 50 && !iface.evaluate("QAPLA'").trim().equals("SUCCESS"));

        System.out.println();
        for (TrainingPair pair : pairs) {
            System.out.println(decoder.decode(pair.input) + " -> " + decoder.decode(network.evaluate(pair.input)));
        }

        TestResults results = network.test(pairs);
        System.out.println(System.lineSeparator() + "RMS: " + results.calculateMetric(TestResults.RMS));
        System.out.println("Trained " + trainingTimes + " times.");
    }

}
