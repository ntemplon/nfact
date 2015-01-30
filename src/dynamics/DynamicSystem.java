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
package dynamics;

import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.math.vector.Vector;

/**
 *
 * @author nathant
 */
public abstract class DynamicSystem {

    // Constants
    public static final StateVariable<Double> TIME = new StateVariable<>("Time");

    public static final StateVariable<Double> X_ACCEL = new StateVariable<>("X Acceleration");
    public static final StateVariable<Double> Y_ACCEL = new StateVariable<>("Y Acceleration");
    public static final StateVariable<Double> Z_ACCEL = new StateVariable<>("Z Acceleration");
    public static final StateVariable<Double> PHI_ACCEL = new StateVariable<>("Phi Double Dot");
    public static final StateVariable<Double> THETA_ACCEL = new StateVariable<>("Theta Double Dot");
    public static final StateVariable<Double> PSI_ACCEL = new StateVariable<>("Psi Double Dot");

    public static final StateVariable<Double> X_VEL = new StateVariable<>("X Velocity");
    public static final StateVariable<Double> Y_VEL = new StateVariable<>("Y Velocity");
    public static final StateVariable<Double> Z_VEL = new StateVariable<>("Z Velocity");
    public static final StateVariable<Double> PHI_VEL = new StateVariable<>("Phi Dot");
    public static final StateVariable<Double> THETA_VEL = new StateVariable<>("Theta Dot");
    public static final StateVariable<Double> PSI_VEL = new StateVariable<>("Psi Dot");

    public static final StateVariable<Double> X_POS = new StateVariable<>("X Position");
    public static final StateVariable<Double> Y_POS = new StateVariable<>("Y Position");
    public static final StateVariable<Double> Z_POS = new StateVariable<>("Z Position");
    public static final StateVariable<Double> PHI_POS = new StateVariable<>("Phi");
    public static final StateVariable<Double> THETA_POS = new StateVariable<>("Theta");
    public static final StateVariable<Double> PSI_POS = new StateVariable<>("Psi");

    public static final StateVariable<Inertia> INERTIA = new StateVariable<>("Inertia");
    public static final StateVariable<Double> MASS = new StateVariable<>("Mass");

    public static final StateVariable<Double> SPEED = new StateVariable<>("Speed");


    // Events
    public final Event<StateUpdatedEventArgs> stateUpdated = new Event<>();


    // Fields
    private SystemState currentState;


    // Properties
    public final SystemState getCurrentState() {
        if (this.currentState == null) {
            this.currentState = this.getInitialState();
        }
        return this.currentState;
    }

    public abstract StateVariable[] getVectorVariables();

    public abstract SystemState getInitialState();


    // Initialization
    public DynamicSystem() {

    }


    // Public Methods
    public void update(double deltaT) {
        // Euler's Method, for Debugging
        SystemState startingState = this.getCurrentState();
        Vector initialStateVector = startingState.getStateVector();
        double startingTime = startingState.getTime();

        ComputeStepResults k1Results = this.computeStep(startingTime, initialStateVector);
        Vector k1 = k1Results.deltaVector;

        ComputeStepResults k2Results = this.computeStep(startingTime + deltaT / 2.0, initialStateVector.plus(
                k1.times(deltaT / 2.0)));
        Vector k2 = k2Results.deltaVector;

        ComputeStepResults k3Results = this.computeStep(startingTime + deltaT / 2.0, initialStateVector.plus(
                k2.times(deltaT / 2.0)));
        Vector k3 = k3Results.deltaVector;
        
        ComputeStepResults k4Results = this.computeStep(startingTime + deltaT, initialStateVector.plus(k3.times(deltaT)));
        Vector k4 = k4Results.deltaVector;
        
        double nextTime = startingTime + deltaT;
        Vector nextStateVector = initialStateVector.plus(k1.plus(k2.times(2)).plus(k3.times(2)).plus(k4).times(deltaT / 6.0));

        this.currentState = new SystemState(nextTime, nextStateVector, k1Results.initialState.getProperties());

        this.stateUpdated.dispatch(new StateUpdatedEventArgs(this.currentState));
    }

    public abstract ComputeStepResults computeStep(double time, Vector stateVector);


    // Nested Classes
    public static class ComputeStepResults {

        // Fields
        public final SystemState initialState;
        public final Vector deltaVector;


        // Initialization
        public ComputeStepResults(SystemState state, Vector deltaVector) {
            this.initialState = state;
            this.deltaVector = deltaVector;
        }
    }

    public static class StateUpdatedEventArgs {

        // Fields
        public final SystemState state;


        // Initialization
        public StateUpdatedEventArgs(SystemState state) {
            this.state = state;
        }

    }

}
