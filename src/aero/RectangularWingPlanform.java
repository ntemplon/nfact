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

/**
 *
 * @author nathan
 */
public class RectangularWingPlanform extends WingPlanform{
    // Fields
    private final double chord;
    private final double span;
    private final double area;
    
    private final SingleVariableFunction leadingEdgeFunction;
    private final SingleVariableFunction trailingEdgeFunction;
    private final SingleVariableFunction quarterChordLocation;
    
    
    // Initialization
    public RectangularWingPlanform(double chord, double span) {
        this.chord = chord;
        this.span = span;
        
        // Calculate fixed parameters
        this.area = chord * span;
        
        this.leadingEdgeFunction = new SingleVariableFunction(-1 * this.span, this.span) {
            @Override
            public Double evaluate(Double input) {
                return 0.0;
            }
        };
        this.trailingEdgeFunction = new SingleVariableFunction(-1 * this.span, this.span) {
            @Override
            public Double evaluate(Double input) {
                return RectangularWingPlanform.this.chord;
            }
        };
        this.quarterChordLocation = new SingleVariableFunction(-1 * this.span, this.span) {
            @Override
            public Double evaluate(Double input) {
                return RectangularWingPlanform.this.chord / 4.0;
            }
        };
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
    public SingleVariableFunction leadingEdgeLocation() {
        return this.leadingEdgeFunction;
    }

    @Override
    public SingleVariableFunction trailingEdgeLocation() {
        return this.trailingEdgeFunction;
    }

    @Override
    public SingleVariableFunction quarterChordLocation() {
        return this.quarterChordLocation;
    }
    
}
