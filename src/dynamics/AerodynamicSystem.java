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
import com.jupiter.ganymede.math.geometry.Angle.AngleType;
import com.jupiter.ganymede.math.geometry.Plane3;
import com.jupiter.ganymede.math.vector.Vector3;
import dynamics.airplane.WindModel;
import dynamics.analysis.InertiaModel;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import propulsion.PropulsionForceModel;
import util.PhysicalConstants;

/**
 *
 * @author Nathan Templon
 */
public class AerodynamicSystem extends DynamicSystem {

    // Constants
    public static final double ANGLE_CALCULATION_SPEED_THRESHOLD = 1;

    public static final StateVariable<FluidState> FLUID_STATE = new StateVariable<>("Fluid State");
    public static final StateVariable<Double> CL = new StateVariable<>("CL");
    public static final StateVariable<Double> CD = new StateVariable<>("CD");
    public static final StateVariable<Double> CSF = new StateVariable<>("CSF");
    public static final StateVariable<Double> CRM = new StateVariable<>("CRM");
    public static final StateVariable<Double> CPM = new StateVariable<>("CPM");
    public static final StateVariable<Double> CYM = new StateVariable<>("CYM");
    public static final StateVariable<Double> LIFT = new StateVariable<>("Lift");
    public static final StateVariable<Double> DRAG = new StateVariable<>("Drag");
    public static final StateVariable<Double> SIDE_FORCE = new StateVariable<>("Side Force");
    public static final StateVariable<Double> ROLLING_MOMENT = new StateVariable<>("Rolling Moment");
    public static final StateVariable<Double> PITCHING_MOMENT = new StateVariable<>("Pitching Moment");
    public static final StateVariable<Double> YAWING_MOMENT = new StateVariable<>("Yawing Moment");
    public static final StateVariable<Double> THRUST = new StateVariable<>("Thrust");

    public static final StateVariable<Double> Q_HAT = new StateVariable<>("Q Hat");

    public static final StateVariable<Double> CPM0 = new StateVariable<>("CPM0");
    public static final StateVariable<Double> CPMA = new StateVariable<>("CPM Alpha");
    public static final StateVariable<Double> CPM_FROM_Q = new StateVariable<>("CPM from Q");
    public static final StateVariable<Double> CPM_FROM_A = new StateVariable<>("CPM from Alpha");

    public static final StateVariable<Angle> DELTA_E = new StateVariable<>("Elevator Deflection");

    public static final StateVariable<Double> DYNAMIC_PRESSURE = new StateVariable<>("Q");
    public static final StateVariable<Double> MACH = new StateVariable<>("Mach");
    public static final StateVariable<Double> REYNOLDS = new StateVariable<>("Reynolds Number");
    public static final StateVariable<Angle> FLIGHT_PATH_ANGLE = new StateVariable<>("Flight Path Angle");
    public static final StateVariable<Angle> ANGLE_OF_ATTACK_GEOMETRIC = new StateVariable<>("Alpha Geo");
    public static final StateVariable<Angle> ANGLE_OF_ATTACK_TOTAL = new StateVariable<>("Alpha Total");
    public static final StateVariable<Angle> SIDESLIP_ANGLE = new StateVariable<>("Beta");
    public static final StateVariable<Angle> ROLL_ANGLE = new StateVariable<>("Phi (Roll)");

    public static final StateVariable<Double> ROLL_RATE = new StateVariable<>("Roll Rate");
    public static final StateVariable<Double> PITCH_RATE = new StateVariable<>("Pitch Rate");
    public static final StateVariable<Double> YAW_RATE = new StateVariable<>("Yaw Rate");

    public static final StateVariable<Double> NORMAL_LOAD_FACTOR = new StateVariable<>("Normal Load Factor");
    public static final StateVariable<Double> AXIAL_LOAD_FACTOR = new StateVariable<>("Axial Load Factor");

    public static final StateVariable<Vector3> BODY_X_AXIS = new StateVariable<>("Body X Axis");
    public static final StateVariable<Vector3> BODY_Y_AXIS = new StateVariable<>("Body Y Axis");
    public static final StateVariable<Vector3> BODY_Z_AXIS = new StateVariable<>("Body Z Axis");

    public static final StateVariable[] VECTOR_VARIABLES = {
        DynamicSystem.X_POS,
        DynamicSystem.X_VEL,
        DynamicSystem.Y_POS,
        DynamicSystem.Y_VEL,
        DynamicSystem.Z_POS,
        DynamicSystem.Z_VEL,
        DynamicSystem.PHI_POS,
        DynamicSystem.PHI_VEL,
        DynamicSystem.THETA_POS,
        DynamicSystem.THETA_VEL,
        DynamicSystem.PSI_POS,
        DynamicSystem.PSI_VEL
    };

    public static final Vector3 X_AXIS = new Vector3(1, 0, 0);
    public static final Vector3 Y_AXIS = new Vector3(0, 1, 0);
    public static final Vector3 Z_AXIS = new Vector3(0, 0, 1);

    public static final Plane3 XY_PLANE = new Plane3(Z_AXIS);
    public static final Plane3 XZ_PLANE = new Plane3(Y_AXIS);
    public static final Plane3 YZ_PLANE = new Plane3(X_AXIS);


    // Fields
    private final AerodynamicCoefficientModel model;
    private final AeroReferenceQuantities reference;
    private final PropulsionForceModel prop;
    private final InertiaModel inertiaModel;
    private final SystemState initialState;
    private final Fluid fluid;
    private final WindModel windModel;

    private boolean useLaunchRod = false;


    // Properties
    @Override
    public final StateVariable[] getVectorVariables() {
        return VECTOR_VARIABLES;
    }

    @Override
    public final SystemState getInitialState() {
        return this.initialState;
    }

    public final void setUseLaunchRod(boolean useLaunchRod) {
        this.useLaunchRod = useLaunchRod;
    }

    public final boolean getUseLaunchRod() {
        return this.useLaunchRod;
    }


    // Initialization
    public AerodynamicSystem(AerodynamicCoefficientModel model, AeroReferenceQuantities reference, PropulsionForceModel prop,
            InertiaModel inertiaModel, SystemState initialState, Fluid fluid, WindModel wind) {
        this.model = model;
        this.reference = reference;
        this.prop = prop;
        this.inertiaModel = inertiaModel;
        this.initialState = initialState;
        this.fluid = fluid;
        this.windModel = wind;
    }


    // Public Methods
    /**
     * Computes a step in a simulation by calculating the "delta vector" (velocities and accelerations) of the vehicle
     * in body axis.
     *
     * Important Assumptions: - The XZ plane is a symmetry plane.
     *
     * Factors Taken Into Account: - Inertial Coupling - Nonlinear Aerodynamics
     *
     * @param time        The time since simulation start for this computational step (used for motor force calculations, etc.)
     * @param stateVector The state vector (positions and velocities) at the start of this time step;
     *
     * @return Returns the results of the computational step
     */
    @Override
    public final ComputeStepResults computeStep(double time, Vector stateVector) {
        Vector3 wind = this.windModel.getWind(time);

        Map<SystemProperty, Object> props = new LinkedHashMap<>();

        // Copy State Vector values to the properities map
        for (int i = 0; i < this.getVectorVariables().length; i++) {
            props.put(this.getVectorVariables()[i], stateVector.getComponent(i + 1));
        }

        // Vehicle Axes
        Angle theta = new Angle((Double) props.get(DynamicSystem.THETA_POS));
        Angle psi = new Angle((Double) props.get(DynamicSystem.PSI_POS));
        Angle phi = new Angle((Double) props.get(DynamicSystem.PHI_POS));
        Matrix phiRotation = Matrix.euler3(phi, 1);
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

        // Speed
        double xVelocity = (Double) props.get(DynamicSystem.X_VEL);
        double yVelocity = (Double) props.get(DynamicSystem.Y_VEL);
        double zVelocity = (Double) props.get(DynamicSystem.Z_VEL);
        Vector3 velocity = new Vector3(xVelocity, yVelocity, zVelocity);
        double speed = velocity.norm();
        Vector3 airVelocity = velocity.minus(wind); // Velocity of plane wrt air
        double airspeed = airVelocity.norm();
        props.put(DynamicSystem.SPEED, speed);

        // Angular Rates
        double xRotateRate = (Double) props.get(DynamicSystem.PHI_VEL);
        double yRotateRate = (Double) props.get(DynamicSystem.THETA_VEL);
        double zRotateRate = (Double) props.get(DynamicSystem.PSI_VEL);
        Vector3 earthRotations = new Vector3(xRotateRate, yRotateRate, zRotateRate);

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
            if (Double.isNaN(gamma.getMeasure())) {
                if (zVelocity > 0.0) {
                    gamma = new Angle(90.0, AngleType.DEGREES);
                }
                else {
                    gamma = new Angle(-90.0, AngleType.DEGREES);
                }
            }

            alpha = airVelocity.angleTo(bodyXYPlane);
            if (airVelocity.dot(bodyXAxis) < 0) {
                alpha = (new Angle(Math.PI)).plus(alpha.times(-1.0));
            }
            if (airVelocity.dot(bodyZAxis) < 0) {
                alpha = alpha.times(-1.0);
            }

            beta = airVelocity.angleTo(bodyXZPlane);
            if (airVelocity.dot(bodyYAxis) < 0) {
                beta = beta.times(-1.0);
            }
        }
        props.put(AerodynamicSystem.FLIGHT_PATH_ANGLE, gamma);
        props.put(AerodynamicSystem.ANGLE_OF_ATTACK_GEOMETRIC, alpha);
        props.put(AerodynamicSystem.SIDESLIP_ANGLE, beta);

        // The addition of PI corrects for the disparity between body axis and gravity axis.
        props.put(AerodynamicSystem.ROLL_ANGLE, phi.plus(new Angle(Math.PI)));

        // Wind Axis (wrt Gravity Axis)
        Vector3 xWind = airVelocity.getUnitVector();
        Vector3 yWind = airVelocity.cross(bodyXYPlane.getNormal()).getUnitVector();
        if (yWind.dot(bodyYAxis) < 0) {
            yWind = yWind.times(-1);
        }
        Vector3 zWind = xWind.cross(yWind);
        Matrix windToEarth = new Matrix(new double[][]{
            {xWind.getComponent(1), yWind.getComponent(1), zWind.getComponent(1)},
            {xWind.getComponent(2), yWind.getComponent(2), zWind.getComponent(2)},
            {xWind.getComponent(3), yWind.getComponent(3), zWind.getComponent(3)}
        });

        // Body Axis Rotation Matrix
        Matrix earthToBody = bodyToEarth.inverse();

        // Body Axis Rates
        Vector bodyAxisVelocity;
        Vector bodyAxisRotation;
        bodyAxisVelocity = earthToBody.times(airVelocity);
        bodyAxisRotation = earthToBody.times(earthRotations);
        double ue = bodyAxisVelocity.getComponent(1);
        double ve = bodyAxisVelocity.getComponent(2);
        double we = bodyAxisVelocity.getComponent(3);
        double rollRate = bodyAxisRotation.getComponent(1);
        double pitchRate = bodyAxisRotation.getComponent(2);
        double yawRate = bodyAxisRotation.getComponent(3);
        props.put(ROLL_RATE, rollRate);
        props.put(PITCH_RATE, pitchRate);
        props.put(YAW_RATE, yawRate);

        // Nondimensional Rates
        double nonDimPitchRate = (pitchRate * this.reference.getChord()) / (2.0 * airspeed);
        props.put(Q_HAT, nonDimPitchRate);

        // Aerodynamic Coefficients
        SystemState preForceState = new SystemState(time, stateVector, props);
        double cl = this.model.cl(preForceState);
        double cd = this.model.cd(preForceState);
        double csf = this.model.csf(preForceState);
        double crm = this.model.crm(preForceState);
        double cpm = this.model.cpm(preForceState);
        double cym = this.model.cym(preForceState);
        props.put(CL, cl);
        props.put(CD, cd);
        props.put(CSF, csf);
        props.put(CRM, crm);
        props.put(CPM, cpm);
        props.put(CYM, cym);

        // Aerodynamic Forces
        double lift = q * this.reference.getArea() * cl;
        double drag = q * this.reference.getArea() * cd;
        double sideForce = q * this.reference.getArea() * csf;
        double pitchingMoment = q * this.reference.getArea() * this.reference.getChord() * cpm;
        double rollingMoment = q * this.reference.getArea() * this.reference.getSpan() * crm;
        double yawingMoment = q * this.reference.getArea() * this.reference.getSpan() * cym;
        props.put(LIFT, lift);
        props.put(DRAG, drag);
        props.put(SIDE_FORCE, sideForce);
        props.put(ROLLING_MOMENT, rollingMoment);
        props.put(PITCHING_MOMENT, pitchingMoment);
        props.put(YAWING_MOMENT, yawingMoment);

        // Body Axis Loads
        //  Since we have wind axis loads, we will find these by converting Wind Axis loads to Earth Axis,
        //  then converting to body
        Vector earthForces = windToEarth.times(new Vector(-1.0 * drag, sideForce, -1.0 * lift));
        Vector earthMoments = windToEarth.times(new Vector(rollingMoment, pitchingMoment, yawingMoment));
        Vector bodyForces = earthToBody.times(earthForces);
        Vector bodyMoments = earthToBody.times(earthMoments);
        double xForce = bodyForces.getComponent(1);
        double yForce = bodyForces.getComponent(2);
        double zForce = bodyForces.getComponent(3);
        double xMoment = bodyMoments.getComponent(1);
        double yMoment = bodyMoments.getComponent(2);
        double zMoment = bodyMoments.getComponent(3);

        // Propulsion
        double thrust = this.prop.thrust(preForceState);
        xForce += thrust;
        props.put(THRUST, thrust);

        // Inertia Fetching
        Inertia inertia = this.inertiaModel.getInertia(time);
        final double mass = inertia.getMass();
        props.put(DynamicSystem.INERTIA, inertia);
        props.put(DynamicSystem.MASS, mass);

        // Velocity Accelerations
        final double g = PhysicalConstants.GRAVITY_ACCELERATION;
        double ueDot = xForce / mass - g * theta.sin() - pitchRate * we + yawRate * ve;
        double veDot = yForce / mass + g * theta.cos() * phi.sin() - yawRate * ue + rollRate * we;
        double weDot = zForce / mass + g * theta.cos() * phi.cos() - rollRate * ve + pitchRate * ue;

        // Uncoupled Rotations
        final double ix = inertia.getIxx();
        final double iy = inertia.getIyy();
        final double iz = inertia.getIzz();
        final double izx = inertia.getIxz();
        double pitchRateDot = (yMoment - yawRate * rollRate * (ix - iz) + izx * (rollRate * rollRate - yawRate * yawRate)) / iy;

        // Coupled Rotations - Use a Matrix to solve
        double a1 = ix;
        double a2 = -1.0 * izx;
        double a3 = xMoment - pitchRate * yawRate * (iz - iy) + izx * rollRate * pitchRate;
        double b1 = -1.0 * izx;
        double b2 = iz;
        double b3 = zMoment - rollRate * pitchRate * (iy - ix) - izx * pitchRate * yawRate;

        double yawRateDot = (b3 - (b1 * a3) / a1) / (b2 - (b1 * a2) / a1);
        double rollRateDot = (a3 - a2 * yawRateDot) / a1;

        // Load Factors
        double nAxial = xForce / (mass * PhysicalConstants.GRAVITY_ACCELERATION);
        // -1 is because force is positive down, but load factor is positive up
        double nNormal = -1.0 * zForce / (mass * PhysicalConstants.GRAVITY_ACCELERATION);
        props.put(AerodynamicSystem.AXIAL_LOAD_FACTOR, nAxial);
        props.put(AerodynamicSystem.NORMAL_LOAD_FACTOR, nNormal);

        // To Earth Axis
        Vector deltaPosition = bodyToEarth.times(new Vector3(ue, ve, we));
        Vector deltaVelocity = bodyToEarth.times(new Vector3(ueDot, veDot, weDot));

        // Matrix from Etkin (he calls it 'T')
        Matrix rotationToEarth = new Matrix(new double[][]{
            {1.0, phi.sin() * theta.tan(), phi.cos() * theta.tan()},
            {0.0, phi.cos(), -1.0 * phi.sin()},
            {0.0, phi.sin() * theta.sec(), phi.cos() * theta.sec()}
        });
        
        double[][] angleVelocityCalcItems = new double[][] {
            {1.0, 0.0, -theta.sin(), rollRate},
            {0.0, phi.cos(), phi.sin() * theta.cos(), pitchRate},
            {0.0, -1.0 * phi.sin(), phi.cos() * theta.cos(), yawRate}
        };
        for (double[] angleVelocityCalcItem : angleVelocityCalcItems) {
            for (int j = 0; j < angleVelocityCalcItems[0].length; j++) {
                if (Math.abs(angleVelocityCalcItem[j]) < 1e-8) {
                    angleVelocityCalcItem[j] = 0.0;
                }
            }
        }
        if (theta.sec() > 100) {
            angleVelocityCalcItems[2][2] = 0.0;
        }
        Matrix angleVelocityCalc = new Matrix(angleVelocityCalcItems);
        Matrix angleVelocityResult = angleVelocityCalc.rref();
        
        double[][] angleAccelerationCalcItems = new double[][] {
            {1.0, 0.0, -theta.sin(), rollRateDot},
            {0.0, phi.cos(), phi.sin() * theta.cos(), pitchRateDot},
            {0.0, -1.0 * phi.sin(), phi.cos() * theta.cos(), yawRateDot}
        };
        for (double[] angleAccelerationCalcItem : angleAccelerationCalcItems) {
            for (int j = 0; j < angleAccelerationCalcItems[0].length; j++) {
                if (Math.abs(angleAccelerationCalcItem[j]) < 1e-8) {
                    angleAccelerationCalcItem[j] = 0.0;
                }
            }
        }
        if (theta.sec() > 100) {
            angleAccelerationCalcItems[2][2] = 0.0;
        }
        Matrix angleAccelerationCalc = new Matrix(angleAccelerationCalcItems);
        Matrix angleAccelerationResult = angleAccelerationCalc.rref();

        // Account for theta = 90 deg breaking things
//        if (theta.tan() > 1e3 || theta.sec() > 1e3) {
//            rotationToEarth = new Matrix(new double[][]{
//                {1.0, phi.sin() * theta.tan(), phi.cos() * theta.tan()},
//                {0.0, phi.cos(), -1.0 * phi.sin()},
//                {0.0, phi.sin() * theta.sec(), phi.cos() * theta.sec()}
//            });
//        }

//        Vector deltaRotationPosition = rotationToEarth.times(new Vector3(rollRate, pitchRate, yawRate));
//        Vector deltaRotationVelocity = rotationToEarth.times(new Vector3(rollRateDot, pitchRateDot, yawRateDot));
        Vector deltaRotationPosition = angleVelocityResult.column(4);
        Vector deltaRotationVelocity = angleAccelerationResult.column(4);

        // Launch Rod
        if (this.getUseLaunchRod() && Math.abs((Double) props.get(DynamicSystem.Z_POS)) < 4) {
            deltaPosition = new Vector3(0.0, 0.0, deltaPosition.getComponent(3));
            deltaVelocity = new Vector3(0.0, 0.0, deltaVelocity.getComponent(3));
            deltaRotationPosition = new Vector3(0.0, 0.0, 0.0);
            deltaRotationVelocity = new Vector3(0.0, 0.0, 0.0);
        }
        
        // Check for and Remove NaN's
        for (int i = 1; i <= deltaRotationVelocity.getDimension(); i++) {
            if (Double.isNaN(deltaRotationVelocity.getComponent(i))) {
                double[] newValues = new double[deltaRotationVelocity.getDimension()];
                for (int j = 0; j < deltaRotationVelocity.getDimension(); j++) {
                    newValues[j] = deltaRotationVelocity.getComponent(j + 1);
                }
                newValues[i - 1] = 0.0;
                deltaRotationVelocity = new Vector(newValues);
                System.out.println("NaN corrected for rotation component #" + i + ".");
            }
        }

        // Put Final Accelerations
        props.put(X_ACCEL, deltaVelocity.getComponent(1));
        props.put(Z_ACCEL, deltaVelocity.getComponent(3));
        props.put(THETA_ACCEL, deltaRotationVelocity.getComponent(2));

        Vector delta = new Vector(
                deltaPosition.getComponent(1), // X Velocity
                deltaVelocity.getComponent(1), // X Acceleration
                deltaPosition.getComponent(2), // Y Velocity
                deltaVelocity.getComponent(2), // Y Acceleration
                deltaPosition.getComponent(3), // Z Velocity
                deltaVelocity.getComponent(3), // Z Acceleration
                deltaRotationPosition.getComponent(1), // Phi Velocity
                deltaRotationVelocity.getComponent(1), // Phi Roll Angle
                deltaRotationPosition.getComponent(2), // Theta Velocity
                deltaRotationVelocity.getComponent(2), // Theta Acceleration
                deltaRotationPosition.getComponent(3), // Psi Velocity
                deltaRotationVelocity.getComponent(3) // Psi Acceleration
        );

        SystemState finalState = new SystemState(time, stateVector, props);

        // Check for NaN's
        boolean foundNaN = false;
        for (SystemProperty curProp : props.keySet()) {
            Object val = props.get(curProp);
            if (val instanceof Double) {
                if (Double.isNaN((Double) val)) {
                    System.out.println("NaN: " + curProp.getName());
                    foundNaN = true;
                }
            }
            else if (val instanceof Angle) {
                if (Double.isNaN(((Angle) val).getMeasure())) {
                    System.out.println("NaN: " + curProp.getName());
                    foundNaN = true;
                }
            }
        }
        if (foundNaN) {
            System.out.println(time);
            System.out.println();
        }

        return new ComputeStepResults(finalState, delta);
    }

    public FluidState getFluidState(double time, Vector stateVector) {
        return new IdealGasState(
                (IdealGas) this.fluid, // Fluid
                99.0 + 459.0, // Temperature (Rankine)
                2011.33);               // Pressure (psf)
    }

}
