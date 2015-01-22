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

import aero.fluid.FluidState;
import com.jupiter.ganymede.math.matrix.Matrix;
import com.jupiter.ganymede.math.vector.Vector;
import dynamics.analysis.SystemState;
import com.jupiter.ganymede.math.geometry.Angle;
import com.jupiter.ganymede.math.geometry.Angle.AngleType;
import com.jupiter.ganymede.math.geometry.Plane3;
import com.jupiter.ganymede.math.vector.Vector3;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nathan Templon
 */
public abstract class AerodynamicSystem extends DynamicSystem {

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

    public static final StateVariable[] VECTOR_VARIABLES = {DynamicSystem.X_POS, DynamicSystem.X_VEL, DynamicSystem.X_ACCEL,
        DynamicSystem.Y_POS, DynamicSystem.Y_VEL, DynamicSystem.Y_ACCEL, DynamicSystem.Z_POS, DynamicSystem.Z_VEL, DynamicSystem.Z_ACCEL,
        DynamicSystem.PHI_POS, DynamicSystem.PHI_VEL, DynamicSystem.PHI_ACCEL, DynamicSystem.THETA_POS, DynamicSystem.THETA_VEL,
        DynamicSystem.THETA_ACCEL, DynamicSystem.PSI_POS, DynamicSystem.PSI_VEL, DynamicSystem.PSI_ACCEL};
    
    public static final Plane3 XY_PLANE = new Plane3(new Vector3(0, 0, 1));
    public static final Plane3 XZ_PLANE = new Plane3(new Vector3(0, 1, 0));
    public static final Plane3 YZ_PLANE = new Plane3(new Vector3(1, 0, 0));


    // Properties
    @Override
    public final StateVariable[] getVectorVariables() {
        return VECTOR_VARIABLES;
    }
    
    public abstract double getReferenceLength();


    // Initialization
    public AerodynamicSystem() {

    }


    // Public Methods
    @Override
    public final SystemState buildState(double time, Vector stateVector) {
        Map<SystemProperty, Object> props = new HashMap<>();
        
        // Copy State Vector values to the properities map
        for (int i = 0; i < this.getVectorVariables().length; i++) {
            props.put(this.getVectorVariables()[i], stateVector.getComponent(i + 1));
        }
        
        // Vehicle Axes
        Angle theta = new Angle((Double)props.get(DynamicSystem.THETA_POS));
        Angle psi = new Angle((Double)props.get(DynamicSystem.PSI_POS));
        Angle phi = new Angle((Double)props.get(DynamicSystem.PHI_POS));
        
        // Calculate Derived Parameters
        
        // Inertial properties
        props.put(DynamicSystem.INERTIA, this.getInertia(time));
        
        // Speed
        double xVelocity = (Double)props.get(DynamicSystem.X_VEL);
        double yVelocity = (Double)props.get(DynamicSystem.X_VEL);
        double zVelocity = (Double)props.get(DynamicSystem.X_VEL);
        Vector3 velocity = new Vector3(xVelocity, yVelocity, zVelocity);
        double speed = velocity.norm();
        props.put(DynamicSystem.SPEED, speed);
        
        // Fluid State and Flow Properties
        FluidState fluid = this.getFluidState(time, stateVector);
        props.put(AerodynamicSystem.FLUID_STATE, fluid);
        double mach = speed / fluid.getSpeedOfSound();
        props.put(AerodynamicSystem.MACH, mach);
        double density = fluid.getDensity();
        double q = 0.5 * speed * speed * density;
        props.put(AerodynamicSystem.DYNAMIC_PRESSURE, q);
        double reynolds = (this.getReferenceLength() * fluid.getDensity() * speed) / fluid.getViscosity();
        props.put(AerodynamicSystem.REYNOLDS, reynolds);
        
        // Aerodynamic Angles
        Angle gamma = new Angle(0.0);
        Angle alpha = new Angle(0.0);
        Angle beta = new Angle(0.0);
        if (speed > ANGLE_CALCULATION_SPEED_THRESHOLD) {
            // angle calculation code
            gamma = velocity.angleTo(velocity.projectionOnto(XY_PLANE));
            if (zVelocity < 0) {
                gamma = gamma.times(-1.0);
            }
//            gamma = velocity.angleTo(new Vector(0, 0, -1)).plus(new Angle(-90, AngleType.DEGREES));
//            alpha = (new Angle((Double)props.get(DynamicSystem.THETA_POS))).plus(gamma.times(-1));
        }
        props.put(AerodynamicSystem.FLIGHT_PATH_ANGLE, gamma);
        props.put(AerodynamicSystem.ANGLE_OF_ATTACK, alpha);
        props.put(AerodynamicSystem.SIDESLIP_ANGLE, beta);
        props.put(AerodynamicSystem.ROLL_ANGLE, phi);
        
        // Aerodynamic Coefficients
        // -- code
        
        
        return new SystemState(time, stateVector, props);
    }

    @Override
    public final Matrix getSystemMatrix(SystemState state) {
        double[][] matrix = new double[state.getStateVector().getDimension()][state.getStateVector().getDimension()];
        
        // Stuff here
        
        return new Matrix(matrix);
    }
    
    
    // Aerodynamic System Required Methods
    @Override
    public abstract Inertia getInertia(double time);
    public abstract FluidState getFluidState(double time, Vector stateVector);

}
