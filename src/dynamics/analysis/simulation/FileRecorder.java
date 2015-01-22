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
package dynamics.analysis.simulation;

import dynamics.SystemProperty;
import dynamics.analysis.SystemState;
import com.jupiter.ganymede.math.geometry.Angle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 *
 * @author Nathan Templon
 * @param <T>
 */
public class FileRecorder implements SimulationRecorder, AutoCloseable {

    // Fields
    private final File outputFile;
    private final SystemProperty[] outputVariables;
    private DecimalFormat format;

    private FileWriter fw;
    private BufferedWriter bw;
    private PrintWriter pw;


    // Properties
    public File getOutputFile() {
        return this.outputFile;
    }

    public boolean outputFileExists() {
        if (this.getOutputFile() != null) {
            return this.outputFile.exists();
        }
        return false;
    }

    public SystemProperty[] getOutputVariables() {
        return this.outputVariables;
    }

    public DecimalFormat getDecimalFormat() {
        return this.format;
    }

    public void setDecimalFormat(DecimalFormat value) {
        this.format = value;
    }


    // Initialization
    public FileRecorder(File outputFile, SystemProperty[] outputVariables) {
        this.outputFile = outputFile;
        this.outputVariables = outputVariables;
        this.format =  new DecimalFormat("0.0000");
    }
    
    
    // Public Methods
    public void print(Object obj) {
        if (this.fw == null || this.pw == null) {
            return;
        }
        
        this.pw.print(obj);
    }
    
    public void println(Object obj) {
        if (this.fw == null || this.pw == null) {
            return;
        }
        
        this.pw.println(obj);
    }


    // SimulationRecorder implementation
    @Override
    public void start(SystemState initialState) {
        if (!this.getOutputFile().exists()) {
            try {
                if (!this.getOutputFile().getParentFile().exists()) {
                    this.getOutputFile().getParentFile().mkdirs();
                }
                this.getOutputFile().createNewFile();
            }
            catch (IOException ex) {
                return;
            }
        }
        try {
            fw = new FileWriter(this.getOutputFile());
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            // Print variable names
            for (int i = 0; i < outputVariables.length; i++) {
                SystemProperty variable = outputVariables[i];
                pw.print(variable.getName());
                if (i < outputVariables.length - 1) {
                    pw.print(",");
                }
                else {
                    pw.println("");
                }
            }

            this.writeState(initialState);
        }
        catch (IOException ex) {

        }
    }

    @Override
    public void recordState(SystemState state) {
        if (fw == null || pw == null) {
            return;
        }

        this.writeState(state);
    }

    @Override
    public void finish(SystemState finalState) {
        this.writeState(finalState);

        this.close();
    }


    // AutoCloseable implementation
    @Override
    public void close() {
        if (this.pw != null) {
            this.pw.close();
        }
        if (this.bw != null) {
            try {
                this.bw.close();
            }
            catch (IOException ex) {

            }
        }
        if (this.fw != null) {
            try {
                this.fw.close();
            }
            catch (IOException ex) {

            }
        }
    }


    // Protected Methods
    protected void writeState(SystemState state) {
        for (int i = 0; i < outputVariables.length; i++) {
            SystemProperty variable = outputVariables[i];

            Object value = state.get(variable);
            if (value instanceof Angle) {
                pw.print(format.format(((Angle) value).getMeasure(Angle.AngleType.DEGREES)));
            }
            else if (value instanceof Double) {
                pw.print(format.format((Double) value));
            }
            else {
                pw.print(value);
            }

            if (i < outputVariables.length - 1) {
                pw.print(",");
            }
            else {
                pw.println("");
            }
        }

    }
}
