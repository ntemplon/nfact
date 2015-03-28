/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
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

import aero.AeroReferenceQuantities;
import aero.fluid.Fluid;
import aero.fluid.IdealGas;
import com.jupiter.ganymede.math.geometry.Angle;
import com.jupiter.ganymede.math.vector.Vector;
import com.jupiter.ganymede.math.vector.Vector3;
import dynamics.AerodynamicSystem;
import dynamics.SystemState;
import dynamics.airplane.PDRSeniorDesignPlane;
import dynamics.airplane.Sharp3;
import dynamics.analysis.simulation.PitchOverExitCondition;
import dynamics.analysis.simulation.PitchOverRecorder;
import dynamics.analysis.simulation.Simulation;
import dynamics.analysis.simulation.SimulationRecorder;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Nathan Templon
 */
public class Sharp3Tester {
    
    public static void main(String[] args) {
        AeroReferenceQuantities reference = new AeroReferenceQuantities(
                8.0 / 12.0, // Chord
                Math.PI * (16.0 / 144.0), // Area
                8.0 / 12.0 // Span
        );

        Fluid fluid = new IdealGas(
                28.97, // Molar Mass
                1.4, // Heat Ratio
                3.86e-7);       // Viscosity

        double intialTime = 0.0;
        Vector initialVector = new Vector(
                0, // X Position
                0, // X Velocity
                0, // Y Position
                0, // Y Velocity
                0, // Z Position
                -0.5, // Z Velocity
                (new Angle(0.0, Angle.AngleType.DEGREES)).getMeasure(Angle.MeasureRange.PlusMinus), // Phi Position
                0, // Phi Velocity
                (new Angle(90.0, Angle.AngleType.DEGREES)).getMeasure(Angle.MeasureRange.PlusMinus), // Theta Position
                0, // Theta Velocity
                (new Angle(0.0, Angle.AngleType.DEGREES)).getMeasure(Angle.MeasureRange.PlusMinus), // Psi PositionA
                0 // Psi Velocity
        );

        SystemState initialState = new SystemState(intialTime, initialVector, new HashMap<>());

        Sharp3 rocket = new Sharp3();

        AerodynamicSystem system = new AerodynamicSystem(rocket, reference, rocket, rocket,
                initialState, fluid,
                (double time) -> new Vector3(0, 0, 0) // Wind Model
        );
        system.setUseLaunchRod(true);
        
        File linuxFile = new File("/home/nathan/out-rocket.csv");
        File windowsFile = new File("D:\\out-rocket.csv");
        File file;
        if (new File("/home/nathan").exists()) {
            file = linuxFile;
        }
        else {
            file = windowsFile;
        }

        SimulationRecorder recorder = new PitchOverRecorder(file, 5);
        Simulation sim = new Simulation(system, new PitchOverExitCondition(), recorder, 0.05);
//        Simulation sim = new Simulation(system, new TimeExitCondition(20), recorder, 0.01);
        sim.run();
    }
    
}
