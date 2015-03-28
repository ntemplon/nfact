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

import dynamics.AerodynamicSystem;
import dynamics.DynamicSystem;
import dynamics.DynamicSystem.StateUpdatedEventArgs;
import dynamics.SystemProperty;
import dynamics.SystemState;
import java.io.File;

/**
 *
 * @author nathan
 */
public class PitchOverRecorder extends FileRecorder {

    // Constants
    public static final SystemProperty[] RECORDED_VARIABLES = new SystemProperty[]{
        DynamicSystem.TIME,
        AerodynamicSystem.ANGLE_OF_ATTACK_GEOMETRIC,
        AerodynamicSystem.ANGLE_OF_ATTACK_TOTAL,
        AerodynamicSystem.SIDESLIP_ANGLE,
        AerodynamicSystem.CL,
        AerodynamicSystem.CD,
        AerodynamicSystem.CSF,
        AerodynamicSystem.CRM,
        AerodynamicSystem.CPM,
        AerodynamicSystem.CYM,
        AerodynamicSystem.DYNAMIC_PRESSURE,
        DynamicSystem.SPEED,
        AerodynamicSystem.FLIGHT_PATH_ANGLE,
        DynamicSystem.X_POS,
        DynamicSystem.Y_POS,
        DynamicSystem.Z_POS,
        DynamicSystem.X_VEL,
        DynamicSystem.Y_VEL,
        DynamicSystem.Z_VEL,
        DynamicSystem.X_ACCEL,
        DynamicSystem.Y_ACCEL,
        DynamicSystem.Z_ACCEL,
        DynamicSystem.THETA_POS,
        DynamicSystem.THETA_VEL,
        DynamicSystem.THETA_ACCEL,
        AerodynamicSystem.ROLL_RATE,
        AerodynamicSystem.PITCH_RATE,
        AerodynamicSystem.YAW_RATE,
        AerodynamicSystem.LIFT,
        AerodynamicSystem.DRAG,
        AerodynamicSystem.PITCHING_MOMENT,
        AerodynamicSystem.AXIAL_LOAD_FACTOR,
        AerodynamicSystem.NORMAL_LOAD_FACTOR,
        DynamicSystem.MASS,
        AerodynamicSystem.THRUST,
        AerodynamicSystem.Q_HAT,
        AerodynamicSystem.CPM0,
        AerodynamicSystem.CPMA,
        AerodynamicSystem.CPM_FROM_A,
        AerodynamicSystem.CPM_FROM_Q
    };


    // Fields
    private final int recordFrequency;
    private int datapointCounter;
    private SystemState lastState;

    private double maxQ = 0.0;
    private double maxSpeed = 0.0;
    private double maxH = 0.0;
    private double maxNormalLoadFactor = 0.0;
    private double minNormalLoadFactor = 0.0;
    private double maxAxialLoadFactor = 0.0;
    private double minAxialLoadFactor = 0.0;

    private double finalVelocity = 0.0;
    private double simulationTime = 0.0;
    private double finalTheta = 0.0;

    private boolean writtenFirst = false;
    private SystemState lastWrittenState;


    // Properties
    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public double getFinalVelocity() {
        return this.finalVelocity;
    }

    public double getSimulationTime() {
        return this.simulationTime;
    }

    public double getMinNormalLoadFactor() {
        return this.minNormalLoadFactor;
    }

    public double getMaxAxialLoadFactor() {
        return this.maxAxialLoadFactor;
    }

    public double getFinalTheta() {
        return this.finalTheta;
    }

    public double getMaxAltitude() {
        return this.maxH;
    }


    // Initialization
    public PitchOverRecorder(File file, int recordFrequency) {
        super(file, RECORDED_VARIABLES);
        this.recordFrequency = recordFrequency;
    }


    // FileRecorder Overrides
    @Override
    public void finish() {
        if (this.lastState != null) {
            if (!this.lastState.equals(this.lastWrittenState)) {
                super.writeState(this.lastState);
            }

            this.finalVelocity = this.lastState.get(DynamicSystem.SPEED);
            this.simulationTime = this.lastState.get(DynamicSystem.TIME);
            this.finalTheta = this.lastState.get(DynamicSystem.THETA_POS);

            // Write maximum / final metrics to file
            super.println("Max Q: " + this.getDecimalFormat().format(this.maxQ));
            super.println("Max Speed: " + this.getDecimalFormat().format(this.maxSpeed));
            super.println("Max Height: " + this.getDecimalFormat().format(this.maxH));
            super.println("Max Normal Load Factor: " + this.getDecimalFormat().format(this.maxNormalLoadFactor));
            super.println("Min Normal Load Factor: " + this.getDecimalFormat().format(this.minNormalLoadFactor));
            super.println("Max Axial Load Factor: " + this.getDecimalFormat().format(this.maxAxialLoadFactor));
            super.println("Min Axial Load Factor: " + this.getDecimalFormat().format(this.minAxialLoadFactor));

            super.println("Final X: " + this.getDecimalFormat().format(this.lastState.get(DynamicSystem.X_POS)));
            super.println("Final Z: " + this.getDecimalFormat().format(this.lastState.get(DynamicSystem.Z_POS)));
            super.println("Final Theta: " + this.getDecimalFormat().format(this.lastState.get(DynamicSystem.THETA_POS)));
            super.println("Final X Velocity: " + this.getDecimalFormat().format(this.lastState.get(DynamicSystem.X_VEL)));
            super.println("Final Z Velocity: " + this.getDecimalFormat().format(this.lastState.get(DynamicSystem.Z_VEL)));
            super.println("Final Angular Velocity: " + this.getDecimalFormat().format(this.lastState.get(DynamicSystem.THETA_VEL)));
        }

        this.close();
    }

    @Override
    public void handle(StateUpdatedEventArgs e) {
        SystemState state = e.state;

        double q = state.get(AerodynamicSystem.DYNAMIC_PRESSURE);
        if (q > this.maxQ) {
            this.maxQ = q;
            this.maxSpeed = state.get(DynamicSystem.SPEED);
        }

        double h = state.get(AerodynamicSystem.Z_POS);
        if (h > this.maxH) {
            this.maxH = h;
        }

        double nNormal = state.get(AerodynamicSystem.NORMAL_LOAD_FACTOR);
        if (nNormal > this.maxNormalLoadFactor) {
            this.maxNormalLoadFactor = nNormal;
        }
        if (nNormal < this.minNormalLoadFactor) {
            this.minNormalLoadFactor = nNormal;
        }

        double nAxial = state.get(AerodynamicSystem.AXIAL_LOAD_FACTOR);
        if (nAxial > this.maxAxialLoadFactor) {
            this.maxAxialLoadFactor = nAxial;
        }
        if (nAxial < this.minAxialLoadFactor) {
            this.minAxialLoadFactor = nAxial;
        }

        this.datapointCounter++;
        if (this.datapointCounter >= this.recordFrequency || !this.writtenFirst) {
            this.writtenFirst = true;
            this.datapointCounter = 0;
            super.writeState(state);
            this.lastWrittenState = state;
        }

        this.lastState = state;
    }
}
