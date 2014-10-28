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

package geometry.angle;

import static Util.Util.doubleEquals;

/**
 * A class to represent angles and perform Angle operations
 * @author Nathan Templon
 */
public class Angle {
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Constructors  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A default constructor that creates a new Angle of measure 0.
     */
    public Angle() {
        this.measure = 0.0;
    }
    
    
    /**
     * A constructor that allows the user to specify the angle's measure in radians
     * @param value the measure of the angle to be created, in radians
     */
    public Angle(double value) {
        this.measure = value;
        
        correctValueRange();
    }
    
    
    /**
     * A constructor that allows the user to specify the angle's measure and the system in which it was measured.
     * @param value the measure of the angle to be created, in the measurement system specified
     * @param type the angle measurement system to be used, either AngleType.RADIANS or AngleType.DEGREES
     */
    public Angle(double value, AngleType type) {
        switch(type) {
            case RADIANS:
                this.measure = value;
                break;
            
            case DEGREES:
                this.measure = value * (Math.PI / 180.0);
                break;
        }
        
        correctValueRange();
    }
    
    
    /**
     * A constructor that allows the user to specify an angle by the value of one of its trigonometric functions
     * @param funcVal the value of the specified trigonometric function for the angle
     * @param function the trigonometric function whose value is given (from the Angle.TrigFunction enum)
     */
    public Angle(double funcVal, TrigFunction function) {
        // Correct for double precision errors
        if (doubleEquals(funcVal, 1.0)) {
            funcVal = 1.0;
        }
        else if (doubleEquals(funcVal, -1.0)) {
            funcVal = -1.0;
        }
        else if (doubleEquals(funcVal, 0.0)) {
            funcVal = 0.0;
        }
        
        switch(function) {
            
            case COSECANT:
                funcVal = 1.0 / funcVal;
            case SINE:
                this.measure = Math.asin(funcVal);
                break;
                
            case SECANT:
                funcVal = 1.0 / funcVal;
            case COSINE:
                this.measure = Math.acos(funcVal);
                break;
            
            case COTANGENT:
                if (funcVal == 0) {
                    this.measure = Math.PI;
                    break;
                }
                funcVal = 1.0 / funcVal;
            case TANGENT:
                this.measure = Math.atan(funcVal);
                break;
            
        }
        
        correctValueRange();
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  Angle Operations  ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method that facilitates the addition of two Angle objects.
     * @param other the angle to be added to this one.
     * @return Returns a new Angle object, equal in size to the sum of the two angles.
     */
    public Angle add(Angle other) {
        return new Angle(this.measure() + other.measure());
    }
    
    
    /**
     * A method that multiplies the Angle object by a scalar multiplier
     * @param scalar the scalar to multiply the angle by
     * @return Returns an angle that is the result of multiplying this angle by a given scalar.
     */
    public Angle scalarMultiply(double scalar) {
        return new Angle(this.measure() * scalar);
    }
    
    
    /**
     * A method that tests this angle for equality against another Angle object.
     * @param other the Angle to be compared to this one for equality.
     * @return Returns a boolean indicating if the two angles are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Angle)) {
            return false;
        }
        return doubleEquals(((Angle)other).measure(), this.measure());
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  Getters / Setters  ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method that returns the measure of the angle in radians.
     * @return Returns the angle measure in radians.
     */
    public double measure(){
        return measure;
    }
    
    
    /**
     * A method that returns the measure of the angle in a specified measurement system.
     * @param type the AngleType (RADIANS or DEGREES) that the user wants the angle reported in
     * @return Returns the angle measure in radians or degrees, as specified.
     */
    public double measure(AngleType type){
        
        switch (type) {
            
            case RADIANS:
                return measure;
                
            case DEGREES:
                return measure * ( 180.0 / Math.PI );
            
            default:
                return measure;
                
        }
        
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  Angle Properties  ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method to get the sine of the angle.
     * @return Returns the sine of the angle.
     */
    public double sin(){
        return Math.sin(measure);
    }
    
    
    /**
     * A method to get the cosine of the angle.
     * @return Returns the cosine of the angle.
     */
    public double cos(){
        return Math.cos(measure);
    }
    
    
    /**
     * A method to get the tangent of the angle.
     * @return Returns the tangent of the angle.
     */
    public double tan(){
        return Math.tan(measure);
    }
    
    
    /**
     * A method to get the secant of the angle.
     * @return Returns the secant of the angle.
     */
    public double sec(){
        return 1.0 / Math.cos(measure);
    }
    
    
    /**
     * A method to get the cosecant of the angle.
     * @return Returns the cosecant of the angle.
     */
    public double csc(){
        return 1.0 / Math.sin(measure);
    }
    
    
    /**
     * A method to get the cotangent of the angle.
     * @return Returns the cotangent of the angle.
     */
    public double cot(){
        return Math.cos(measure) / Math.sin(measure);
    }
    
    
    /**
     * A method to get the hyperbolic sine of the angle.
     * @return Returns the hyperbolic sine of the angle.
     */
    public double sinh(){
        return Math.sinh(measure);
    }
    
    
    /**
     * A method to get the hyperbolic cosine of the angle.
     * @return Returns the hyperbolic cosine of the angle.
     */
    public double cosh(){
        return Math.cosh(measure);
    }
    
    
    /**
     * A method to get the hyperbolic tangent of the angle.
     * @return Returns the hyperbolic tangent of the angle.
     */
    public double tanh(){
        return Math.tanh(measure);
    }
    
    
    /**
     * A method to get the hyperbolic secant of the angle.
     * @return Returns the hyperbolic secant of the angle.
     */
    public double sech(){
        return 1.0 / Math.cosh(measure);
    }
    
    
    /**
     * A method to get the hyperbolic cosecant of the angle.
     * @return Returns the hyperbolic cosecant of the angle.
     */
    public double csch(){
       return 1.0 / Math.sinh(measure); 
    }
    
    
    /**
     * A method to get the hyperbolic cotangent of the angle.
     * @return Returns the hyperbolic cotangent of the angle.
     */
    public double coth(){
        return Math.cosh(measure) / Math.sinh(measure);
    }
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  State Variables  -----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    private double measure;
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  Internal Fucntions  --------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * Changes the angles measure to be between 0 and 2 PI, inclusive.
     */
    private void correctValueRange() {
        while(measure < 0.0) {
            measure += (2 * Math.PI);
        }
        while(measure > (2.0 * Math.PI)) {
            measure -= (2.0 * Math.PI);
        }
        
        // Correct 2 PI to 0
        if (doubleEquals(measure, 2.0 * Math.PI)) {
            measure = 0.0;
        }
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  External Fucntions  --------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method to return the value of the angle in radians as a String
     * @return Returns a String of the magnitude of the angle in radians.
     */
    @Override
    public String toString() {
        return measure + "";
    }
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  Enumerations  --------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * An enumeration specifying the different angle measurement systems supported.
     */
    public enum AngleType {
        RADIANS,
        DEGREES;
    }
    
    
    /**
     * An enumeration specifying the different trigonometric functions supported.
     */
    public enum TrigFunction {
        SINE,
        COSINE,
        TANGENT,
        SECANT,
        COSECANT,
        COTANGENT;
    }
}
