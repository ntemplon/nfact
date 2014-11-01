/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import aero.fluid.FluidState;
import geometry.angle.Angle;
import util.ArrayUtil;
import util.PhysicalConstants;

/**
 *
 * @author nathant
 */
public class AeroSystemState extends DynamicSystemState {

    // Constants
    public static final StateVariable<FluidState> FLUID_STATE = new StateVariable("Fluid State");
    public static final StateVariable<Double> CL = new StateVariable("CL");
    public static final StateVariable<Double> CD = new StateVariable("CD");
    public static final StateVariable<Double> CPM = new StateVariable("CPM");
    public static final StateVariable<Double> LIFT = new StateVariable("Lift");
    public static final StateVariable<Double> DRAG = new StateVariable("Drag");
    public static final StateVariable<Double> PITCHING_MOMENT = new StateVariable("Pitching Moment");
    public static final StateVariable<Double> THRUST = new StateVariable("Thrust");

    public static final StateVariable<Double> CPMQ = new StateVariable("CPM from Q");
    public static final StateVariable<Double> CPMT = new StateVariable("CPM from Thrust");
    public static final StateVariable<Double> CPMA = new StateVariable("CPM from Alpha");

    public static final StateVariable<Double> Q = new StateVariable("Q");
    public static final StateVariable<Double> MACH = new StateVariable("Mach");
    public static final StateVariable<Angle> FLIGHT_PATH_ANGLE = new StateVariable("Flight Path Angle");
    public static final StateVariable<Angle> ALPHA = new StateVariable("Alpha");
    
    public static final StateVariable<Double> NORMAL_LOAD_FACTOR = new StateVariable("Normal Load Factor");
    public static final StateVariable<Double> AXIAL_LOAD_FACTOR = new StateVariable("Axial Load Factor");

    public static final StateVariable[] AERO_VARIABLES = new StateVariable[]{
        FLUID_STATE, CL, CD, CPM, LIFT, DRAG, PITCHING_MOMENT, THRUST,
        CPMQ, CPMT, CPMA, Q, MACH, FLIGHT_PATH_ANGLE, ALPHA,
        NORMAL_LOAD_FACTOR, AXIAL_LOAD_FACTOR
    };


    // Properties
    /**
     *
     * @return the dynamic pressure of the current state, in pounds per square
     * foot
     */
    public double getDynamicPressure() {
        double q = 0.5 * this.get(FLUID_STATE).getDensity() * this.getSpeed() * this.getSpeed();
        this.set(Q, q);
        return q;
    }

    /**
     *
     * @return the mach number of the system state
     */
    public double getMachNumber() {
        double mach = this.getSpeed() / this.get(FLUID_STATE).getSpeedOfSound();
        this.set(MACH, mach);
        return mach;
    }

    /**
     *
     * @return the flight path angle
     */
    public Angle getFlightPathAngle() {
        Angle fpa = new Angle(this.get(Z_VEL) / this.get(X_VEL), Angle.TrigFunction.TANGENT);
        if (Double.isNaN(fpa.getMeasure())) {
            fpa = new Angle(Math.PI / 2);
        }
        this.set(FLIGHT_PATH_ANGLE, fpa);
        return fpa;
    }

    /**
     *
     * @return the angle of attack of the aircraft
     */
    public Angle getAngleOfAttack() {
        Angle alpha = new Angle(this.get(ANGULAR_POS).getMeasure() - this.getFlightPathAngle().getMeasure());
        this.set(ALPHA, alpha);
        return alpha;
//        return this.getAngularPosition().add(this.getFlightPathAngle().scalarMultiply(-1.0));
    }
    
    public double getAxialLoadFactor() {
        double xLoadFactor = this.get(X_ACCEL) / PhysicalConstants.GRAVITY_ACCELERATION;
        double zLoadFactor = (this.get(Z_ACCEL)  / PhysicalConstants.GRAVITY_ACCELERATION) + 1;
        Angle theta = this.get(ANGULAR_POS);
        
        double axialLoadFactor = xLoadFactor * theta.cos() + zLoadFactor * theta.sin();
        this.set(AeroSystemState.AXIAL_LOAD_FACTOR, axialLoadFactor);
        return axialLoadFactor;
    }
    
    public double getNormalLoadFactor() {
        double xLoadFactor = this.get(X_ACCEL) / PhysicalConstants.GRAVITY_ACCELERATION;
        double zLoadFactor = this.get(Z_ACCEL) / PhysicalConstants.GRAVITY_ACCELERATION;
        Angle theta = this.get(ANGULAR_POS);
        
        double normalLoadFactor = -1 * xLoadFactor * theta.sin() + zLoadFactor * theta.cos();
        this.set(AeroSystemState.NORMAL_LOAD_FACTOR, normalLoadFactor);
        return normalLoadFactor;
    }


    // Initialization
    public AeroSystemState() {
        super(AERO_VARIABLES);
    }

    public AeroSystemState(StateVariable[] variables) {
        super(ArrayUtil.concat(variables, AERO_VARIABLES));
    }

    public AeroSystemState(AeroSystemState other) {
        super(other);
    }

}
