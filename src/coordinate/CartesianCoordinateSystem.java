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
import vector.Vector3D;

/**
 *
 * @author Nathan Templon
 */
class CartesianCoordinateSystem extends CoordinateSystem {
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Constructors  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------

    public CartesianCoordinateSystem() {
        this.reference = CoordinateSystem.getUniversalReference();
        this.shift = DEFAULT_SHIFT;
        this.scale = DEFAULT_SCALE;
        this.rotationMatrix = DEFAULT_ROTATION_MATRIX;
    }
    
    
    public CartesianCoordinateSystem(boolean reference) {
        if (reference) {
            this.reference = CoordinateSystem.getUniversalReference();
        }
        else {
            this.reference = null;
        }
        
        this.shift = DEFAULT_SHIFT;
        this.scale = DEFAULT_SCALE;
        this.rotationMatrix = DEFAULT_ROTATION_MATRIX;
    }
    

    @Override
    public Vector3D getShift() {
        return this.shift;
    }

    
    @Override
    public double getScale() {
        return this.scale;
    }

    
    @Override
    public Matrix getRotationMatrix() {
        return rotationMatrix;
    }
    
    
    @Override
    public Vector toSystem(Vector vector) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Internal Methods  --------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  State Variables  -----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    private CoordinateSystem reference;
    private Vector3D shift;
    private double scale;
    private Matrix rotationMatrix;
    
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  Static Variables and Methods  ----------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    public static Vector3D DEFAULT_SHIFT = new Vector3D(0.0, 0.0, 0.0);
    public static double DEFAULT_SCALE = 1.0;
    public static Matrix DEFAULT_ROTATION_MATRIX = getDefaultRotationMatrix();
    
    
    /**
     * 
     * @return Returns the default rotation matrix (no rotation at all)
     */
    private static Matrix getDefaultRotationMatrix() {
        double[][] values = {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        };
        return new Matrix(values);
    }

}
