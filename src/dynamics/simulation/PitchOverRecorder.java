/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics.simulation;

import dynamics.AeroSystemState;
import dynamics.StateVariable;
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
public class PitchOverRecorder implements SimulationRecorder<AeroSystemState>, AutoCloseable {

    // Constants
    public static final StateVariable[] RECORDED_VARIABLES = new StateVariable[]{
        AeroSystemState.TIME, AeroSystemState.ALPHA, AeroSystemState.CL, AeroSystemState.CD,
        AeroSystemState.CPM, AeroSystemState.Q, AeroSystemState.SPEED, AeroSystemState.FLIGHT_PATH_ANGLE,
        AeroSystemState.X_POS, AeroSystemState.Z_POS, AeroSystemState.ANGULAR_POS, AeroSystemState.X_VEL,
        AeroSystemState.Z_VEL, AeroSystemState.ANGULAR_VEL, AeroSystemState.X_ACCEL, AeroSystemState.Z_ACCEL,
        AeroSystemState.ANGULAR_ACCEL, AeroSystemState.AXIAL_LOAD_FACTOR, AeroSystemState.NORMAL_LOAD_FACTOR,
        AeroSystemState.THRUST
    };


    // Fields
    private final File file;
    private FileWriter fw;
    private BufferedWriter bw;
    private PrintWriter pw;
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

    private final DecimalFormat format = new DecimalFormat("0.0000");
    
    
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
        this.file = file;
        this.recordFrequency = recordFrequency;
    }


    // SimulationRecorder Implementation
    @Override
    public void start(AeroSystemState initialState) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                return;
            }
        }
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            for (int i = 0; i < RECORDED_VARIABLES.length; i++) {
                StateVariable variable = RECORDED_VARIABLES[i];
                pw.print(variable.getName());
                if (i < RECORDED_VARIABLES.length - 1) {
                    pw.print(",");
                } else {
                    pw.println("");
                }
            }

            this.writeState(initialState);
        } catch (IOException ex) {

        }
    }

    @Override
    public void finish(AeroSystemState finalState) {
        this.writeState(finalState);
        
        this.finalVelocity = finalState.getSpeed();
        this.simulationTime = finalState.get(AeroSystemState.TIME);
        this.finalTheta = finalState.get(AeroSystemState.ANGULAR_POS);

        // Write maximum / final metrics to file
        this.pw.println("Max Q: " + format.format(this.maxQ));
        this.pw.println("Max Speed: " + format.format(this.maxSpeed));
        this.pw.println("Max Height: " + format.format(this.maxH));
        this.pw.println("Max Normal Load Factor: " + format.format(this.maxNormalLoadFactor));
        this.pw.println("Min Normal Load Factor: " + format.format(this.minNormalLoadFactor));
        this.pw.println("Max Axial Load Factor: " + format.format(this.maxAxialLoadFactor));
        this.pw.println("Min Axial Load Factor: " + format.format(this.minAxialLoadFactor));

        this.pw.println("Final X: " + format.format(finalState.get(AeroSystemState.X_POS)));
        this.pw.println("Final Z: " + format.format(finalState.get(AeroSystemState.Z_POS)));
        this.pw.println("Final Theta: " + format.format(finalState.get(AeroSystemState.ANGULAR_POS).getMeasure(AngleType.DEGREES)));
        this.pw.println("Final X Velocity: " + format.format(finalState.get(AeroSystemState.X_VEL)));
        this.pw.println("Final Z Velocity: " + format.format(finalState.get(AeroSystemState.Z_VEL)));
        this.pw.println("Final Angular Velocity: " + format.format(finalState.get(AeroSystemState.ANGULAR_VEL)));

        this.close();
    }

    @Override
    public void recordState(AeroSystemState state) {
        if (fw == null || pw == null) {
            return;
        }

        double q = state.get(AeroSystemState.Q);
        if (q > this.maxQ) {
            this.maxQ = q;
            this.maxSpeed = state.getSpeed();
        }

        double h = state.get(AeroSystemState.Z_POS);
        if (h > this.maxH) {
            this.maxH = h;
        }

        double nNormal = state.getNormalLoadFactor();
        if (nNormal > this.maxNormalLoadFactor) {
            this.maxNormalLoadFactor = nNormal;
        }
        if (nNormal < this.minNormalLoadFactor) {
            this.minNormalLoadFactor = nNormal;
        }

        double nAxial = state.getAxialLoadFactor();
        if (nAxial > this.maxAxialLoadFactor) {
            this.maxAxialLoadFactor = nAxial;
        }
        if (nAxial < this.minAxialLoadFactor) {
            this.minAxialLoadFactor = nAxial;
        }

        this.datapointCounter++;
        if (this.datapointCounter >= this.recordFrequency) {
            this.datapointCounter = 0;
            this.writeState(state);
        }
    }


    // AutoCloseable implementation
    @Override
    public void close() {
        if (this.pw != null) {
            this.pw.close();
        }
        if (this.bw != null) {
            try {
                this.bw.close();
            } catch (IOException ex) {

            }
        }
        if (this.fw != null) {
            try {
                this.fw.close();
            } catch (IOException ex) {

            }
        }
    }


    // Private Methods
    private void writeState(AeroSystemState state) {
        for (int i = 0; i < RECORDED_VARIABLES.length; i++) {
            StateVariable variable = RECORDED_VARIABLES[i];

            Object value = state.get(variable);
            if (value instanceof Angle) {
                pw.print(format.format(((Angle) value).getMeasure(Angle.AngleType.DEGREES)));
            } else if (value instanceof Double) {
                pw.print(format.format((Double)value));
            } else {
                pw.print(value);
            }

            if (i < RECORDED_VARIABLES.length - 1) {
                pw.print(",");
            } else {
                pw.println("");
            }
        }
    }

}
