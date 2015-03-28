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
import dynamics.AerodynamicSystem;
import dynamics.Inertia;
import dynamics.SystemState;
import dynamics.analysis.InertiaModel;
import propulsion.PropulsionForceModel;
import propulsion.rocket.HobbyRocketEngine;
import util.PhysicalConstants;

/**
 *
 * @author Nathan Templon
 */
public class Sharp3 implements AerodynamicCoefficientModel, PropulsionForceModel, InertiaModel {

    // Fields
    private final HobbyRocketEngine engine = HobbyRocketEngine.M750;
    private final double baseMass = 32.13 / PhysicalConstants.GRAVITY_ACCELERATION;
    private final double length = 9.5;
    private final double baseDiameter = 2.0 / 3.0;
    private final double baseArea = Math.PI * (baseDiameter * baseDiameter) / 4.0;
    private final double maximumDiameter = 2.0 / 3.0;
    private final double maximumArea = Math.PI * (maximumDiameter * maximumDiameter) / 4.0;
    private final double refrenceArea = this.maximumArea;
    private final double wettedArea = this.length * this.maximumDiameter * Math.PI;
    
    
    // Fields
    private final Inertia inertia;
    
    
    // Initialization
    public Sharp3() {
        this.inertia = new Inertia();
        
        this.inertia.setIxx(1.0);
        this.inertia.setIxy(1.0);
        this.inertia.setIxz(1.0);
        this.inertia.setIyy(1.0);
        this.inertia.setIyz(1.0);
        this.inertia.setIzz(1.0);
        this.inertia.setMass(this.getMass(0.0));
    }
    

    // Public Methods
    @Override
    public double cl(SystemState state) {
        return 0.0;
    }

    @Override
    public double cd(SystemState state) {
        double cd = 0.0;
        
        double reynolds = state.get(AerodynamicSystem.REYNOLDS);
        double mach = state.get(AerodynamicSystem.MACH);
        double cdsf = 0.455 / (Math.pow(Math.log10(reynolds), 2.58) * Math.pow(1.0 + 0.144 * Math.pow(mach, 2), 0.65) * (this.wettedArea / this.refrenceArea));
        cd += cdsf;
        
        double cdbase = (0.139 + 0.419 * Math.pow(mach, 2)) * (this.baseArea / this.refrenceArea);
        cd += cdbase;
        
        return cd;
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
        this.inertia.setMass(this.getMass(time));
        return this.inertia;
    }
    
    
    // Private Methods
    private double getMass(double time) {
        return this.baseMass + this.engine.getMass(time);
    }

}
