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
import geometry.angle.Angle.AngleType;

/**
 *
 * @author NathanT
 */
public class Airfoil {
    
    // Protected Static Methods
    protected double getDataMeasureFor(Angle angle) {
        double measure = angle.getMeasure(AngleType.DEGREES);
        if (measure > 180) {
            measure = measure * -1.0;
        }
        return measure;
    }
    
    
    // Protected Members
    protected String name;
    protected SingleVariableFunction liftCoeff;
    protected SingleVariableFunction dragCoeff;
    protected SingleVariableFunction pmCoeff;
    
    
    // Initialization
    public Airfoil(String name, SingleVariableFunction liftCoeff,
                    SingleVariableFunction dragCoeff,
                    SingleVariableFunction pmCoeff) {
        this.liftCoeff = liftCoeff;
        this.dragCoeff = dragCoeff;
        this.pmCoeff = pmCoeff;
    }
    
    
    // Public Methods
    public double cl(Angle alpha) {
        return this.liftCoeff.evaluate(getDataMeasureFor(alpha));
    }
    
    public double cl(double alpha) {
        return this.liftCoeff.evaluate(alpha);
    }
    
    public SingleVariableFunction clFunction() {
        return this.liftCoeff;
    }
    
    public double cd(Angle alpha) {
        return this.dragCoeff.evaluate(getDataMeasureFor(alpha));
    }
    
    public double cd(double alpha) {
        return this.dragCoeff.evaluate(alpha);
    }
    
    public SingleVariableFunction cdFunction() {
        return this.dragCoeff;
    }
    
    public double cpm(Angle alpha) {
        return this.pmCoeff.evaluate(getDataMeasureFor(alpha));
    }
    
    public double cpm(double alpha) {
        return this.pmCoeff.evaluate(alpha);
    }
    
    public SingleVariableFunction cpmFunction() {
        return this.pmCoeff;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
}
