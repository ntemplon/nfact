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

/**
 * A general interface for Vectors
 * @author Nathan Templon
 */
public interface Vector {
    
    /**
     * A method that multiplies this Vector by a scalar.
     * @param scalar the scalar to multiply this vector by
     * @return Returns a Vector object that is the result of multiplying this Vector by a given scalar.
     */
    public Vector scalarMultiply(double scalar);
    
    /**
     * A method that adds this Vector to another.
     * @param other the Vector to add to this one
     * @return Returns a Vector object that is the result of adding the given vector to this one.
     */
    public Vector add(Vector other);
    
    /**
     * A method that takes the dot product of this Vector and another.
     * @param other the Vector to take the dot product of this Vector with
     * @return Returns the result of the dot product operation between the two Vectors.
     */
    public double dot(Vector other);
    
    /**
     * A method that calculates the angle between two Vectors.
     * @param other the Vector to find the Angle to relative to this Vector
     * @return Returns an Angle object representing the angle between the two Vectors.
     */
    public Angle angleTo(Vector other);
    
    
    /**
     * A method that returns the dimensionality of the Vector object.
     * @return Returns the dimensionality of the Vector.
     */
    public int dimension();
    
    /**
     * A method for finding the magnitude (norm) of the Vector
     * @return Returns the magnitude of the Vector
     */
    public double magnitude();
    
    /**
     * A method for finding the direction of the Vector.
     * @return Returns a Vector object in the same direction as this one, but with a magnitude of 1.
     */
    public Vector direction();
    
    
    /**
     * A method for getting one of the components of the Vector.
     * @param index the index (starting at one) of the desired component
     * @return Returns the component at the specified index.
     */
    public double getComponent(int index);
    
    /**
     * A method for setting one of the components of the Vector
     * @param index the index (starting at one) of the desired position of the new value
     * @param val the value to be placed in the specified location
     * @return Returns true if the assignment is successful, and false if the index was outside of the valid range.
     */
    public boolean setComponent(int index, double val);
    
}
