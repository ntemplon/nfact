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
    protected SingleVariableRealFunction liftCoeff;
    protected SingleVariableRealFunction dragCoeff;
    protected SingleVariableRealFunction pmCoeff;
    
    
    // Initialization
    public Airfoil(String name, SingleVariableRealFunction liftCoeff,
                    SingleVariableRealFunction dragCoeff,
                    SingleVariableRealFunction pmCoeff) {
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
    
    public SingleVariableRealFunction clFunction() {
        return this.liftCoeff;
    }
    
    public double cd(Angle alpha) {
        return this.dragCoeff.evaluate(getDataMeasureFor(alpha));
    }
    
    public double cd(double alpha) {
        return this.dragCoeff.evaluate(alpha);
    }
    
    public SingleVariableRealFunction cdFunction() {
        return this.dragCoeff;
    }
    
    public double cpm(Angle alpha) {
        return this.pmCoeff.evaluate(getDataMeasureFor(alpha));
    }
    
    public double cpm(double alpha) {
        return this.pmCoeff.evaluate(alpha);
    }
    
    public SingleVariableRealFunction cpmFunction() {
        return this.pmCoeff;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
}
