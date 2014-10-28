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

package geometry;

/**
 * A class representing a geometric circle
 * @author Hortator
 */
public class Circle implements PlaneArea{
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Constructors  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A default constructor that creates a new Circle of radius 0.
     */
    public Circle() {
        this.radius = new Radius(0);
        this.diameter = new Diameter(0);
        this.area = new Area(0);
    }
    
    
    /**
     * A constructor that builds a Circle based off of its diameter.
     * @param diameter the diameter of the circle to be created
     */
    public Circle( Diameter diameter ) {
        this.radius = new Radius(diameter.value() / 2);
        this.diameter = diameter;
        this.area = new Area((Math.PI * diameter.value() * diameter.value()) / 4);
    }
    
    
    /**
     * A constructor that builds a Circle based off of its radius.
     * @param radius the radius of the circle to be created
     */
    public Circle( Radius radius ) {
        this.radius = radius;
        this.diameter = new Diameter(radius.value() * 2);
        this.area = new Area((Math.PI * radius.value() * radius.value()));
    }
    
    
    /**
     * A constructor that builds a Circle based off of its area.
     * @param area the area of the circle to be created
     */
    public Circle( Area area ) {
        double rad = Math.sqrt(area.value() / Math.PI);
        this.radius = new Radius(rad);
        this.diameter = new Diameter(rad * 2);
        this.area = area;
    }
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -----------------------  Property Returners   ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    
    /**
     * A method that returns the Area of the Circle
     * @return Returns the area of the circle as an Area object
     */
    @Override
    public Area area() {
        return area;
    }
    
    
    /**
     * A method that returns the Radius of the Circle
     * @return Returns the radius of the circle as a Radius object
     */
    public Radius radius() {
        return radius;
    }
    
    
    /**
     * A method that returns the Diameter of the Circle
     * @return Returns the diameter of the circle as a Diameter object
     */
    public Diameter diameter() {
        return diameter;
    }
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  State Variables  -----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    
    private Radius radius;
    private Diameter diameter;
    private Area area;
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  Static Classes -------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A class to wrap up doubles as radii
     */
    public static class Radius {
        
        /**
         * A default constructor that creates a Radius of value '0'
         */
        public Radius() {
            this.radius = 0;
        }
        
        /**
         * A constructor that allows the user to specify the value of the Radius.
         * @param radius the double to be wrapped as a Radius
         */
        public Radius(double radius) {
            this.radius = radius;
        }
        
        
        /**
         * A method that returns the numerical value of the Radius
         * @return Returns the numerical value of the Radius as a double
         */
        public double value() {
            return radius;
        }
        
        
        /**
         * A method that returns a String representing the Area.
         * @return Returns the numerical value of the Radius as a String.
         */
        @Override
        public String toString() {
            return radius + "";
        }
        
        private double radius;
    }
    
    
    /**
     * A class to wrap up doubles as diameters
     */
    public static class Diameter {
        
        /**
         * A default constructor that creates a new Diameter of value '0'
         */
        public Diameter() {
            this.diameter = 0;
        }
        
        /**
         * A constructor that allows the user to specify the value of the Diameter
         * @param diameter the double to be wrapped as a Diameter
         */
        public Diameter(double diameter) {
            this.diameter = diameter;
        }
        
        
        /**
         * A method for obtaining the numerical value of the Diameter.
         * @return Returns the numerical value of the Diameter
         */
        public double value() {
            return diameter;
        }
        
        
        /**
         * A method for obtaining a String representing the Diameter.
         * @return Returns the numerical value of the Diameter as a String.
         */
        @Override
        public String toString() {
            return diameter + "";
        }
        
        private double diameter;
    }
    
}
