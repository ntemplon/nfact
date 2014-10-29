/*
 * Copyright (C) 2014 Nathan Templon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
