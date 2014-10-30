/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import aero.fluid.FluidState;
import aero.fluid.IdealGas;
import aero.fluid.IdealGasState;
import geometry.angle.Angle;
import geometry.angle.Angle.AngleType;
import geometry.angle.Angle.MeasureRange;
import propulsion.rocket.HobbyRocketEngine;
import util.PhysicalConstants;

/**
 *
 * @author nathant
 */
public class TestSystem extends AerodynamicSystem {
    
    // Fields
    private final HobbyRocketEngine engine;
    

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
        
        state.setAngularPosition(new Angle(89.5, AngleType.DEGREES));
        state.setAngularVelocity(0);
        state.setXPosition(0);
        state.setXVelocity(0);
        state.setZPosition(0);
        state.setZVelocity(0.1);
        
        // Air on a hot day
        FluidState fluid = new IdealGasState(new IdealGas(28.97, 1.4), 95.0 + 459.0, 2018.68);
        state.setFluidState(fluid);
        
        return state;
    }
    
    
    // Initializtion
    public TestSystem(HobbyRocketEngine engine) {
        this.engine = engine;
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
        double thrust = this.engine.getThrustAt(state.getTime());
        state.setThrust(thrust);
        return thrust;
    }
    
    public double getCl(AeroSystemState state) {
        double cl = 4.5891 * state.getAngleOfAttack().getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180);
        state.setCl(cl);
        return cl;
    }
    
    public double getCd(AeroSystemState state) {
        double cd = 0.02 + ((getCl(state) * getCl(state)) / (Math.PI * 0.87 * 6.3));
        state.setCd(cd);
        return cd;
    }
    
    public double getCpm(AeroSystemState state) {
//        double cpm = -0.096 + -0.3735 * state.getAngleOfAttack().getMeasure(AngleType.RADIANS, MeasureRange.PlusMin180) +
//                (-8) * state.getAngularVelocity();
        double cpm = 0;
        state.setCpm(cpm);
        return cpm;
    }
    
}
