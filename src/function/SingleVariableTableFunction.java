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
package function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Nathan Templon
 */
public class SingleVariableTableFunction extends SingleVariableFunction {

    // Static Methods
    public static SingleVariableTableFunction fromCsvFile(File file) {

        try {
            FileReader fr = null;
            BufferedReader reader = null;
            double minX = 0.0;
            double maxX = 0.0;
            boolean minMaxSet = false;

            try {
                fr = new FileReader(file);
                reader = new BufferedReader(fr);

                ArrayList<FuncPoint> readPoints = new ArrayList<>();
                String line = reader.readLine();
                while (line != null) {
                    FuncPoint point = FuncPoint.fromString(line);
                    if (point != null) {
                        if (!minMaxSet) {
                            minX = point.x;
                            maxX = point.x;
                            minMaxSet = true;
                        } else if (point.x < minX) {
                            minX = point.x;
                        } else if (point.x > maxX) {
                            maxX = point.x;
                        }

                        readPoints.add(point);
                    }
                    line = reader.readLine();
                }

                return new SingleVariableTableFunction(minX, maxX, readPoints);
            } catch (IOException ex) {

            } finally {
                if (fr != null) {
                    fr.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Exception ex) {

        }
        // If something goes totally wrong, return a null value
        return null;
    }
    
    private static int comparePoints(FuncPoint p1, FuncPoint p2) {
        return Double.compare(p1.x, p2.x);
    }

    // Private Members
    private final List<FuncPoint> points;

    // Initialization
    public SingleVariableTableFunction() {
        this.points = new ArrayList<>();
    }

    public SingleVariableTableFunction(FuncPoint[] points) {
        this.points = new ArrayList<>();
        this.addPoints(points);
    }

    public SingleVariableTableFunction(double domainMin, double domainMax, FuncPoint[] points) {
        super(domainMin, domainMax);
        this.points = new ArrayList<>();
        this.addPoints(points);
    }

    public SingleVariableTableFunction(List<FuncPoint> points) {
        this.points = new ArrayList<>();
        this.addPoints(points);
    }

    public SingleVariableTableFunction(double domainMin, double domainMax, List<FuncPoint> points) {
        super(domainMin, domainMax);
        this.points = new ArrayList<>();
        this.addPoints(points);
    }

    // Public Members
    @Override
    public Double evaluate(Double value) {

        // If there are no points, return 0
        if (this.points.isEmpty()) {
            return 0.0;
        } // If there is one point, that is the value
        else if (this.points.size() == 1) {
            return this.points.get(0).y;
        }

        if (!(value < this.domainMax)) {
            FuncPoint penultPoint = this.points.get(this.points.size() - 2);
            FuncPoint finalPoint = this.points.get(this.points.size() - 1);
            double finalSlope = (finalPoint.y - penultPoint.y) / (finalPoint.x - penultPoint.x);
            double change = finalSlope * (value - finalPoint.x);
            return (finalPoint.y + change);
        } else if (!(value > this.domainMin)) {
            FuncPoint firstPoint = this.points.get(0);
            FuncPoint secondPoint = this.points.get(1);
            double initialSlope = (secondPoint.y - firstPoint.y) / (secondPoint.x - firstPoint.x);
            double change = initialSlope * (value - firstPoint.x);
            return (firstPoint.y + change);
        }

        int lowerIndex = indexOfPointSmallerThan(value);

        FuncPoint leftPoint = points.get(lowerIndex);
        FuncPoint rightPoint = points.get(lowerIndex + 1);

        double interpolationR = (value - leftPoint.x) / (rightPoint.x - leftPoint.x);
        double change = (rightPoint.y - leftPoint.y);

        return interpolationR * change + leftPoint.y;
    }

    public final void addPoints(FuncPoint[] points) {
        this.points.addAll(Arrays.asList(points));
        Collections.sort(this.points, (FuncPoint p1, FuncPoint p2) -> comparePoints(p1, p2));
    }

    public final void addPoint(FuncPoint point) {
        points.add(point);
        Collections.sort(this.points, (FuncPoint p1, FuncPoint p2) -> comparePoints(p1, p2));
    }

    public final void addPoints(Collection<FuncPoint> points) {
        this.points.addAll(points);
        Collections.sort(this.points, (FuncPoint p1, FuncPoint p2) -> comparePoints(p1, p2));
    }

    public final int indexOfPointSmallerThan(double val) {
        int upperBound = points.size() - 1;
        int lowerBound = 0;
        int guess = (upperBound + lowerBound) / 2;

        double startGuess;
        boolean guessRepeated = false;

        while (!((points.get(guess).x < val) && (points.get(guess + 1).x > val)) && !guessRepeated) {
            startGuess = guess;
            if (points.get(guess + 1).x < val) {
                lowerBound = guess;
                guess = (upperBound + lowerBound) / 2;
            } else if (points.get(guess).x > val) {
                upperBound = guess;
                guess = (upperBound + lowerBound) / 2;
            }
            guessRepeated = (startGuess == guess);
        }

        return guess;
    }

}
