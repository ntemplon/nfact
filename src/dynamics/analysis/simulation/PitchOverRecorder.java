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

import dynamics.AeroSystemState;
import dynamics.DynamicSystemState;
import dynamics.SystemProperty;
import dynamics.SystemState;
import geometry.angle.Angle;
import geometry.angle.Angle.AngleType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 *
 * @author nathan
 */
public class PitchOverRecorder extends FileRecorder<AeroSystemState> {

    // Constants
    public static final SystemProperty[] RECORDED_VARIABLES = new SystemProperty[]{
        AeroSystemState.TIME, AeroSystemState.ANGLE_OF_ATTACK, AeroSystemState.CL, AeroSystemState.CD,
        AeroSystemState.CPM, AeroSystemState.DYNAMIC_PRESSURE, AeroSystemState.SPEED, AeroSystemState.FLIGHT_PATH_ANGLE,
        AeroSystemState.X_POS, AeroSystemState.Z_POS, AeroSystemState.ANGULAR_POS, AeroSystemState.X_VEL,
        AeroSystemState.Z_VEL, AeroSystemState.ANGULAR_VEL, AeroSystemState.X_ACCEL, AeroSystemState.Z_ACCEL,
        AeroSystemState.ANGULAR_ACCEL, AeroSystemState.AXIAL_LOAD_FACTOR, AeroSystemState.NORMAL_LOAD_FACTOR,
        AeroSystemState.THRUST, AeroSystemState.MASS
    };


    // Fields
    private final int recordFrequency;
    private int datapointCounter;

    private double maxQ = 0.0;
    private double maxSpeed = 0.0;
    private double maxH = 0.0;
    private double maxNormalLoadFactor = 0.0;
    private double minNormalLoadFactor = 0.0;
    private double maxAxialLoadFactor = 0.0;
    private double minAxialLoadFactor = 0.0;

    private double finalVelocity = 0.0;
    private double simulationTime = 0.0;
    private Angle finalTheta = new Angle(0.0);


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

    public Angle getFinalTheta() {
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
    public void finish(AeroSystemState finalState) {
        this.writeState(finalState);

        this.finalVelocity = finalState.get(DynamicSystemState.SPEED);
        this.simulationTime = finalState.get(SystemState.TIME);
        this.finalTheta = finalState.get(DynamicSystemState.ANGULAR_POS);

        // Write maximum / final metrics to file
        super.println("Max Q: " + this.getDecimalFormat().format(this.maxQ));
        super.println("Max Speed: " + this.getDecimalFormat().format(this.maxSpeed));
        super.println("Max Height: " + this.getDecimalFormat().format(this.maxH));
        super.println("Max Normal Load Factor: " + this.getDecimalFormat().format(this.maxNormalLoadFactor));
        super.println("Min Normal Load Factor: " + this.getDecimalFormat().format(this.minNormalLoadFactor));
        super.println("Max Axial Load Factor: " + this.getDecimalFormat().format(this.maxAxialLoadFactor));
        super.println("Min Axial Load Factor: " + this.getDecimalFormat().format(this.minAxialLoadFactor));

        super.println("Final X: " + this.getDecimalFormat().format(finalState.get(AeroSystemState.X_POS)));
        super.println("Final Z: " + this.getDecimalFormat().format(finalState.get(AeroSystemState.Z_POS)));
        super.println("Final Theta: " + this.getDecimalFormat().format(finalState.get(AeroSystemState.ANGULAR_POS).getMeasure(AngleType.DEGREES)));
        super.println("Final X Velocity: " + this.getDecimalFormat().format(finalState.get(AeroSystemState.X_VEL)));
        super.println("Final Z Velocity: " + this.getDecimalFormat().format(finalState.get(AeroSystemState.Z_VEL)));
        super.println("Final Angular Velocity: " + this.getDecimalFormat().format(finalState.get(AeroSystemState.ANGULAR_VEL)));

        this.close();
    }

    @Override
    public void recordState(AeroSystemState state) {
        double q = state.get(AeroSystemState.DYNAMIC_PRESSURE);
        if (q > this.maxQ) {
            this.maxQ = q;
            this.maxSpeed = state.get(DynamicSystemState.SPEED);
        }

        double h = state.get(AeroSystemState.Z_POS);
        if (h > this.maxH) {
            this.maxH = h;
        }

        double nNormal = state.get(AeroSystemState.NORMAL_LOAD_FACTOR);
        if (nNormal > this.maxNormalLoadFactor) {
            this.maxNormalLoadFactor = nNormal;
        }
        if (nNormal < this.minNormalLoadFactor) {
            this.minNormalLoadFactor = nNormal;
        }

        double nAxial = state.get(AeroSystemState.AXIAL_LOAD_FACTOR);
        if (nAxial > this.maxAxialLoadFactor) {
            this.maxAxialLoadFactor = nAxial;
        }
        if (nAxial < this.minAxialLoadFactor) {
            this.minAxialLoadFactor = nAxial;
        }

        this.datapointCounter++;
        if (this.datapointCounter >= this.recordFrequency) {
            this.datapointCounter = 0;
            super.writeState(state);
        }
    }
}
