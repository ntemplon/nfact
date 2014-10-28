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

/**
 *
 * @author NathanT
 */
public class WingSection {
    
    // Private Members
    private final double xPos;
    private final double chord;
    private final Airfoil airfoil;
    
    
    // Initialization
    public WingSection(double xPos, double chord, Airfoil airfoil) {
        this.xPos = xPos;
        this.chord = chord;
        this.airfoil = airfoil;
    }
    
    
    // Public Methods
    public double xPosition() {
        return this.xPos;
    }
    
    public double chord() {
        return this.chord;
    }
    
    public Airfoil airfoil() {
        return this.airfoil;
    }
    
}
