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
package dynamics.airplane;

import aero.fluid.FluidState;
import aero.fluid.IdealGas;
import aero.fluid.IdealGasState;
import dynamics.AeroSystemState;
import dynamics.AerodynamicSystem;
import dynamics.DynamicSystemState;
import dynamics.SystemState;
import geometry.angle.Angle;
import geometry.angle.Angle.AngleType;
import geometry.angle.Angle.MeasureRange;
import propulsion.rocket.HobbyRocketEngine;
import propulsion.rocket.SolidRocketEngine;
import util.PhysicalConstants;

/**
 *
 * @author nathant
 */
public class RocketPlane extends AerodynamicSystem {

    // Fields
    private final double sRef;
    private final double cBar;
    private final double spanEfficiency;
    private final double aspectRatio;
    private final double zThrust;
    
    private final double iyy;
    private final double baseMass;
    
    private final SolidRocketEngine engine;
    
    private final double clAlpha;
    private final Angle alphaZeroLift;
    private final double clDeltaE;
    private final double clQ;
    private final double cd0;
    private final double cpm0;
    private final double cpmAlpha;
    private final double cpmDeltaE;
    private final double cpmQ;
    
    private Angle elevatorDeflection = new Angle(0);
    
    private final AeroSystemState initialState;


    // Properties
    @Override
    public double getMass(AeroSystemState state) {
        double mass = this.baseMass + this.engine.getMass(state.get(SystemState.TIME));
        state.set(AeroSystemState.MASS, mass);
        return mass;
    }

    @Override
    public double getIyy(AeroSystemState state) {
        return this.iyy;
    }

    @Override
    public AeroSystemState getInitialState() {
        return this.initialState;
    }
    
    public Angle getElevatorDeflection() {
        return this.elevatorDeflection;
    }
    
    public void setElevatorDeflection(Angle deflection) {
        this.elevatorDeflection = deflection;
    }


    // Initializtion
//    public RocketPlane(HobbyRocketEngine engine) {
//        super();
//        
//        this.sRef = 2.4;
//        this.cBar = 0.5291;
//        this.spanEfficiency = 0.87;
//        this.aspectRatio = 6.3;
//        this.zThrust = 0.0;
//        this.baseMass = 2.373 / PhysicalConstants.GRAVITY_ACCELERATION;
//        this.iyy = 0.2 * (3 * 3 + 0.33 * 0.33) * (this.baseMass + engine.getMass(0));
//        
//        this.engine = engine;
//        
//        this.clAlpha = 4.5891;
//        this.alphaZeroLift = new Angle(-1.858, AngleType.DEGREES);
//        this.clDeltaE = 0.0;
//        this.clQ = 0.0;
//        this.cd0 = 0.02;
//        this.cpm0 = -0.02;
//        this.cpmAlpha = -0.373;
//        this.cpmDeltaE = 0.0;
//        this.cpmQ = -12.6;
//        
//        AeroSystemState state = new AeroSystemState();
//
//        state.set(AeroSystemState.ANGULAR_POS, new Angle(89.5, AngleType.DEGREES));
//        state.set(AeroSystemState.ANGULAR_VEL, 0.0);
//        state.set(AeroSystemState.X_POS, 0.0);
//        state.set(AeroSystemState.X_VEL, 0.0);
//        state.set(AeroSystemState.Z_POS, 0.0);
//        state.set(AeroSystemState.Z_VEL, 0.1);
//
//        // Air on a hot day
//        FluidState fluid = new IdealGasState(new IdealGas(28.97, 1.4), 95.0 + 459.0, 2018.68);
//        state.set(AeroSystemState.FLUID_STATE, fluid);
//        
//        this.initialState = state;
//    }
    
    public RocketPlane(RocketPlaneParameters prms) {
        this.sRef = prms.getSRef();
        this.cBar = prms.getCBar();
        this.spanEfficiency = prms.getSpanEfficiency();
        this.aspectRatio = prms.getAspectRatio();
        this.zThrust = prms.getZThrust();
        this.baseMass = prms.getBaseMass();
        this.iyy = prms.getIyy();
        
        this.engine = prms.getRocketEngine();
        
        this.clAlpha = prms.getClAlpha();
        this.alphaZeroLift = prms.getAlphaZeroLift();
        this.clDeltaE = prms.getClDeltaE();
        this.clQ = prms.getClQ();
        this.cd0 = prms.getCd0();
        this.cpm0 = prms.getCpm0();
        this.cpmAlpha = prms.getCpmAlpha();
        this.cpmDeltaE = prms.getCpmDeltaE();
        this.cpmQ = prms.getCpmQ();
        
        this.initialState = prms.getInitialState();
    }


    // Public Methods
    @Override
    public double getLift(AeroSystemState state) {
        double cl = this.getCl(state);
        return cl * state.get(AeroSystemState.DYNAMIC_PRESSURE) * this.sRef;
    }

    @Override
    public double getDrag(AeroSystemState state) {
        return this.getCd(state) * state.get(AeroSystemState.DYNAMIC_PRESSURE) * this.sRef;
    }

    @Override
    public double getPitchingMoment(AeroSystemState state) {
        return this.getCpm(state) * state.get(AeroSystemState.DYNAMIC_PRESSURE) * this.sRef * this.cBar;
    }

    @Override
    public double getThrust(AeroSystemState state) {
        double thrust = this.engine.getThrust(state.get(AeroSystemState.TIME));
        state.set(AeroSystemState.THRUST, thrust);
        return thrust;
    }

    public double getCl(AeroSystemState state) {
        Angle totalAlpha = state.get(AeroSystemState.ANGLE_OF_ATTACK).add(alphaZeroLift.scalarMultiply(-1.0));
        double cl = this.clAlpha * totalAlpha.getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180);
        
        double cle = this.getElevatorDeflection().getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180) * this.clDeltaE;
        cl += cle;
        
        double qHat = (state.get(AeroSystemState.ANGULAR_VEL) * this.cBar) / (2 * state.get(DynamicSystemState.SPEED));
        cl += qHat * this.clQ;
        
        state.set(AeroSystemState.CL, cl);
        return cl;
    }

    public double getCd(AeroSystemState state) {
        double cdi = (getCl(state) * getCl(state)) / (Math.PI * this.spanEfficiency * this.aspectRatio);
        double cd = this.cd0 + cdi;
        state.set(AeroSystemState.CD, cd);
        return cd;
    }

    public double getCpm(AeroSystemState state) {
        double cpmFromAlpha = this.cpmAlpha * state.get(AeroSystemState.ANGLE_OF_ATTACK).getMeasure(AngleType.RADIANS);
        state.set(AeroSystemState.CPMA, cpmFromAlpha);

        double qHat = (state.get(AeroSystemState.ANGULAR_VEL) * this.cBar) / (2 * state.get(DynamicSystemState.SPEED));
        double cpmFromQ = this.cpmQ * qHat;
        state.set(AeroSystemState.CPMQ, cpmFromQ);
        
        double cpmFromElevator = this.getElevatorDeflection().getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180) * this.cpmDeltaE;

        double thrust = this.getThrust(state);
        double thrustPitchMoment = -1 * zThrust * thrust;
        double cpmt = (thrustPitchMoment / (state.get(AeroSystemState.DYNAMIC_PRESSURE) * this.sRef * this.cBar));
//        double cpmt = 0.0;
        state.set(AeroSystemState.CPMT, cpmt);

        double cpm = this.cpm0 + cpmFromAlpha + cpmFromElevator + cpmFromQ + cpmt;

        state.set(AeroSystemState.CPM, cpm);
        return cpm;
    }

}
