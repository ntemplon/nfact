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

import function.SingleVariableFormulaFunction;
import function.SingleVariableFunction;
import geometry.angle.Angle;

/**
 *
 * @author nathan
 */
public class RectangularWing extends Wing {
    // Fields
    private final RectangularWingPlanform planform;
    private final Airfoil airfoil;
    
    private final SingleVariableFunction liftCoeff;
    private final SingleVariableFunction dragCoeff;
    private final SingleVariableFunction pmCoeff;
    
    
    // Initialization
    public RectangularWing(double chord, double span, Airfoil airfoil) {
        this.planform = new RectangularWingPlanform(chord, span);
        this.airfoil = airfoil;
        
        this.liftCoeff = new SingleVariableFormulaFunction(this.airfoil.clFunction().domainMin, this.airfoil.clFunction().domainMax) {
            @Override
            public Double evaluate(Double input) {
                return 0.8 * RectangularWing.this.airfoil.cl(input);
            }
        };
        this.dragCoeff = new SingleVariableFormulaFunction(this.airfoil.cdFunction().domainMin, this.airfoil.cdFunction().domainMax) {
            @Override
            public Double evaluate(Double input) {
                double cd0 = 0.04;
                double cdi = (liftCoeff.evaluate(input) * liftCoeff.evaluate(input))
                        / (Math.PI * RectangularWing.this.spanEfficiency() * RectangularWing.this.getPlanform().aspectRatio());
                return (cd0 + cdi);
            }
        };
        this.pmCoeff = new SingleVariableFormulaFunction(this.airfoil.cpmFunction().domainMin, this.airfoil.cpmFunction().domainMax) {
            @Override
            public Double evaluate(Double input) {
                return RectangularWing.this.airfoil.cpm(input);
            }
        };
    }
    
    
    // Public Methods
    @Override
    public double spanEfficiency() {
        return 0.85;
    }

    @Override
    public WingSection sectionAt(double station) {
        return new WingSection(this.getPlanform().xPositionAt(station)
                , this.getPlanform().chordAt(station), this.airfoil);
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
    
    
    // Getters and Setters
    @Override
    public WingPlanform getPlanform() {
        return this.planform;
    }
    
    public Airfoil getAirfoil() {
        return this.airfoil;
    }
    
}
