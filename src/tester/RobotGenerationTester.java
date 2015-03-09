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

import com.jupiter.ganymede.math.function.FuncPoint;
import com.jupiter.ganymede.math.geometry.Angle;
import com.jupiter.ganymede.math.geometry.Angle.AngleType;
import tester.neuralnetclass.AngleSet;
import tester.neuralnetclass.RobotArm;

/**
 *
 * @author Nathan Templon
 */
public class RobotGenerationTester {
    
    public static void main(String[] args) {
        
    }
    
    private static void generatePoints() {
        
    }
    
    private static void test() {
        final RobotArm arm = new RobotArm();
        
        final FuncPoint point = arm.endPoint(new Angle(90, AngleType.DEGREES), new Angle(90, AngleType.DEGREES));
        System.out.println("Point: " + point);
        System.out.println();
        
        final AngleSet angles = arm.angles(point.x, point.y);
        
        System.out.println("Theta1: " + angles.theta1.getMeasure(AngleType.DEGREES));
        System.out.println("Theta2: " + angles.theta2.getMeasure(AngleType.DEGREES));
        System.out.println();
        
        System.out.println("Resultant: " + arm.endPoint(angles.theta1, angles.theta2));
    }
    
}
