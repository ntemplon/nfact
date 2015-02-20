/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
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
package tester;

import com.jupiter.ganymede.math.geometry.Angle;
import com.jupiter.ganymede.math.geometry.Angle.AngleType;
import com.jupiter.ganymede.math.vector.Vector;

/**
 *
 * @author Nathan Templon
 */
public class ServoHornTester {

    public static void main(String[] args) {
        Vector p = new Vector(0, 0);
        Angle gammaOff = new Angle(0.0, AngleType.DEGREES);
        double rs = 0.0;
        double rh = 0.0;
        Angle gammaControlMin = new Angle(0.0, AngleType.DEGREES);
        Angle gammaControlMax = new Angle(0.0, AngleType.DEGREES);

        double lmax = getLength(p, rs, rh, new Angle(180.0, AngleType.DEGREES), gammaControlMax.plus(gammaOff));
        double lmin = getLength(p, rs, rh, new Angle(0.0, AngleType.DEGREES), gammaControlMin.plus(gammaOff));
        
        System.out.println(lmax + ", " + lmin);
    }
    
    
    // Private Methods
    private static double getLength(Vector p, double rs, double rh, Angle theta, Angle phi) {
        Vector rhVec = new Vector(rh * phi.cos(), rh * phi.sin());
        Vector rsVec = new Vector(rs * theta.cos(), rs * theta.sin());
        Vector lVec = p.plus(rhVec).minus(rsVec);
        return lVec.norm();
    }

}
