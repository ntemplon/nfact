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
package aero;

import com.jupiter.ganymede.math.function.SingleVariableRealFunction;
import dynamics.AeroSystemState;

/**
 *
 * @author NathanT
 */
public abstract class Wing {

    // Public Methods
    public abstract WingPlanform getPlanform();

    public abstract double spanEfficiency();

    public abstract WingSection sectionAt(double station);

    public abstract double cl(AeroSystemState state);

    public abstract double cd(AeroSystemState state);

    public abstract double cpm(AeroSystemState state);

    public abstract SingleVariableRealFunction clFunction();

    public abstract SingleVariableRealFunction cdFunction();

    public abstract SingleVariableRealFunction cpmFunction();

}
