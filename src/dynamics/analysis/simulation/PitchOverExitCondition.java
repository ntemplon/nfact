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

import dynamics.AeroSystemState;

/**
 *
 * @author nathan
 */
public class PitchOverExitCondition implements ExitCondition<AeroSystemState> {

    private boolean threshholdReached = false;

    @Override
    public boolean isFinished(AeroSystemState state) {
        if (state.get(AeroSystemState.DYNAMIC_PRESSURE) == null || state.get(AeroSystemState.Z_VEL) == null ||
                state.get(AeroSystemState.THRUST) == null) {
            return false;
        }
        
        if (state.get(AeroSystemState.DYNAMIC_PRESSURE) > 1) {
            threshholdReached = true;
        }
        return threshholdReached && !(state.get(AeroSystemState.Z_VEL) > 0
            || state.get(AeroSystemState.THRUST) > 0);
    }

}
