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
package dynamics;

import aero.AeroReferenceQuantities;
import aero.AerodynamicCoefficientModel;
import aero.fluid.Fluid;
import aero.fluid.FluidState;
import aero.fluid.IdealGas;
import aero.fluid.IdealGasState;
import com.jupiter.ganymede.math.matrix.Matrix;
import com.jupiter.ganymede.math.vector.Vector;
import com.jupiter.ganymede.math.geometry.Angle;
import com.jupiter.ganymede.math.geometry.Plane3;
import com.jupiter.ganymede.math.vector.Vector3;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Nathan Templon
 */
public class AerodynamicSystem extends DynamicSystem {

    // Constants
    public static final double ANGLE_CALCULATION_SPEED_THRESHOLD = 5;

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

    public static final StateVariable<Angle> DELTA_E = new StateVariable("Elevator Deflection");

    public static final StateVariable<Double> DYNAMIC_PRESSURE = new StateVariable<>("Q");
    public static final StateVariable<Double> MACH = new StateVariable<>("Mach");
    public static final StateVariable<Double> REYNOLDS = new StateVariable<>("Reynolds Number");
    public static final StateVariable<Angle> FLIGHT_PATH_ANGLE = new StateVariable<>("Flight Path Angle");
    public static final StateVariable<Angle> ANGLE_OF_ATTACK = new StateVariable<>("Alpha");
    public static final StateVariable<Angle> SIDESLIP_ANGLE = new StateVariable<>("Beta");
    public static final StateVariable<Angle> ROLL_ANGLE = new StateVariable<>("Phi");

    public static final StateVariable<Double> NORMAL_LOAD_FACTOR = new StateVariable<>("Normal Load Factor");
    public static final StateVariable<Double> AXIAL_LOAD_FACTOR = new StateVariable<>("Axial Load Factor");
    
    public static final StateVariable<Vector3> BODY_X_AXIS = new StateVariable<>("Body X Axis");
    public static final StateVariable<Vector3> BODY_Y_AXIS = new StateVariable<>("Body Y Axis");
    public static final StateVariable<Vector3> BODY_Z_AXIS = new StateVariable<>("Body Z Axis");

    public static final StateVariable[] VECTOR_VARIABLES = {DynamicSystem.X_POS, DynamicSystem.X_VEL,
        DynamicSystem.Y_POS, DynamicSystem.Y_VEL, DynamicSystem.Z_POS, DynamicSystem.Z_VEL, DynamicSystem.PHI_POS,
        DynamicSystem.PHI_VEL, DynamicSystem.THETA_POS, DynamicSystem.THETA_VEL, DynamicSystem.PSI_POS,
        DynamicSystem.PSI_VEL};

    public static final Vector3 X_AXIS = new Vector3(1, 0, 0);
    public static final Vector3 Y_AXIS = new Vector3(0, 1, 0);
    public static final Vector3 Z_AXIS = new Vector3(0, 0, 1);

    public static final Plane3 XY_PLANE = new Plane3(Z_AXIS);
    public static final Plane3 XZ_PLANE = new Plane3(Y_AXIS);
    public static final Plane3 YZ_PLANE = new Plane3(X_AXIS);


    // Fields
    private final AerodynamicCoefficientModel model;
    private final AeroReferenceQuantities reference;
    private final SystemState initialState;
    private final Fluid fluid;


    // Properties
    @Override
    public final StateVariable[] getVectorVariables() {
        return VECTOR_VARIABLES;
    }
    
    @Override
    public final SystemState getInitialState() {
        return this.initialState;
    }


    // Initialization
    public AerodynamicSystem(AerodynamicCoefficientModel model, AeroReferenceQuantities reference, SystemState initialState, Fluid fluid) {
        this.model = model;
        this.reference = reference;
        this.initialState = initialState;
        this.fluid = fluid;
    }


    // Public Methods
    @Override
    public final SystemState buildState(double time, Vector stateVector) {
        Vector3 wind = new Vector3(0, 0, 0);

        Map<SystemProperty, Object> props = new LinkedHashMap<>();

        // Copy State Vector values to the properities map
        for (int i = 0; i < this.getVectorVariables().length; i++) {
            props.put(this.getVectorVariables()[i], stateVector.getComponent(i + 1));
        }

        // Vehicle Axes
        Angle theta = new Angle((Double) props.get(DynamicSystem.THETA_POS));
        Angle psi = new Angle((Double) props.get(DynamicSystem.PSI_POS));
        Angle phi = new Angle((Double) props.get(DynamicSystem.PHI_POS));
        Matrix phiRotation = Matrix.euler3(phi.plus(new Angle(Math.PI)), 1);
        Matrix thetaRotation = Matrix.euler3(theta, 2);
        Matrix psiRotation = Matrix.euler3(psi, 3);
        Matrix bodyToEarth = phiRotation.times(thetaRotation).times(psiRotation);
        Vector3 bodyXAxis = Vector3.fromVector(bodyToEarth.column(1));
        Vector3 bodyYAxis = Vector3.fromVector(bodyToEarth.column(2));
        Vector3 bodyZAxis = Vector3.fromVector(bodyToEarth.column(3));
        props.put(BODY_X_AXIS, bodyXAxis);
        props.put(BODY_Y_AXIS, bodyYAxis);
        props.put(BODY_Z_AXIS, bodyZAxis);
        Plane3 bodyXYPlane = new Plane3(bodyZAxis);
        Plane3 bodyXZPlane = new Plane3(bodyYAxis);
        Plane3 bodyYZPlane = new Plane3(bodyXAxis);

        // Inertial properties
        props.put(DynamicSystem.INERTIA, this.getInertia(time));

        // Speed
        double xVelocity = (Double) props.get(DynamicSystem.X_VEL);
        double yVelocity = (Double) props.get(DynamicSystem.Y_VEL);
        double zVelocity = (Double) props.get(DynamicSystem.Z_VEL);
        Vector3 velocity = new Vector3(xVelocity, yVelocity, zVelocity);
        double speed = velocity.norm();
        Vector3 airVelocity = velocity.minus(wind);
        double airspeed = airVelocity.norm();
        props.put(DynamicSystem.SPEED, speed);

        // Fluid State and Flow Properties
        FluidState fluidState = this.getFluidState(time, stateVector);
        props.put(AerodynamicSystem.FLUID_STATE, fluidState);
        double mach = speed / fluidState.getSpeedOfSound();
        props.put(AerodynamicSystem.MACH, mach);
        double density = fluidState.getDensity();
        double q = 0.5 * airspeed * airspeed * density;
        props.put(AerodynamicSystem.DYNAMIC_PRESSURE, q);
        double reynolds = (this.reference.getChord() * fluidState.getDensity() * airspeed) / fluidState.getViscosity();
        props.put(AerodynamicSystem.REYNOLDS, reynolds);

        // Aerodynamic Angles
        Angle gamma = new Angle(0.0);
        Angle alpha = new Angle(0.0);
        Angle beta = new Angle(0.0);
        if (speed > ANGLE_CALCULATION_SPEED_THRESHOLD) {
            // angle calculation code
            gamma = velocity.angleTo(XY_PLANE);
            if (zVelocity < 0) {
                gamma = gamma.times(-1.0);
            }

            alpha = airVelocity.angleTo(bodyXYPlane);
            if (theta.times(-1).getMeasure(Angle.MeasureRange.PlusMinus180) < gamma.getMeasure(Angle.MeasureRange.PlusMinus180)) {
                alpha = alpha.times(-1.0);
            }

            beta = airVelocity.angleTo(bodyXZPlane);
            if (airVelocity.dot(bodyYAxis) < 0) {
                beta = beta.times(-1.0);
            }
        }
        props.put(AerodynamicSystem.FLIGHT_PATH_ANGLE, gamma);
        props.put(AerodynamicSystem.ANGLE_OF_ATTACK, alpha);
        props.put(AerodynamicSystem.SIDESLIP_ANGLE, beta);
        props.put(AerodynamicSystem.ROLL_ANGLE, phi);

        return new SystemState(time, stateVector, props);
    }

    @Override
    /**
     * Acceleration Vector in GravityAxis
     */
    public final Vector getAccelerationVector(SystemState state) {
        // Fetch Parameters
        double reynolds = state.get(AerodynamicSystem.REYNOLDS);
        double q = state.get(AerodynamicSystem.DYNAMIC_PRESSURE);
        Angle alpha = state.get(AerodynamicSystem.ANGLE_OF_ATTACK);
        Angle beta = state.get(AerodynamicSystem.SIDESLIP_ANGLE);
        Inertia inertia = state.get(DynamicSystem.INERTIA);
        
        // Aerodynamic Forces
        double lift = q * this.reference.getArea() * this.model.cl(state);
        double drag = q * this.reference.getArea() * this.model.cd(state);
        double sideForce = q * this.reference.getArea() * this.model.csf(state);
        double pitchingMoment = q * this.reference.getArea() * this.reference.getChord() * this.model.cpm(state);
        double rollingMoment = q * this.reference.getArea() * this.reference.getSpan() * this.model.crm(state);
        double yawingMoment = q * this.reference.getArea() * this.reference.getSpan() * this.model.cym(state);
        
        return new Vector();
    }


    // Aerodynamic System Required Methods
    @Override
    public Inertia getInertia(double time) {
        return null;
    }

    public FluidState getFluidState(double time, Vector stateVector) {
        return new IdealGasState(
                (IdealGas)this.fluid,   // Fluid
                99.0 + 459.0,           // Temperature (Rankine)
                2011.33);               // Pressure (psf)
    }

}
