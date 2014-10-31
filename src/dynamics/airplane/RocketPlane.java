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
    private double cBar;
    private double spanEfficiency;
    private double aspectRatio;
    private double zThrust;
    
    private double mass;
    private double iyy;
    
    private final SolidRocketEngine engine;
    
    private double clAlpha;
    private double clDeltaE;
    private double clQ;
    private double cd0;
    private double cpm0;
    private double cpmAlpha;
    private double cpmQ;
    
    private AeroSystemState initialState;


    // Properties
    @Override
    public double getMass() {
        return 2.8 / PhysicalConstants.GRAVITY_ACCELERATION;
    }

    @Override
    public double getIyy() {
        return 0.2 * (3 * 3 + 0.33 * 0.33) * this.getMass();
    }

    @Override
    public AeroSystemState getInitialState() {
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

        return state;
    }


    // Initializtion
    public RocketPlane(HobbyRocketEngine engine) {
        this.engine = engine;
        
        this.sRef = 2.4;
        this.cBar = 0.5291;
        this.spanEfficiency = 0.87;
    }
    
    public RocketPlane(RocketPlaneParameters prms) {
        this.sRef = prms.getSRef();
        this.cBar = prms.getCBar();
        this.spanEfficiency = prms.getSpanEfficiency();
        
        // Continue implementation here after work.
        
        this.engine = prms.getRocketEngine();
    }


    // Public Methods
    @Override
    public double getLift(AeroSystemState state) {
        double cl = this.getCl(state);
        return cl * state.getDynamicPressure() * 2.4;
    }

    @Override
    public double getDrag(AeroSystemState state) {
        return this.getCd(state) * state.getDynamicPressure() * 2.4;
    }

    @Override
    public double getPitchingMoment(AeroSystemState state) {
        return this.getCpm(state) * state.getDynamicPressure() * 2.4 * 0.5291;
    }

    @Override
    public double getThrust(AeroSystemState state) {
        double thrust = this.engine.getThrust(state.get(AeroSystemState.TIME));
        state.set(AeroSystemState.THRUST, thrust);
        return thrust;
    }

    public double getCl(AeroSystemState state) {
        double cl = 4.5891 * state.getAngleOfAttack().getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180);
        state.set(AeroSystemState.CL, cl);
        return cl;
    }

    public double getCd(AeroSystemState state) {
        double cd = 0.02 + ((getCl(state) * getCl(state)) / (Math.PI * 0.87 * 6.3));
        state.set(AeroSystemState.CD, cd);
        return cd;
    }

    public double getCpm(AeroSystemState state) {
        double cpm0 = -0.02;
        double cpmAlpha = -0.373;
        double cpmQ = -12.6;
        double zThrust = 0.0 / 12.0;

        double cpmFromAlpha = cpmAlpha * state.getAngleOfAttack().getMeasure(AngleType.RADIANS);
        state.set(AeroSystemState.CPMA, cpmFromAlpha);

        double qHat = (state.get(AeroSystemState.ANGULAR_VEL) * 0.5291) / (2 * state.getSpeed());
        double cpmFromQ = cpmQ * qHat;
        state.set(AeroSystemState.CPMQ, cpmFromQ);

        double thrust = this.getThrust(state);
        double thrustPitchMoment = -1 * zThrust * thrust;
        double cpmt = (thrustPitchMoment / (state.getDynamicPressure() * 2.4 * 0.5291));
        state.set(AeroSystemState.CPMT, cpmt);

        double cpm = cpm0 + cpmFromAlpha + cpmFromQ + cpmt;

        state.set(AeroSystemState.CPM, cpm);
        return cpm;
    }

}
