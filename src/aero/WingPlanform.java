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
 * @author NathanT
 */
public abstract class WingPlanform {
    
    /**
     * 
     * @return Returns the area of the WingArea, in square feet
     */
    public abstract double area();
    
    /**
     * 
     * @return Returns the 
     */
    public abstract double span();
    
    /**
     * 
     * @return Returns the taper ratio of the WingArea
     */
    public abstract double taperRatio();
    
    /**
     * 
     * @param lateralPos The lateral position of the chord station, in feet
     * @return Returns the length of the chord at the provided station, in feet
     */
    public abstract double chordAt(double lateralPos);
    
    public abstract double xPositionAt(double lateralPos);
    
    public abstract SingleVariableRealFunction leadingEdgeLocation();
    
    public abstract SingleVariableRealFunction trailingEdgeLocation();
    
    public abstract SingleVariableRealFunction quarterChordLocation();
    
    
    // Public Methods
    public double aspectRatio() {
        return (this.span() * this.span()) / this.area();
    }
    
}
