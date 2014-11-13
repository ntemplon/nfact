/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics.airplane;

import aero.fluid.FluidState;
import aero.fluid.IdealGas;
import aero.fluid.IdealGasState;
import dynamics.AeroSystemState;
import dynamics.AerodynamicSystem;
import dynamics.DynamicSystemState;
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
    
    private final double mass;
    private final double iyy;
    
    private final SolidRocketEngine engine;
    
    private final double clAlpha;
    private final double clDeltaE;
    private final double clQ;
    private final double cd0;
    private final double cpm0;
    private final double cpmAlpha;
    private final double cpmQ;
    
    private final AeroSystemState initialState;


    // Properties
    @Override
    public double getMass() {
        return 2.8 / PhysicalConstants.GRAVITY_ACCELERATION;
    }

    @Override
    public double getIyy() {
        return this.iyy;
    }

    @Override
    public AeroSystemState getInitialState() {
        return this.initialState;
    }


    // Initializtion
    public RocketPlane(HobbyRocketEngine engine) {
        super();
        
        this.sRef = 2.4;
        this.cBar = 0.5291;
        this.spanEfficiency = 0.87;
        this.aspectRatio = 6.3;
        this.zThrust = 0.0;
        this.mass = 2.8 / PhysicalConstants.GRAVITY_ACCELERATION;
        this.iyy = 0.2 * (3 * 3 + 0.33 * 0.33) * this.mass;
        
        this.engine = engine;
        
        this.clAlpha = 4.5891;
        this.clDeltaE = 0.0;
        this.clQ = 0.0;
        this.cd0 = 0.02;
        this.cpm0 = -0.02;
        this.cpmAlpha = -0.373;
        this.cpmQ = -12.6;
        
        AeroSystemState state = new AeroSystemState();

        state.set(AeroSystemState.ANGULAR_POS, new Angle(89.5, AngleType.DEGREES));
        state.set(AeroSystemState.ANGULAR_VEL, 0.0);
        state.set(AeroSystemState.X_POS, 0.0);
        state.set(AeroSystemState.X_VEL, 0.0);
        state.set(AeroSystemState.Z_POS, 0.0);
        state.set(AeroSystemState.Z_VEL, 0.1);

        // Air on a hot day
        FluidState fluid = new IdealGasState(new IdealGas(28.97, 1.4), 95.0 + 459.0, 2018.68);
        state.set(AeroSystemState.FLUID_STATE, fluid);
        
        this.initialState = state;
    }
    
    public RocketPlane(RocketPlaneParameters prms) {
        this.sRef = prms.getSRef();
        this.cBar = prms.getCBar();
        this.spanEfficiency = prms.getSpanEfficiency();
        this.aspectRatio = prms.getAspectRatio();
        this.zThrust = prms.getZThrust();
        this.mass = prms.getMass();
        this.iyy = prms.getIyy();
        
        this.engine = prms.getRocketEngine();
        
        this.clAlpha = prms.getClAlpha();
        this.clDeltaE = prms.getClDeltaE();
        this.clQ = prms.getClQ();
        this.cd0 = prms.getCd0();
        this.cpm0 = prms.getCpm0();
        this.cpmAlpha = prms.getCpmAlpha();
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
        double cl = this.clAlpha * state.get(AeroSystemState.ALPHA).getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180);
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
        double cpmFromAlpha = this.cpmAlpha * state.get(AeroSystemState.ALPHA).getMeasure(AngleType.RADIANS);
        state.set(AeroSystemState.CPMA, cpmFromAlpha);

        double qHat = (state.get(AeroSystemState.ANGULAR_VEL) * this.cBar) / (2 * state.get(DynamicSystemState.SPEED));
        double cpmFromQ = this.cpmQ * qHat;
        state.set(AeroSystemState.CPMQ, cpmFromQ);

        double thrust = this.getThrust(state);
        double thrustPitchMoment = -1 * zThrust * thrust;
        double cpmt = (thrustPitchMoment / (state.get(AeroSystemState.DYNAMIC_PRESSURE) * this.sRef * this.cBar));
        state.set(AeroSystemState.CPMT, cpmt);

        double cpm = this.cpm0 + cpmFromAlpha + cpmFromQ + cpmt;

        state.set(AeroSystemState.CPM, cpm);
        return cpm;
    }

}
