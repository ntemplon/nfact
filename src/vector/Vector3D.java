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

package vector;

import geometry.angle.Angle;
import geometry.angle.Angle.TrigFunction;
import exception.InvalidOperationException;
import java.util.Arrays;

/**
 * A class to represent 3-Dimensional Vectors and perform 3-Dimensional Vector operations.
 * @author Nathan Templon
 */
public class Vector3D implements Vector {
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Constructors  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A default constructor that creates a Vector3D objects with all three components set to 0
     */
    public Vector3D() {
        this.components = new double[3];
        components[0] = 0;
        components[1] = 0;
        components[2] = 0;
    }
    
    
    /**
     * A constructor that allows the user to set all the components.
     * @param x the first component of the Vector
     * @param y the second component of the Vector
     * @param z the third component of the Vector
     */
    public Vector3D(double x, double y, double z) {
        this.components = new double[3];
        components[0] = x;
        components[1] = y;
        components[2] = z;
    }
    
    
    /**
     * A constructor that creates a Vector3D based on a Vector2D. The third component is set to zero.
     * @param other the Vector2D to use as the basis for a new Vector3D
     */
    public Vector3D(Vector2D other) {
        this.components = new double[3];
        components[0] = other.getComponent(1);
        components[1] = other.getComponent(2);
        components[2] = 0.0;
    }
    
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  Vector Operations  ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method to check this Vector for equivalence to another.
     * @param other an object to check for equality against this one
     * @return Returns true if the objects are equal, and false if they are not.
     */
    @Override
    public boolean equals(Object other) {
        
        if (other instanceof Vector3D) {
            Vector vec = (Vector)other;
            
            if (vec.dimension() != this.dimension()) {
                return false;
            }
            
            for (int index = 1; index <= 3; index++) {
                if (this.getComponent(index) != vec.getComponent(index)) return false;
            }
            
            return true;
        }
        
        else {
            return false;
        }
    }
    
    /**
     * A method to generate a number that represents the Vector.
     * @return Returns a number representing this Vector.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Arrays.hashCode(this.components);
        return hash;
    }

    
    /**
     * A method that multiplies this Vector by a scalar.
     * @param scalar the scalar factor to multiply the vector by.
     * @return Returns a new Vector3D object that is the scalar multiple of this vector.
     */
    @Override
    public Vector scalarMultiply(double scalar) {
        Vector vec = new Vector3D();
        
        for (int index = 1; index <= 3; index++) {
            vec.setComponent(index, this.getComponent(index) * scalar);
        }
        
        return vec;
    }

    
    /**
     * A method that adds another Vector to this one.
     * @param other the vector to be added to this one.
     * @return Returns a Vector3D object that is the sum of the two vectors.
     */
    @Override
    public Vector add(Vector other) {
        if (other.dimension() != this.dimension()) {
            throw new InvalidOperationException("Cannot add two vectors of different dimensionalities together.");
        }
        
        Vector vec = new Vector3D();
        
        for (int index = 1; index <= 3; index++) {
            vec.setComponent(index, this.getComponent(index) + other.getComponent(index));
        }
        
        return vec;
    }

    
    /**
     * A method that takes the dot product of this Vector and another.
     * @param other the vector to be dotted with this one.
     * @return Returns the dot product between the two vectors.
     */
    @Override
    public double dot(Vector other) {
        if (other.dimension() != this.dimension()) {
            throw new InvalidOperationException("Cannot dot two vectors of different dimensionalities together.");
        }
        
        double sum = 0;
        
        for (int index = 1; index <= 3; index++) {
            sum += this.getComponent(index) * other.getComponent(index);
        }
        
        return sum;
    }

    
    /**
     * A method that calculates the angle between two Vectors.
     * @param other the vector to be compared to this one for relative angle.
     * @return Returns an Angle object containing the relative angle between the two vectors.
     */
    @Override
    public Angle angleTo(Vector other) {
        if (other.dimension() != this.dimension()) {
            throw new InvalidOperationException("Cannot find the angle between vectors with different dimensionalities.");
        }
        
        return new Angle( this.dot(other) / (this.magnitude() * other.magnitude()), TrigFunction.COSINE);
    }
    
    
    /**
     * A method that calculates the cross product between this Vector and another.
     * @param other the vector to be crossed with this one, on the right hand side.
     * @return Returns a Vector object storing the result of the cross multiplication.
     */
    public Vector3D cross(Vector other) {
        if (other.dimension() != this.dimension()) {
            throw new InvalidOperationException("Cannot take the cross product of two vectors with different dimensionalities.");
        }
        
        double x = ((this.getComponent(2) * other.getComponent(3)) - (this.getComponent(3) * other.getComponent(2)));
        double y = ((this.getComponent(3) * other.getComponent(1)) - (this.getComponent(1) * other.getComponent(3)));
        double z = ((this.getComponent(1) * other.getComponent(2)) - (this.getComponent(2) * other.getComponent(1)));
        
        return new Vector3D(x, y, z);
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  Vector Properties  ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method that returns the dimensionality of the Vector object.
     * @return Returns the dimensionality of the vector, which is 3 in this case.
     */
    @Override
    public int dimension() {
        return 3;
    }

    
    /**
     * A method for finding the magnitude (norm) of the Vector
     * @return Returns the magnitude of the Vector
     */
    @Override
    public double magnitude() {
        double sum = 0.0;
        
        for (int index = 0; index < components.length; index++) {
            sum += components[index] * components[index];
        }
        
        return Math.sqrt(sum);
    }

    
    /**
     * A method for finding the direction of the Vector.
     * @return Returns a Vector object in the same direction as this one, but with a magnitude of 1.
     */
    @Override
    public Vector direction() {
        return this.scalarMultiply( 1.0 / this.magnitude() );
    }

    
    /**
     * A method for getting one of the components of the Vector.
     * @param index the index (starting at one) of the desired component
     * @return Returns the component at the specified index.
     */
    @Override
    public double getComponent(int index) {
        
        if (index <= 0 || index > 3) {
            throw new InvalidOperationException("Index does not exist for this vector.");
        }
        
        return components[index - 1];
    }

    
    /**
     * A method for setting one of the components of the Vector
     * @param index the index (starting at one) of the desired position of the new value
     * @param val the value to be placed in the specified location
     * @return Returns true if the assignment is successful, and false if the index was outside of the valid range.
     */
    @Override
    public boolean setComponent(int index, double val) {
        if (index > 0 && index <= 3) {
            components[index - 1] = val;
            return true;
        }
        return false;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  External Functions  ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method that returns a String representing the Vector.
     * @return Returns a String describing the vector.
     */
    @Override
    public String toString() {
        return "{" + components[0] + ", " + components[1] + ", " + components[2] + "}";
    }
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  State Variables  -----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    private double[] components;  
}
