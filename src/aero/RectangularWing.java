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
import dynamics.AeroSystemState;
import geometry.angle.Angle;
import geometry.angle.Angle.AngleType;

/**
 *
 * @author nathan
 */
public class RectangularWing extends Wing {
    // Fields
    private final RectangularWingPlanform planform;
    private final Airfoil airfoil;
    
    private final SingleVariableRealFunction liftCoeff;
    private final SingleVariableRealFunction dragCoeff;
    private final SingleVariableRealFunction pmCoeff;
    
    
    // Initialization
    public RectangularWing(double chord, double span, Airfoil airfoil) {
        this.planform = new RectangularWingPlanform(chord, span);
        this.airfoil = airfoil;
        
        this.liftCoeff = (Double input) -> 0.8 * RectangularWing.this.airfoil.cl(input);
        this.dragCoeff = (Double input) -> {
            double cd0 = 0.04;
            double cdi = (liftCoeff.evaluate(input) * liftCoeff.evaluate(input))
                    / (Math.PI * RectangularWing.this.spanEfficiency() * RectangularWing.this.getPlanform().aspectRatio());
            return (cd0 + cdi);
        };
        this.pmCoeff = RectangularWing.this.airfoil::cpm;
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
    public double cl(AeroSystemState state) {
        return this.liftCoeff.evaluate(state.get(AeroSystemState.ANGLE_OF_ATTACK).getMeasure(AngleType.RADIANS, Angle.MeasureRange.PlusMin180));
    }

    @Override
    public double cd(AeroSystemState state) {
        return this.dragCoeff.evaluate(state.get(AeroSystemState.ANGLE_OF_ATTACK).getMeasure(AngleType.RADIANS, Angle.MeasureRange.PlusMin180));
    }

    @Override
    public double cpm(AeroSystemState state) {
        return this.pmCoeff.evaluate(state.get(AeroSystemState.ANGLE_OF_ATTACK).getMeasure(AngleType.RADIANS, Angle.MeasureRange.PlusMin180));
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
    
    
    // Getters and Setters
    @Override
    public WingPlanform getPlanform() {
        return this.planform;
    }
    
    public Airfoil getAirfoil() {
        return this.airfoil;
    }
    
}
