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
import com.jupiter.ganymede.math.geometry.Angle.TrigFunction;
import com.jupiter.ganymede.math.regression.Regressor.Point;
import com.jupiter.ganymede.math.vector.Vector;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Nathan Templon
 */
public class ServoHornTester {

    public static void main(String[] args) {
        final double hs = 1.0;
        final double ls = 1.0;
        final double xf = 1.0;
        final double xh = 1.0;
        final double hf = 1.0;
        final Angle theta = new Angle(0, AngleType.DEGREES);
        
        final double xDiff = xh + xf;
        final double yDiff = ls + hs - hf;
        final double l = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        
        final List<Point<Double, Double>> inters = Arrays.asList(getIntersections(ls, 0, hs, l, -xf - xh * theta.cos() - hf * theta.sin(), -xh * theta.sin() + hf * theta.cos()));
        final List<Angle> angles = inters.stream()
                .map((Point<Double, Double> point) -> getServoAngle(hs, point))
                .collect(Collectors.toList());
        
        System.out.println("Length: " + l);
        angles.stream()
                .filter((Angle angle) -> angle.getMeasure() < Math.PI)
                .forEach((Angle angle) -> System.out.println("Phi: " + angle.getMeasure(AngleType.DEGREES)));
    }


    // Private Methods
    private static Point<Double, Double>[] getIntersections(double r0, double x0, double y0, double r1, double x1, double y1) {
        final double xDiff = x0 - x1;
        final double yDiff = y0 - y1;
        final double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

        if (distance > r0 + r1) {
            // Too far apart
            return new Point[0];
        }
        if (distance < Math.abs(r0 - r1)) {
            // One circle contained within the other
            return new Point[0];
        }
        if (distance == 0.0 && r0 == r1) {
            // Same Circel
            return new Point[0];
        }

        final double a = (r0 * r0 - r1 * r1 + distance * distance) / (2 * distance);
        double h = Math.sqrt(r0 * r0 - a * a);

        final Angle alpha = new Angle(yDiff / xDiff, TrigFunction.TANGENT);

        final Point<Double, Double> p2 = new Point<>(
                x0 + a * alpha.cos(),
                y0 + a * alpha.sin()
        );

        final Angle beta = alpha.plus(new Angle(Math.PI));

        final Point<Double, Double> inter = new Point<>(
                p2.first + h * beta.cos(),
                p2.second + h * beta.sin()
        );
        final Point<Double, Double> inter2 = new Point<>(
                p2.first - h * beta.cos(),
                p2.second - h * beta.sin()
        );

        return new Point[]{inter, inter2};
    }

    private static Angle getServoAngle(double hs, Point<Double, Double> target) {
        final double yDiff = target.second - hs;
        return new Angle(Math.abs(target.first / yDiff), TrigFunction.TANGENT);
    }
    
    private static void oldCalcs() {
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

    private static double getLength(Vector p, double rs, double rh, Angle theta, Angle phi) {
        Vector rhVec = new Vector(rh * phi.cos(), rh * phi.sin());
        Vector rsVec = new Vector(rs * theta.cos(), rs * theta.sin());
        Vector lVec = p.plus(rhVec).minus(rsVec);
        return lVec.norm();
    }

}
