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

package space;

/**
 *
 * @author Nathan Templon
 */
public class CelestialBody {
    
    public CelestialBody(String name, double mass) {
        this.name = name;
        this.mass = mass;
        this.mu = G * mass;
    }
    
    public String name() {
        return name;
    }
    
    public double mass() {
        return mass;
    }
    
    public double gravParam() {
        return mu;
    }
    
    
    @Override
    public String toString() {
        return this.name;
    }
    
    
    private String name;
    private double mass;
    private double mu;
    
    public static final double G = 6.67384E-20;
    
}
