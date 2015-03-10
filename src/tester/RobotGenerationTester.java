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
import com.jupiter.ganymede.math.geometry.Angle.MeasureRange;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import tester.neuralnetclass.AngleSet;
import tester.neuralnetclass.RobotArm;

/**
 *
 * @author Nathan Templon
 */
public class RobotGenerationTester {

    private static final double boxWidth = 1;
    private static final double boxHeight = 1;
    private static final double boxX = 1;
    private static final RobotArm arm = new RobotArm();

    public static void main(String[] args) {
//        test();
//        generateTrainingPoints();
//        runPostProcessor();
        generatePoints();
    }

    private static void generatePoints() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            writeTestPoints(fileChooser.getSelectedFile().toPath());
        }
    }

    private static void generateTrainingPoints() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            writeTrainingPoints(fileChooser.getSelectedFile().toPath());
        }
    }

    private static void writeTestPoints(Path outputFile) {
        try {
            final Path directory = outputFile.getParent();
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            final List<String> output = getTestingPoints().stream()
                    .map((FuncPoint point) -> {
                        AngleSet angles = arm.angles(point.x, point.y);
                        return point.x + "\t" + point.y + "\t"
                                + angles.theta1.getMeasure(AngleType.DEGREES) + "\t"
                                + angles.theta2.getMeasure(AngleType.DEGREES);
                    })
                    .collect(Collectors.toList());

            Files.write(outputFile, output);
        }
        catch (IOException ex) {

        }
    }

    private static void writeTrainingPoints(Path outputFile) {
        try {
            final Path directory = outputFile.getParent();
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            final List<String> output = getTrainingPoints().stream()
                    .map((FuncPoint point) -> {
                        AngleSet angles = arm.angles(point.x, point.y);
                        return point.x + "\t" + point.y + "\t"
                        + angles.theta1.getMeasure(AngleType.DEGREES, MeasureRange.FullCircle) + "\t"
                        + angles.theta2.getMeasure(AngleType.DEGREES, MeasureRange.FullCircle);
                    })
                    .collect(Collectors.toList());

            Files.write(outputFile, output);
        }
        catch (IOException ex) {

        }

        getTrainingPoints().stream()
                .forEach((FuncPoint point) -> {
                    final AngleSet undo = arm.angles(point.x, point.y);
                    final FuncPoint redo = arm.endPoint(undo.theta1, undo.theta2);
                    final double deltaX = redo.x - point.x;
                    final double deltaY = redo.y - point.y;
                    final double error = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    if (error > 0.1) {
                        System.out.println("Point: " + point);
                        System.out.println("Angles: " + undo.theta1.getMeasure(AngleType.DEGREES) + ", " + undo.theta2.getMeasure(AngleType.DEGREES));
                        System.out.println("Result: " + redo);
                        System.out.println();
                    }
                });
//        System.out.println(maxError + ", " + getTrainingPoints().size());
    }

    private static Set<FuncPoint> getTrainingPoints() {
        final Set<FuncPoint> points = new HashSet<>();

        final double trainWidth = 1.2;
        final double trainHeight = 1.2;
        final int pointsPerSide = 5;

        // Even distribution throughout box
        final double leftX = boxX + (boxWidth - trainWidth) / 2.0;
        final double bottomY = (boxHeight - trainHeight) / 2.0;
        final double stepX = trainWidth / (pointsPerSide - 1);
        final double stepY = trainHeight / (pointsPerSide - 1);

        for (int i = 0; i < pointsPerSide; i++) {
            for (int j = 0; j < pointsPerSide; j++) {
                FuncPoint target = new FuncPoint(
                        leftX + i * stepX,
                        bottomY + j * stepY
                );
                points.add(target);
            }
        }

        return points;
    }

    private static Set<FuncPoint> getTestingPoints() {
        final Set<FuncPoint> points = new HashSet<>();

        // 11 points along y = x - 1
        for (int t = 0; t <= 10; t++) {
            points.add(new FuncPoint(
                    t / 10.0 + 1,
                    t / 10.0
            ));
        }

        // 11 points along y = 2 - x
        for (int t = 0; t <= 10; t++) {
            points.add(new FuncPoint(
                    t / 10.0 + 1,
                    1 - t / 10.0
            ));
        }

        // Circle of Radius 0.25
        for (int theta = 0; theta < 360; theta += 5) {
            points.add(new FuncPoint(
                    1.5 + 0.25 * Math.cos(Math.toRadians(theta)),
                    0.5 + 0.25 * Math.sin(Math.toRadians(theta))
            ));
        }

        // Circle of Radius 0.55
        for (int theta = 0; theta < 360; theta += 5) {
            points.add(new FuncPoint(
                    1.5 + 0.55 * Math.cos(Math.toRadians(theta)),
                    0.5 + 0.55 * Math.sin(Math.toRadians(theta))
            ));
        }

        return points;
    }

    private static void runPostProcessor() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            postProcess(fileChooser.getSelectedFile().toPath());
        }
    }

    private static void postProcess(Path file) {
        try {
            List<String> lines = Files.readAllLines(file);
            List<AngleSet> angles = lines.stream()
                    .map((String line) -> {
                        String[] parts = line.split("\\w");
                        Angle theta1 = new Angle(Double.parseDouble(parts[2]), AngleType.DEGREES);
                        Angle theta2 = new Angle(Double.parseDouble(parts[3]), AngleType.DEGREES);
                        return new AngleSet(theta1, theta2);
                    })
                    .collect(Collectors.toList());
        }
        catch (IOException ex) {

        }
    }

    private static void test() {
//        final RobotArm arm = new RobotArm();
//
//        final FuncPoint point = arm.endPoint(new Angle(120, AngleType.DEGREES), new Angle(115, AngleType.DEGREES));
//        System.out.println("Point: " + point);
//        System.out.println();
//
//        final AngleSet angles = arm.angles(point.x, point.y);
//
//        System.out.println("Theta1: " + angles.theta1.getMeasure(AngleType.DEGREES, MeasureRange.FullCircle));
//        System.out.println("Theta2: " + angles.theta2.getMeasure(AngleType.DEGREES, MeasureRange.FullCircle));
//        System.out.println();
//
//        System.out.println("Resultant: " + arm.endPoint(angles.theta1, angles.theta2));

        final FuncPoint point = new FuncPoint(0.9, 1.1);
        final AngleSet angles = arm.angles(point.x, point.y);
        final FuncPoint redo = arm.endPoint(angles.theta1, angles.theta2);
        System.out.println("Theta1: " + angles.theta1.getMeasure(AngleType.DEGREES, MeasureRange.FullCircle));
        System.out.println("Theta2: " + angles.theta2.getMeasure(AngleType.DEGREES, MeasureRange.FullCircle));
        System.out.println("Redone: " + redo);
    }

}
