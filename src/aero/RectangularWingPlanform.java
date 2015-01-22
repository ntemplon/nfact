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


/**
 *
 * @author nathan
 */
public class RectangularWingPlanform extends WingPlanform{
    // Fields
    private final double chord;
    private final double span;
    private final double area;
    
    private final SingleVariableRealFunction leadingEdgeFunction;
    private final SingleVariableRealFunction trailingEdgeFunction;
    private final SingleVariableRealFunction quarterChordLocation;
    
    
    // Initialization
    public RectangularWingPlanform(double chord, double span) {
        this.chord = chord;
        this.span = span;
        
        // Calculate fixed parameters
        this.area = chord * span;
        
        this.leadingEdgeFunction = (Double input) -> 0.0;
        this.trailingEdgeFunction = (Double input) -> RectangularWingPlanform.this.chord;
        this.quarterChordLocation = (Double input) -> RectangularWingPlanform.this.chord / 4.0;
    }
    
    @Override
    public double area() {
        return this.area;
    }

    @Override
    public double span() {
        return this.span;
    }

    @Override
    public double taperRatio() {
        return 1;
    }

    @Override
    public double chordAt(double lateralPos) {
        return this.chord;
    }

    @Override
    public double xPositionAt(double lateralPos) {
        return 0;
    }

    @Override
    public SingleVariableRealFunction leadingEdgeLocation() {
        return this.leadingEdgeFunction;
    }

    @Override
    public SingleVariableRealFunction trailingEdgeLocation() {
        return this.trailingEdgeFunction;
    }

    @Override
    public SingleVariableRealFunction quarterChordLocation() {
        return this.quarterChordLocation;
    }
    
}
