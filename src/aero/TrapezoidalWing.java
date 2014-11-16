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

import function.SingleVariableFunction;
import geometry.angle.Angle;

/**
 *
 * @author NathanT
 */
public class TrapezoidalWing extends Wing {
    
    // Private Members
    private final TrapezoidWingPlanform planform;
    private final Airfoil airfoil;
    
    private SingleVariableFunction liftCoeff;
    private SingleVariableFunction dragCoeff;
    private SingleVariableFunction pmCoeff;
    
    
    // Initialization
    public TrapezoidalWing(TrapezoidWingPlanform planform, Airfoil airfoil) {
        this.planform = planform;
        this.airfoil = airfoil;
        
        this.generateCoeffFunctions();
    }
    
    private void generateCoeffFunctions() {
        // CL
        this.liftCoeff = new SingleVariableFunction(this.airfoil.clFunction().domainMin, this.airfoil.clFunction().domainMax) {
            @Override
            public Double evaluate(Double input) {
                return 0.8 * airfoil.cl(input);
            }
        };
        
        // CD
        this.dragCoeff = new SingleVariableFunction(this.airfoil.cdFunction().domainMin, this.airfoil.cdFunction().domainMax) {
            @Override
            public Double evaluate(Double input) {
                double cd0 = 0.04;
                double cdi = (liftCoeff.evaluate(input) * liftCoeff.evaluate(input))
                        / (Math.PI * TrapezoidalWing.this.spanEfficiency() * TrapezoidalWing.this.getPlanform().aspectRatio());
                return (cd0 + cdi);
            }
        };
        
        // CPM
        this.pmCoeff = new SingleVariableFunction(this.airfoil.cpmFunction().domainMin, this.airfoil.cpmFunction().domainMax) {
            @Override
            public Double evaluate(Double input) {
                return airfoil.cpm(input);
            }
        };
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
    public double cl(Angle alpha) {
        return this.liftCoeff.evaluate(getDataMeasureFor(alpha));
    }

    @Override
    public double cd(Angle alpha) {
        return this.dragCoeff.evaluate(getDataMeasureFor(alpha));
    }

    @Override
    public double cpm(Angle alpha) {
        return this.pmCoeff.evaluate(getDataMeasureFor(alpha));
    }
    
    @Override
    public SingleVariableFunction clFunction() {
        return this.liftCoeff;
    }
    
    @Override
    public SingleVariableFunction cdFunction() {
        return this.dragCoeff;
    }
    
    @Override
    public SingleVariableFunction cpmFunction() {
        return this.pmCoeff;
    }
    
    
    // Getters and setters
    @Override
    public WingPlanform getPlanform() {
        return this.planform;
    }
    
}
