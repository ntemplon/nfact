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
package dynamics.analysis.simulation;

import dynamics.DynamicSystem;
import dynamics.SystemState;

/**
 *
 * @author nathan
 * @param <TSystem>
 * @param <TState>
 */
public class Simulation<TSystem extends DynamicSystem> implements Runnable {

    // Fields
    private final TSystem system;
    private final ExitCondition exit;
    private final SimulationRecorder recorder;
    private final double deltaT;


    // Initialization
    public Simulation(TSystem system, ExitCondition exit, double timeIncrement) {
        this.system = system;
        this.deltaT = timeIncrement;
        this.exit = exit;

        this.recorder = null;
    }

    public Simulation(TSystem system, ExitCondition exit, SimulationRecorder recorder,
            double timeIncrement) {
        this.system = system;
        this.exit = exit;
        this.recorder = recorder;
        this.deltaT = timeIncrement;
    }


    // Public Methods
    @Override
    public void run() {
        this.system.stateUpdated.addListener(this.recorder);

        SystemState state = this.system.getCurrentState();

        this.recorder.start();
        try {
            while (!this.exit.isFinished(state)) {
                this.system.update(deltaT);
                state = this.system.getCurrentState();
            }
        }
        catch (Exception ex) {
            System.out.println("Simulation exited with exception.");
        }
        this.recorder.finish();
    }

}
