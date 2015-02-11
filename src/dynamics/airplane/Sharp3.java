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
package dynamics.airplane;

import aero.AerodynamicCoefficientModel;
import dynamics.Inertia;
import dynamics.SystemState;
import dynamics.analysis.InertiaModel;
import propulsion.PropulsionForceModel;
import propulsion.rocket.HobbyRocketEngine;

/**
 *
 * @author Nathan Templon
 */
public class Sharp3 implements AerodynamicCoefficientModel, PropulsionForceModel, InertiaModel {
    
    // Fields
    private final HobbyRocketEngine engine = HobbyRocketEngine.G25;
    
    
    // Public Methods
    @Override
    public double cl(SystemState state) {
        return 0.0;
    }

    @Override
    public double cd(SystemState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double csf(SystemState state) {
        return 0.0;
    }

    @Override
    public double cpm(SystemState state) {
        return 0.0;
    }

    @Override
    public double cym(SystemState state) {
        return 0.0;
    }

    @Override
    public double crm(SystemState state) {
        return 0.0;
    }

    @Override
    public double thrust(SystemState state) {
        return this.engine.getThrust(state.getTime());
    }

    @Override
    public Inertia getInertia(double time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
