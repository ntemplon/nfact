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
 * @author Nathan Templon
 * @param <T>
 */
public class TimeExitCondition implements ExitCondition {

    // Fields
    private final double simulationTime;


    // Properties
    public double getSimulationTime() {
        return this.simulationTime;
    }


    // Initialization
    public TimeExitCondition(double simulationTime) {
        this.simulationTime = simulationTime;
    }


    @Override
    public boolean isFinished(SystemState state) {
        try {
            if (state.get(DynamicSystem.TIME) > this.simulationTime) {
                return true;
            }
        }
        catch (Exception ex) {
            // Will fall through to the "return false" statement directly below
        }
        return false;
    }

}
