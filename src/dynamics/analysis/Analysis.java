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
package dynamics.analysis;

import java.util.ArrayList;
import java.util.List;
import util.ArrayUtil;

/**
 *
 * @author Nathan Templon
 */
public class Analysis {

    // Fields
    private final List<AnalysisStep> steps;


    // Properties
    public AnalysisStep[] steps() {
        return ArrayUtil.asArray(steps);
    }


    // Initialization
    public Analysis() {
        this.steps = new ArrayList<>();
    }


    // Public Methods
    /**
     * Adds an analysis step to the end of the analysis
     *
     * @param step
     */
    public void addStep(AnalysisStep step) {
        this.steps.add(step);
    }

    /**
     * Adds the specified step into the analysis at the specified index
     *
     * @param step
     * @param index
     * @return Returns true if the step was successfully added, and false if it
     * was not (for example, if there was an IndexOutOfBoundsException)
     */
    public boolean addStep(AnalysisStep step, int index) {
        try {
            this.steps.add(index, step);
            return true;
        }
        catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }
    
    public boolean removeStep(AnalysisStep step) {
        if (this.steps.contains(step)) {
            this.steps.remove(step);
            return true;
        }
        return false;
    }
    
    public boolean removeStep(int index) {
        if (index < this.steps.size() - 1) {
            this.steps.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Performs the analysis
     */
    public void run() {
        StringBuilder output = new StringBuilder();
        
        this.steps.stream().forEach((AnalysisStep step) -> {
            AnalysisResults results = step.performAnalysis();
            if (results.exception != null) {
                output.append("Exception: ").append(results.exception.getLocalizedMessage()).append(System.lineSeparator());
            }
            if (results.includeInOutput) {
                output.append(results.outputString).append(System.lineSeparator());
            }
        });
    }

}
