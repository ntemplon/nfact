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
        state.setZVelocity(5);
        
        FluidState fluid = new IdealGasState(new IdealGas(28.97, 1.4), 95 + 459, 2116 * 0.95);
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
    public double getThrust(double time) {
        return this.engine.getThrustAt(time);
    }
    
    public double getCl(AeroSystemState state) {
        return 4.5891 * state.getAngleOfAttack().measure(AngleType.RADIANS, MeasureRange.PlusMin180);
    }
    
    public double getCd(AeroSystemState state) {
        return 0.02 + ((getCl(state) * getCl(state)) / (Math.PI * 0.87 * 6.3));
    }
    
    public double getCpm(AeroSystemState state) {
        return -0.096 + -0.3735 * state.getAngleOfAttack().measure(AngleType.RADIANS, MeasureRange.PlusMin180);
    }
    
}
