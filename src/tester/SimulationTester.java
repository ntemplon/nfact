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
import com.jupiter.ganymede.math.geometry.Angle.AngleType;
import com.jupiter.ganymede.math.vector.Vector;
import dynamics.AerodynamicSystem;
import dynamics.SystemState;

/**
 *
 * @author nathan
 */
public class SimulationTester {
    
    public static void main(String args[]) {
        /*
        Conventions:
         * X: @Psi = 0, Positive Forward
         * Y: @Phi = 0, Positive Left
         * Z: Positive Against Gravity
         * Phi: Positive Right Wing Down
         * Theta: Positive Nose Up
        */
        
        AeroReferenceQuantities reference = new AeroReferenceQuantities(
                0.671,          // Chord
                1.8,          // Area
                2.683           // Span
        );
        
        Fluid fluid = new IdealGas(
                28.97,          // Molar Mass
                1.4,            // Heat Ratio
                3.86e-7);       // Viscosity
        
        double testStateVectorTime = 0.0;
        Vector testStateVector = new Vector(
                0,          // X Position
                10,          // X Velocity
                0,          // Y Position
                1,          // Y Velocity
                0,          // Z Position
                -1,          // Z Velocity
                0,          // Phi Position
                0,          // Phi Velocity
                (new Angle(5.0, AngleType.DEGREES)).getMeasure(Angle.MeasureRange.PlusMinus180),          // Theta Position
                0,          // Theta Velocity
                (new Angle(10.0, AngleType.DEGREES)).getMeasure(Angle.MeasureRange.PlusMinus180),          // Psi PositionA
                0           // Psi Velocity
        );
        
        AerodynamicSystem system = new AerodynamicSystem(null, reference, null, fluid);
        
        SystemState resultant = system.buildState(testStateVectorTime, testStateVector);
    }
    
}
