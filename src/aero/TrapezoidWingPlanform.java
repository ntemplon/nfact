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
public class TrapezoidWingPlanform extends WingPlanform {

    // Private Members
    private final double rootChord;
    private final double tipChord;
    private final double span;
    private final Angle leadSweep;
    
    private final double area;
    private final double taperRatio;
    private final Angle trailSweep;
    private final Angle quarterSweep;
    
    private final SingleVariableFunction leadingEdgeLocation;
    private final SingleVariableFunction trailingEdgeLocation;
    private final SingleVariableFunction quarterChordLocation;
    
            
    // Initialization
    /**
     * Creates a new Trapezoidal Wing Area
     * @param rootChord The length of the root chord, in feet
     * @param tipChord The length of the tip chord, in feet
     * @param span The span of the area, in feet
     * @param leadSweep The angle by which the leading edge of the wing is swept back
     */
    public TrapezoidWingPlanform(double rootChord, double tipChord, double span, final Angle leadSweep) {
        this.rootChord = rootChord;
        this.tipChord = tipChord;
        this.span = span;
        this.leadSweep = leadSweep;
        
        // Calculations of parameters
        this.area = this.span * ((this.rootChord + this.tipChord) / 2.0);
        this.taperRatio = this.tipChord / this.rootChord;
        
        double tipOffset = this.leadSweep.tan() * this.span;
        double tipTrailRelative = (tipOffset + this.tipChord) - this.rootChord;
        this.trailSweep = new Angle(tipTrailRelative / this.span, Angle.TrigFunction.TANGENT);
        
        double quarterPosRelative = (tipOffset + this.tipChord / 4.0) - (this.rootChord / 4.0);
        this.quarterSweep = new Angle(quarterPosRelative / this.span, Angle.TrigFunction.TANGENT);
        
        // Calculations of functions
        this.leadingEdgeLocation = new SingleVariableFunction(-1 * this.span, this.span) {

            @Override
            public Double evaluate(Double input) {
                return leadSweep.tan() * input;
            }
        };
        
        this.quarterChordLocation = new SingleVariableFunction(-1 * this.span, this.span) {

            @Override
            public Double evaluate(Double input) {
                return quarterSweep.tan() * input;
            }
        };
        
        this.trailingEdgeLocation = new SingleVariableFunction(-1 * this.span, this.span) {

            @Override
            public Double evaluate(Double input) {
                return trailSweep.tan() * input;
            }
        };
    }
    
    /**
     * 
     * @return Returns the area of the wing area, in square feet
     */
    @Override
    public double area() {
        return this.area;
    }

    /**
     * 
     * @return Returns the span of the wing, in feet
     */
    @Override
    public double span() {
        return this.span;
    }

    /**
     * 
     * @return Returns the taper ratio of the wing
     */
    @Override
    public double taperRatio() {
        return this.taperRatio;
    }

    /**
     * 
     * @return Returns the sweep angle of the leading edge
     */
    public Angle leSweep() {
        return this.leadSweep;
    }

    /**
     * 
     * @return Returns the sweep angle of the trailing edge of the area
     */
    public Angle teSweep() {
        return this.trailSweep;
    }

    /**
     * 
     * @return Returns the sweep of the quarter chord of the area
     */
    public Angle quarterSweep() {
        return this.quarterSweep;
    }

    /**
     * 
     * @param lateralPos The lateral station to retrieve the chord at, in feet
     * @return The length of the chord at the lateral position, in feet
     */
    @Override
    public double chordAt(double lateralPos) {
        if (lateralPos > this.span || lateralPos < 0) {
            return 0;
        }
        
        double fracSpan = lateralPos / this.span;
        return this.rootChord - (this.rootChord - this.tipChord) * fracSpan;
    }
    
    @Override
    public double xPositionAt(double lateralPos) {
        if (lateralPos < 0) {
            return 0;
        }
        else if (lateralPos > this.span) {
            return this.leadSweep.tan() * this.span;
        }
        else {
            return this.leadSweep.tan() * lateralPos;
        }
    }

    @Override
    public SingleVariableFunction leadingEdgeLocation() {
        return this.leadingEdgeLocation;
    }

    @Override
    public SingleVariableFunction trailingEdgeLocation() {
        return this.trailingEdgeLocation;
    }

    @Override
    public SingleVariableFunction quarterChordLocation() {
        return this.quarterChordLocation;
    }
    
}
