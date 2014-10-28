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
