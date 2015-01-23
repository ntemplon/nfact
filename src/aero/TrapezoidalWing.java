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
package aero;

import com.jupiter.ganymede.math.function.SingleVariableRealFunction;
import com.jupiter.ganymede.math.geometry.Angle;
import dynamics.AerodynamicSystem;
import dynamics.analysis.SystemState;

/**
 *
 * @author NathanT
 */
public class TrapezoidalWing extends Wing {
    
    // Private Members
    private final TrapezoidWingPlanform planform;
    private final Airfoil airfoil;
    
    private SingleVariableRealFunction liftCoeff;
    private SingleVariableRealFunction dragCoeff;
    private SingleVariableRealFunction pmCoeff;
    
    
    // Initialization
    public TrapezoidalWing(TrapezoidWingPlanform planform, Airfoil airfoil) {
        this.planform = planform;
        this.airfoil = airfoil;
        
        this.generateCoeffFunctions();
    }
    
    private void generateCoeffFunctions() {
        // CL
        this.liftCoeff = (Double input) -> 0.8 * airfoil.cl(input);
        
        // CD
        this.dragCoeff = (Double input) -> {
            double cd0 = 0.04;
            double cdi = (liftCoeff.evaluate(input) * liftCoeff.evaluate(input))
                    / (Math.PI * TrapezoidalWing.this.spanEfficiency() * TrapezoidalWing.this.getPlanform().aspectRatio());
            return (cd0 + cdi);
        };
        
        // CPM
        this.pmCoeff = airfoil::cpm;
    }
    
    
    // Public Members
    @Override
    public double spanEfficiency() {
        return 0.9;
    }
    
    public Angle leadingEdgeSweep() {
        return this.planform.leSweep();
    }
    
    public Angle trailingEdgeSweep() {
        return this.planform.teSweep();
    }
    
    public Angle quarterChordSweep() {
        return this.planform.quarterSweep();
    }
    
    @Override
    public WingSection sectionAt(double station) {
        return new WingSection(this.planform.xPositionAt(station),
                                this.planform.chordAt(station),
                                this.airfoil);
    }

    @Override
    public double cl(SystemState state) {
        return this.liftCoeff.evaluate(state.get(AerodynamicSystem.ANGLE_OF_ATTACK).getMeasure(Angle.AngleType.RADIANS, Angle.MeasureRange.PlusMinus180));
    }

    @Override
    public double cd(SystemState state) {
        return this.dragCoeff.evaluate(state.get(AerodynamicSystem.ANGLE_OF_ATTACK).getMeasure(Angle.AngleType.RADIANS, Angle.MeasureRange.PlusMinus180));
    }

    @Override
    public double cpm(SystemState state) {
        return this.pmCoeff.evaluate(state.get(AerodynamicSystem.ANGLE_OF_ATTACK).getMeasure(Angle.AngleType.RADIANS, Angle.MeasureRange.PlusMinus180));
    }
    
    @Override
    public SingleVariableRealFunction clFunction() {
        return this.liftCoeff;
    }
    
    @Override
    public SingleVariableRealFunction cdFunction() {
        return this.dragCoeff;
    }
    
    @Override
    public SingleVariableRealFunction cpmFunction() {
        return this.pmCoeff;
    }
    
    
    // Getters and setters
    @Override
    public WingPlanform getPlanform() {
        return this.planform;
    }
    
}
