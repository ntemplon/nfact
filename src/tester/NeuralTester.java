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
import com.jupiter.ganymede.neural.BackPropagationTrainer;
import com.jupiter.ganymede.neural.FeedForwardNetwork;
import com.jupiter.ganymede.neural.ManagedTrainer;
import com.jupiter.ganymede.neural.ManagedTrainer.TrainingPair;
import com.jupiter.ganymede.neural.NeuralNetworkInputLayer;
import com.jupiter.ganymede.neural.NeuralNetworkLayer;
import com.jupiter.ganymede.neural.Perceptron;
import com.jupiter.ganymede.neural.SigmoidNeuron;
import com.jupiter.ganymede.neural.ThresholdNeuron;

/**
 *
 * @author Nathan Templon
 */
public class NeuralTester {

    public static void main(String args[]) {
        NeuralNetworkInputLayer inputLayer = new NeuralNetworkInputLayer(2);
        NeuralNetworkLayer hiddenLayer = new NeuralNetworkLayer(new SigmoidNeuron(), new SigmoidNeuron(), new SigmoidNeuron(), new SigmoidNeuron(), new SigmoidNeuron());
        NeuralNetworkLayer outputLayer = new NeuralNetworkLayer(new SigmoidNeuron());
//        Perceptron network = new Perceptron(0.1, inputLayer, outputLayer);
        FeedForwardNetwork network = new FeedForwardNetwork(0.1, inputLayer, hiddenLayer, outputLayer);
        
        ManagedTrainer<FeedForwardNetwork> trainer = new BackPropagationTrainer<>(0.22);
        trainer.addTrainingPair(new TrainingPair(new Vector(0, 0), new Vector(0)));
        trainer.addTrainingPair(new TrainingPair(new Vector(0, 1), new Vector(1)));
        trainer.addTrainingPair(new TrainingPair(new Vector(1, 0), new Vector(1)));
        trainer.addTrainingPair(new TrainingPair(new Vector(1, 1), new Vector(1)));
        
        boolean trained = trainer.train(network, 1000);
        
        System.out.println("Trained: " + trained);
        
        for(int i = 0; i <= 1; i++) {
            for (int j = 0; j<= 1; j++) {
                Vector input = new Vector(i, j);
                Vector output = network.evaluate(input);
                
                System.out.println(input + " -> " + output);
            }
        }
    }

}
