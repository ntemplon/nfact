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

    public static final DerivedProperty<AeroSystemState, Double> DYNAMIC_PRESSURE = new DerivedProperty<>("Q", new StateFunction<>(
            (AeroSystemState state) -> getDynamicPressure(state),
            new SystemProperty[]{FLUID_STATE, SPEED}));

    public static final DerivedProperty<AeroSystemState, Double> MACH = new DerivedProperty<>("Mach", new StateFunction<>(
            (AeroSystemState state) -> getMach(state),
            new SystemProperty[]{SPEED, FLUID_STATE}));

    public static final DerivedProperty<AeroSystemState, Angle> FLIGHT_PATH_ANGLE = new DerivedProperty<>("Flight Path Angle", new StateFunction<>(
            (AeroSystemState state) -> getFlightPathAngle(state),
            new SystemProperty[]{Z_VEL, X_VEL}));

    public static final DerivedProperty<AeroSystemState, Angle> ALPHA = new DerivedProperty<>("Alpha", new StateFunction<>(
            (AeroSystemState state) -> getAngleOfAttack(state),
            new SystemProperty[]{ANGULAR_POS, FLIGHT_PATH_ANGLE}));

    public static final DerivedProperty<AeroSystemState, Double> NORMAL_LOAD_FACTOR = new DerivedProperty<>("Normal Load Factor", new StateFunction<>(
            (AeroSystemState state) -> getNormalLoadFactor(state),
            new SystemProperty[]{X_ACCEL, Z_ACCEL, ANGULAR_POS}));
    
    public static final DerivedProperty<AeroSystemState, Double> AXIAL_LOAD_FACTOR = new DerivedProperty<>("Axial Load Factor", new StateFunction<>(
            (AeroSystemState state) -> getAxialLoadFactor(state),
            new SystemProperty[]{X_ACCEL, Z_ACCEL, ANGULAR_POS}));

    public static final SystemProperty[] AERO_VARIABLES = new SystemProperty[]{
        FLUID_STATE, CL, CD, CPM, LIFT, DRAG, PITCHING_MOMENT, THRUST,
        CPMQ, CPMT, CPMA, DYNAMIC_PRESSURE, MACH, FLIGHT_PATH_ANGLE, ALPHA,
        NORMAL_LOAD_FACTOR, AXIAL_LOAD_FACTOR
    };


    // Static Methods
    private static double getDynamicPressure(AeroSystemState state) {
        return 0.5 * state.get(FLUID_STATE).getDensity() * state.get(SPEED) * state.get(SPEED);
    }

    private static double getMach(AeroSystemState state) {
        return state.get(SPEED) / state.get(FLUID_STATE).getSpeedOfSound();
    }

    private static Angle getFlightPathAngle(AeroSystemState state) {
        Angle fpa = new Angle(Math.PI / 2);
        try {
            fpa = new Angle(state.get(Z_VEL) / state.get(X_VEL), Angle.TrigFunction.TANGENT);
        } catch (Exception ex) {

        }
        if (Double.isNaN(fpa.getMeasure())) {
            fpa = new Angle(Math.PI / 2);
        }
        return fpa;
    }
    
    private static Angle getAngleOfAttack(AeroSystemState state) {
        return new Angle(state.get(ANGULAR_POS).getMeasure() - state.get(FLIGHT_PATH_ANGLE).getMeasure());
    }
    
    private static double getAxialLoadFactor(AeroSystemState state) {
        double xLoadFactor = state.get(X_ACCEL) / PhysicalConstants.GRAVITY_ACCELERATION;
        double zLoadFactor = (state.get(Z_ACCEL) / PhysicalConstants.GRAVITY_ACCELERATION) + 1;
        Angle theta = state.get(ANGULAR_POS);

        double axialLoadFactor = xLoadFactor * theta.cos() + zLoadFactor * theta.sin();
        return axialLoadFactor;
    }

    public static double getNormalLoadFactor(AeroSystemState state) {
        double xLoadFactor = state.get(X_ACCEL) / PhysicalConstants.GRAVITY_ACCELERATION;
        double zLoadFactor = state.get(Z_ACCEL) / PhysicalConstants.GRAVITY_ACCELERATION;
        Angle theta = state.get(ANGULAR_POS);

        double normalLoadFactor = -1 * xLoadFactor * theta.sin() + zLoadFactor * theta.cos();
        return normalLoadFactor;
    }
    
    
    // Initialization
    public AeroSystemState() {
        super(AERO_VARIABLES);
    }

    public AeroSystemState(SystemProperty[] variables) {
        super(ArrayUtil.concat(variables, AERO_VARIABLES));
    }

    public AeroSystemState(AeroSystemState other) {
        super(other);
    }

}
