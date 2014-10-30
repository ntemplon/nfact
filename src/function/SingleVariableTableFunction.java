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
                        }
                        else if (point.x < minX) {
                            minX = point.x;
                        }
                        else if (point.x > maxX) {
                            maxX = point.x;
                        }

                        readPoints.add(point);
                    }
                    line = reader.readLine();
                }

                return new SingleVariableTableFunction(minX, maxX, readPoints);
            }
            catch (IOException ex) {

            }
            finally {
                if (fr != null) {
                    fr.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        }
        catch (Exception ex) {

        }
        // If something goes totally wrong, return a null value
        return null;
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
        }
        // If there is one point, that is the value
        else if (this.points.size() == 1) {
            return this.points.get(0).y;
        }
        
        if (!(value < this.domainMax)) {
                FuncPoint penultPoint = this.points.get(this.points.size() - 2);
                FuncPoint finalPoint = this.points.get(this.points.size() - 1);
                double finalSlope = (finalPoint.y - penultPoint.y) / (finalPoint.x - penultPoint.x);
                double change = finalSlope * (value - finalPoint.x);
                return (finalPoint.y + change);
        }
        else if (!(value > this.domainMin)) {
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
        Collections.sort(this.points, new Comparator<FuncPoint>() {
            @Override
            public int compare(FuncPoint p1, FuncPoint p2) {
                return Double.compare(p1.x(), p2.x());
            }
        });
    }

    public final void addPoint(FuncPoint point) {
        points.add(point);
        Collections.sort(this.points, new Comparator<FuncPoint>() {
            @Override
            public int compare(FuncPoint p1, FuncPoint p2) {
                return Double.compare(p1.x(), p2.x());
            }
        });
    }

    public final void addPoints(Collection<FuncPoint> points) {
        this.points.addAll(points);
        Collections.sort(this.points, new Comparator<FuncPoint>() {
            @Override
            public int compare(FuncPoint p1, FuncPoint p2) {
                return Double.compare(p1.x(), p2.x());
            }
        });
    }

    public final int indexOfPointSmallerThan(double val) {
        int upperBound = points.size() - 1;
        int lowerBound = 0;
        int guess = (upperBound + lowerBound) / 2;
        
        double startGuess = guess;
        boolean guessRepeated = false;

        while (!((points.get(guess).x < val) && (points.get(guess + 1).x > val)) && !guessRepeated) {
            startGuess = guess;
            if (points.get(guess + 1).x < val) {
                lowerBound = guess;
                guess = (upperBound + lowerBound) / 2;
            }
            else if (points.get(guess).x > val) {
                upperBound = guess;
                guess = (upperBound + lowerBound) / 2;
            }
            guessRepeated = (startGuess == guess);
        }

        return guess;
    }

}
