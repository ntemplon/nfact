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

package coordinate;

import matrix.Matrix;
import vector.Vector;

/**
 *
 * @author Nathan Templon
 */
public abstract class CoordinateSystem {
    
    public abstract Vector getShift();
    public abstract double getScale();
    public abstract Matrix getRotationMatrix();
    
    public abstract Vector toSystem(Vector vec);
    
    
    /**
     * 
     * @return Returns the n
     */
    public static CoordinateSystem getUniversalReference() {
        return new CartesianCoordinateSystem(false);
    }
    
    
    /**
     * 
     * @param vec the vector to be changed to the universal reference coordinate system.
     * @return Returns the vector in terms of the universal reference coordinate system.
     */
    public static Vector toUniversalReference(Vector vec) {
        return (new CartesianCoordinateSystem(false)).toSystem(vec);
    }
    
}
