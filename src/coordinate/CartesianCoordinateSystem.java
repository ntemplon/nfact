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

package coordinate;

import geometry.angle.Angle;
import java.util.ArrayList;
import matrix.Matrix;
import vector.Vector;
import vector.Vector2D;
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
