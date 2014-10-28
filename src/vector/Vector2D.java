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

package vector;

import geometry.angle.Angle;
import geometry.angle.Angle.TrigFunction;
import exception.InvalidOperationException;

/**
 * A class to represent 2-Dimensional Vectors and 2-Dimensional Vector operations
 * @author Nathan Templon
 */
public class Vector2D implements Vector {
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Constructors  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A default constructor that creates a new Vector2D object with both components set to 0.
     */
    public Vector2D() {
        x = 0;
        y = 0;
    }
    
    
    /**
     * A constructor that allows the user to set the values of both components.
     * @param x the value to be placed in the 1st component
     * @param y the value to be placed in the 2nd component
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  Vector Operations  ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method to check another object for equality to this one.
     * @param other another vector to be compared to this one.
     * @return Returns a boolean indicating if the two vectors are equal.
     */
    @Override
    public boolean equals(Object other) {
        
        if (other instanceof Vector2D) {
            
            Vector vec = (Vector)other;
            return (vec.getComponent(1) == x) && (vec.getComponent(2) == y);
            
        }
        
        else {
            return false;
        }
    }

    
    /**
     * A method to generate a number to represent the object.
     * @return Returns the object's Hash Code.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }
    
    
    /**
     * A method that multiplies this Vector by a scalar. 
     * @param scalar the scalar by which the vector is to be multiplied
     * @return Returns a new Vector2D object that has been multiplied by the scalar.
     */
    @Override
    public Vector scalarMultiply(double scalar) {
        return new Vector2D(x * scalar, y * scalar);
    }
    
    
    /**
     * A method that adds another Vector to this one.
     * @param other the vector to be added to this one
     * @return Returns a new Vector2D object that is the result of the addition
     * @throws InvalidOperationException if the two vectors are not the same size.
     */
    @Override
    public Vector add(Vector other) {
        if (other.dimension() != 2) {
            throw new InvalidOperationException("Cannot add two vectors of different dimensionalities together.");
        }
        double newX = x + other.getComponent(1);
        double newY = y + other.getComponent(2);
        return new Vector2D(newX, newY);
    }
    
    
    /**
     * A method that takes the dot product of this Vector and another.
     * @param other the vector to be dotted with this one
     * @return Returns the dot product of the two vectors.
     * @throws InvalidOperationException if the two vectors are not the same size.
     */
    @Override
    public double dot(Vector other) {
        if (other.dimension() != 2) {
            throw new InvalidOperationException("Cannot take the dot product of two vectors of different dimensionalities.");
        }
        
        return (this.x * other.getComponent(1)) + (this.y * other.getComponent(2));
    }
    
    
    /**
     * A method that calculates the angle between two Vectors
     * @param other the vector to be compared with to find the angle between.
     * @return Returns the angle between this vector and a given vector.
     * @throws InvalidOperationException if the two vectors are not the same size.
     */
    @Override
    public Angle angleTo(Vector other) {
        if (other.dimension() != 2) {
            throw new InvalidOperationException("Cannot take the dot product of two vectors of different dimensionalities.");
        }
        double magProduct = this.magnitude() * other.magnitude();
        double dotProduct = this.dot(other);
        double cosAngle = dotProduct / magProduct;
        
        return new Angle(cosAngle, TrigFunction.COSINE);
    }
    
    
    /**
     * A method that takes the cross product of this Vector with a Vector2D.
     * @param other the 2D vector to be crossed with this one.
     * @return Returns a Vector3D object that is the cross product between the two vectors.
     */
    public Vector3D cross(Vector2D other) {
        double z = (this.getComponent(1) * other.getComponent(2)) - (this.getComponent(2) * other.getComponent(1));
        return new Vector3D(0, 0, z);
    }
    
    
    /**
     * A method that takes the cross product of this Vector with a Vector3D.
     * @param other the 3D vector to be crossed with this one.
     * @return Returns a Vector3D object that is the cross product between the two vectors.
     */
    public Vector3D cross(Vector3D other) {
        return (new Vector3D(this).cross(other));
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  Vector Properties  ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method that returns the dimensionality of the Vector
     * @return Returns the dimension of the Vector (2).
     */
    @Override
    public int dimension() {
        return 2;
    }
    
    
    /**
     * A method for finding the magnitude (norm) of the Vector
     * @return Returns the magnitude of the Vector
     */
    @Override
    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }
    
    
    /**
     * A method for finding the direction of the Vector.
     * @return Returns a Vector object in the same direction as this one, but with a magnitude of 1.
     */
    @Override
    public Vector direction() {
        return this.scalarMultiply(1.0 / this.magnitude());
    }
    
    
    /**
     * A method for getting one of the components of the Vector.
     * @param index the index (starting at one) of the desired component
     * @return Returns the component at the specified index.
     */
    @Override
    public double getComponent(int index) {
        
        switch(index) {
            case 1:
                return x;
              
            case 2:
                return y;
        }
        
        throw new InvalidOperationException("Index does not exist for this vector.");
    }
    
    
    /**
     * A method for setting one of the components of the Vector
     * @param index the index (starting at one) of the desired position of the new value
     * @param val the value to be placed in the specified location
     * @return Returns true if the assignment is successful, and false if the index was outside of the valid range.
     */
    @Override
    public boolean setComponent(int index, double val) {
        
        switch(index) {
            case 1:
                x = val;
                return true;
                
            case 2:
                y = val;
                return true;
                
            default:
                return false;
                
        }
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  External Functions  ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method that returns a String representing the Vector.
     * @return Returns the vector in a String form.
     */
    @Override
    public String toString() {
        return "{" + x + ", " + y + "}";
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  State Variables  -----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    private double x, y;
}
