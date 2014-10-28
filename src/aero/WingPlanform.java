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
    
    public abstract SingleVariableFunction leadingEdgeLocation();
    
    public abstract SingleVariableFunction trailingEdgeLocation();
    
    public abstract SingleVariableFunction quarterChordLocation();
    
    
    // Public Methods
    public double aspectRatio() {
        return (this.span() * this.span()) / this.area();
    }
    
}
