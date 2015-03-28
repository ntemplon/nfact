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
package tester.neuralnetclass;

import com.jupiter.ganymede.math.function.FuncPoint;
import com.jupiter.ganymede.math.geometry.Angle;
import com.jupiter.ganymede.math.geometry.Angle.AngleType;
import com.jupiter.ganymede.math.geometry.Angle.TrigFunction;
import com.jupiter.ganymede.math.vector.Vector3;

/**
 *
 * @author Nathan Templon
 */
public class RobotArm {
    
    // Fields
    private final double height = 1.0;
    private final double length1 = 1.5;
    private final double length2 = 1.9;
    
    
    // Initialization
    public RobotArm() {
        
    }
    
    // Public Methods
    public FuncPoint endPoint(Angle theta1, Angle theta2) {
        final double x = length1 * theta1.minus(new Angle(90, AngleType.DEGREES)).cos()
                + length2 * theta2.plus(theta1).minus(new Angle(180, AngleType.DEGREES)).sin();
        
        final double y = height + length1 * theta1.minus(new Angle(90, AngleType.DEGREES)).sin()
                - length2 * theta2.plus(theta1).minus(new Angle(180, AngleType.DEGREES)).cos();
        
        return new FuncPoint(x, y);
    }
    
    public AngleSet angles(double x, double y) {
        final double r0 = this.length1;
        final double r1 = this.length2;
        
        final FuncPoint p0 = new FuncPoint(0, height);
        final FuncPoint p1 = new FuncPoint(x, y);
        
        final double xDiff = p0.x - p1.x;
        final double yDiff = p0.y - p1.y;
        final double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        
        if (distance > r0 + r1) {
            // Too far apart
            return new AngleSet(null, null);
        }
        if (distance < Math.abs(r0 - r1)) {
            // One circle contained within the other
            return new AngleSet(null, null);
        }
        if (distance == 0.0 && r0 == r1) {
            // Same Circel
            return new AngleSet(null, null);
        }
        
        final double a = (r0 * r0 - r1 * r1 + distance * distance) / (2 * distance); // Distance to line containing intersection points
        final double h = Math.sqrt(r0 * r0 - a * a); // Distance from line of centers to intersection points
        
        final Angle alpha = new Angle(yDiff / xDiff, TrigFunction.TANGENT); // Angle of line of centers above horizon
        
        final FuncPoint p2 = new FuncPoint(
                p0.x + a * alpha.cos(),
                p0.y + a * alpha.sin()
        );
        
        final Angle beta = alpha.plus(new Angle(90, AngleType.DEGREES));
        
        AngleSet angles = this.getAngles(p0, p1, p2, h, beta);
        final double theta2Measure = angles.theta2.getMeasure(AngleType.DEGREES);
        if (theta2Measure > 180.0) {
//            System.out.println("Using other Angle");
            return new AngleSet(
                    angles.theta1,
                    angles.theta2.minus(new Angle(180.0, AngleType.DEGREES))
            );
        }
        return angles;
    }
    
    
    // Private Methods
    private AngleSet getAngles(FuncPoint p0, FuncPoint p1, FuncPoint p2, double h, Angle beta) {
        final FuncPoint inter = new FuncPoint(
                p2.x + h * beta.cos(),
                p2.y + h * beta.sin()
        );
        
        final double arm1x = inter.x - p0.x;
        final double arm1y = inter.y - p0.y;
//        System.out.println("Arm 1: " + arm1x + ", " + arm1y);
        final double horizonTangent = arm1y / arm1x;
        Angle arm1Horizon = new Angle(Math.atan(horizonTangent));
        if (arm1x < 0) {
            arm1Horizon = arm1Horizon.minus(new Angle(Math.PI));
        }
        
        Angle theta1 = arm1Horizon.plus(new Angle(90, AngleType.DEGREES));
//        if (theta1.getMeasure(AngleType.DEGREES) < 0) {
//            System.out.println("Shifting Theta");
////            theta1 = theta1.times(-1.0);
//            System.out.println(theta1.getMeasure());
//            theta1 = (new Angle(360, AngleType.DEGREES)).plus(theta1);
//            System.out.println(theta1.getMeasure());
//        }
//        System.out.println("Arm 1 to Horizon: " + arm1Horizon.getMeasure(AngleType.DEGREES));
//        System.out.println("Tangent: " + (arm1y / arm1x));
//        boolean thetaAdjustment = false;
//        if (theta1.getMeasure(AngleType.DEGREES) < 30) {
//            theta1 = (new Angle(Math.PI)).minus(theta1);
//            thetaAdjustment = true;
//        }
        
        final double arm2x = p1.x - inter.x;
        final double arm2y = p1.y - inter.y;
        Angle gamma = new Angle(arm2y / arm2x, Angle.TrigFunction.TANGENT);
//        if (p1.x < 0) {
//            gamma = theta1;
//        }
        Angle alpha = theta1.plus(new Angle(-90, AngleType.DEGREES));
        
//        System.out.println("P0: " + p0 + "\nP1: " + p1 + "\nP2: " + p2);
//        System.out.println("Gamma: " + gamma.getMeasure(AngleType.DEGREES));
//        System.out.println("Alpha: " + alpha.getMeasure(AngleType.DEGREES));
        Angle theta2 = new Angle(180, AngleType.DEGREES).plus(gamma).minus(alpha);
        
        return new AngleSet(theta1, theta2);
    }
    
}
