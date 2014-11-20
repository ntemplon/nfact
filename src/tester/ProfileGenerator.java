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
package tester;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Nathan Templon
 */
public class ProfileGenerator {

    // Constants
    public static final String SEPERATOR = " ";

    public static void main(String args[]) {
        String bodyFile = "/home/nathan/body1.dat";
        writeBodyProfileNewForm(bodyFile);
    }

    private static void writeBodyProfile(String filePath) {
        final double bodyLength = 3.0;
        final double bodyDiameter = 4.0 / 12.0;
        final double taperLength = 6.0 / 12.0;
        final double taperFinalDiam = 2.5 / 12.0;
        final int numberOfIncs = 20;

        final double bodyRadius = bodyDiameter / 2.0;
        final double taperFinalRadius = taperFinalDiam / 2.0;

        StringBuilder content = new StringBuilder();

        // Top profile
        // Nose
        double amountPerInc = (Math.PI / 2.0) / 20.0;
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double angle = inc * amountPerInc;
            double x = bodyRadius * Math.sin(angle);
            double y = bodyRadius * (1.0 - Math.cos(angle));

            content.append((float) x).append(" ").append((float) y).append(
                    System.lineSeparator());
        }

        // Body
        amountPerInc = (bodyLength - taperLength - bodyRadius) / numberOfIncs;
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double xPos = bodyRadius + amountPerInc * inc;
            content.append((float) xPos).append(" ").append((float) bodyRadius).append(
                    System.lineSeparator());
        }

        // Taper
        amountPerInc = taperLength / numberOfIncs;
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double xPos = (bodyLength - taperLength) + inc * amountPerInc;
            double y = bodyRadius - (((xPos - (bodyLength - taperLength)) / taperLength) * (bodyRadius - taperFinalRadius));
            content.append((float) xPos).append(" ").append((float) y).append(
                    System.lineSeparator());
        }
        content.append((float) bodyLength).append(" ").append(
                (float) taperFinalRadius).append(System.lineSeparator());

        // Bottom profile
        // Nose
        amountPerInc = (Math.PI / 2.0) / 20.0;
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double angle = inc * amountPerInc;
            double x = bodyRadius * Math.sin(angle);
            double y = -1 * (bodyRadius * (1.0 - Math.cos(angle)));

            content.append((float) x).append(" ").append((float) y).append(
                    System.lineSeparator());
        }

        // Body
        amountPerInc = (bodyLength - taperLength - bodyRadius) / numberOfIncs;
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double xPos = bodyRadius + amountPerInc * inc;
            content.append((float) xPos).append(" ").append(
                    (float) (-1.0 * bodyRadius)).append(
                            System.lineSeparator());
        }

        // Taper
        amountPerInc = taperLength / numberOfIncs;
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double xPos = (bodyLength - taperLength) + inc * amountPerInc;
            double y = -1.0 * (bodyRadius - (((xPos - (bodyLength - taperLength)) / taperLength) * (bodyRadius - taperFinalRadius)));
            content.append((float) xPos).append(" ").append((float) y).append(
                    System.lineSeparator());
        }
        content.append((float) bodyLength).append(" ").append(
                (float) taperFinalRadius).append(System.lineSeparator());

        // Output
        File output = new File(filePath);
        try (FileWriter fw = new FileWriter(output);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw)) {

            pw.println(content.toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void writeBodyProfileNewForm(String filePath) {
        // Constants
        final double noseLength = 6.0 / 12.0;
        final double bodyLength = 3.0;
        final double bodyDiameter = 4.0 / 12.0;
        final double taperLength = 6.0 / 12.0;
        final double taperFinalDiam = 2.5 / 12.0;
        final int numberOfIncs = 20;

        final double bodyRadius = bodyDiameter / 2.0;
        final double taperFinalRadius = taperFinalDiam / 2.0;

        StringBuilder content = new StringBuilder();

        // Top Surface
        // Taper
        double stepPerInc = taperLength / ((double)numberOfIncs);
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double x = bodyLength - (inc * stepPerInc);
            double y = bodyRadius - ((x - (bodyLength - taperLength)) / (taperLength)) * (bodyRadius - taperFinalRadius);
            content.append(getFormattedString(x));
            content.append(SEPERATOR);
            content.append(getFormattedString(y));
            content.append(System.lineSeparator());
        }

        // Main Section
        stepPerInc = (bodyLength - taperLength - noseLength) / ((double)numberOfIncs);
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double x = bodyLength - taperLength - (inc * stepPerInc);
            double y = bodyRadius;
            content.append(getFormattedString(x));
            content.append(SEPERATOR);
            content.append(getFormattedString(y));
            content.append(System.lineSeparator());
        }
        
        // Nose
        stepPerInc = noseLength / ((double)numberOfIncs);
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double x = noseLength - (stepPerInc * inc);
            double y = bodyRadius * Math.sqrt(1 - ((noseLength - x) / noseLength) * ((noseLength - x) / noseLength));
            content.append(getFormattedString(x));
            content.append(SEPERATOR);
            content.append(getFormattedString(y));
            content.append(System.lineSeparator());
        }
        
        // Bottom Surface
        // Nose
        stepPerInc = noseLength / ((double)numberOfIncs);
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double x = stepPerInc * inc;
            double y = -1.0 * bodyRadius * Math.sqrt(1 - ((noseLength - x) / noseLength) * ((noseLength - x) / noseLength));
            content.append(getFormattedString(x));
            content.append(SEPERATOR);
            content.append(getFormattedString(y));
            content.append(System.lineSeparator());
        }
        
        // Main Section
        stepPerInc = (bodyLength - taperLength - noseLength) / ((double)numberOfIncs);
        for (int inc = 0; inc < numberOfIncs; inc++) {
            double x = noseLength + (inc * stepPerInc);
            double y = -1.0 * bodyRadius;
            content.append(getFormattedString(x));
            content.append(SEPERATOR);
            content.append(getFormattedString(y));
            content.append(System.lineSeparator());
        }
        
        // Taper
        stepPerInc = taperLength / ((double)numberOfIncs);
        for (int inc = 0; inc <= numberOfIncs; inc++) {
            double x = (bodyLength - taperLength) + (inc * stepPerInc);
            double y = -1.0 * (bodyRadius - (((x - (bodyLength - taperLength)) / (taperLength)) * (bodyRadius - taperFinalRadius)));
            content.append(getFormattedString(x));
            content.append(SEPERATOR);
            content.append(getFormattedString(y));
            content.append(System.lineSeparator());
        }

        // Output
        File output = new File(filePath);
        try (FileWriter fw = new FileWriter(output);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw)) {

            pw.println(content.toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static String getFormattedString(double d) {
        return ((float) d) + "";
    }

}
