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
import com.jupiter.ganymede.math.matrix.Matrix;
import com.jupiter.ganymede.math.vector.Vector;
import dynamics.AerodynamicSystem;
import dynamics.Inertia;
import dynamics.SystemState;
import com.jupiter.ganymede.math.geometry.Angle;
import propulsion.rocket.SolidRocketEngine;

/**
 *
 * @author nathant
 */
public class RocketPlane {

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

    // Properties
//    @Override
//    public double getMass(AeroSystemState state) {
//        double mass = this.baseMass + this.engine.getMass(state.get(SystemState.TIME));
//        state.set(AeroSystemState.MASS, mass);
//        return mass;
//    }
//
//    @Override
//    public double getIyy(AeroSystemState state) {
//        return this.iyy;
//    }
//
//    public Angle getElevatorDeflection() {
//        return this.elevatorDeflection;
//    }
//
//    public void setElevatorDeflection(Angle deflection) {
//        this.elevatorDeflection = deflection;
//    }

    // Initializtion
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
    }

    // Public Methods
//    @Override
//    public double getLift(AeroSystemState state) {
//        double cl = this.getCl(state);
//        return cl * state.get(AeroSystemState.DYNAMIC_PRESSURE) * this.sRef;
//    }
//
//    @Override
//    public double getDrag(AeroSystemState state) {
//        return this.getCd(state) * state.get(AeroSystemState.DYNAMIC_PRESSURE) * this.sRef;
//    }
//
//    @Override
//    public double getPitchingMoment(AeroSystemState state) {
//        return this.getCpm(state) * state.get(AeroSystemState.DYNAMIC_PRESSURE) * this.sRef * this.cBar;
//    }
//
//    @Override
//    public double getThrust(AeroSystemState state) {
//        double thrust = this.engine.getThrust(state.get(AeroSystemState.TIME));
//        state.set(AeroSystemState.THRUST, thrust);
//        return thrust;
//    }
//
//    public double getCl(AeroSystemState state) {
//        Angle totalAlpha = state.get(AeroSystemState.ANGLE_OF_ATTACK).add(alphaZeroLift.scalarMultiply(-1.0));
//        double cl = this.clAlpha * totalAlpha.getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180);
//
//        double cle = this.getElevatorDeflection().getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180) * this.clDeltaE;
//        cl += cle;
//
//        double qHat = (state.get(AeroSystemState.ANGULAR_VEL) * this.cBar) / (2 * state.get(DynamicSystemState.SPEED));
//        cl += qHat * this.clQ;
//
//        state.set(AeroSystemState.CL, cl);
//        return cl;
//    }
//
//    public double getCd(AeroSystemState state) {
//        // Random constants are empiracle adjustments to match AVL's data, based on the presence of two lifting surfaces, not one.
//        double cdi = (getCl(state) * getCl(state) * (1.8 / 2.4) * (1.8 / 2.4) * 1.05 * 1.05) / (Math.PI * this.spanEfficiency * this.aspectRatio);
//        double cd = this.cd0 + cdi;
//        state.set(AeroSystemState.CD, cd);
//        return cd;
//    }
//
//    public double getCpm(AeroSystemState state) {
//        double cpm0Prime = this.cpm0;
//        double cpmAlphaPrime = this.cpmAlpha;
//
//        if (this.engine.equals(HobbyRocketEngine.G25)) {
//            double motorBurnFrac = 1 - (this.engine.getMass(state.get(SystemState.TIME)) - this.engine.getMass(this.engine.getBurnTime())) / (this.engine.getMass(0.0) - this.engine.getMass(this.engine.getBurnTime()));
//            cpm0Prime = -0.0346 + (-0.0510 + 0.0346) * motorBurnFrac;
//
//            cpmAlphaPrime = -0.5479 + (-0.9973 + 0.5479) * motorBurnFrac;
////            System.out.println(motorBurnFrac);
//        }
//
//        double cpmFromAlpha = cpmAlphaPrime * state.get(AeroSystemState.ANGLE_OF_ATTACK).getMeasure(AngleType.RADIANS);
//        state.set(AeroSystemState.CPMA, cpmFromAlpha);
//
//        double qHat = (state.get(AeroSystemState.ANGULAR_VEL) * this.cBar) / (2 * state.get(DynamicSystemState.SPEED));
//        double cpmFromQ = this.cpmQ * qHat;
//        state.set(AeroSystemState.CPMQ, cpmFromQ);
//
//        double cpmFromElevator = this.getElevatorDeflection().getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180) * this.cpmDeltaE;
//
//        double thrust = this.getThrust(state);
//        double thrustPitchMoment = -1 * zThrust * thrust;
//        double cpmt = (thrustPitchMoment / (state.get(AeroSystemState.DYNAMIC_PRESSURE) * this.sRef * this.cBar));
////        double cpmt = 0.0;
//        state.set(AeroSystemState.CPMT, cpmt);
//
//        double cpm = cpm0Prime + cpmFromAlpha + cpmFromElevator + cpmFromQ + cpmt;
//
//        state.set(AeroSystemState.CPM, cpm);
//        return cpm;
//    }

    public SystemState getInitialState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Inertia getInertia(double time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getReferenceLength() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public FluidState getFluidState(double time, Vector stateVector) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
