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

import geometry.angle.Angle;
import util.PhysicalConstants;

/**
 *
 * @author nathant
 */
public abstract class AerodynamicSystem implements DynamicSystem<AeroSystemState> {

    // Fields
    private AeroSystemState state;
    private double time;


    // Properties
    @Override
    public AeroSystemState getState() {
        if (this.state == null) {
            this.state = this.getInitialState();
        }
        return this.state;
    }

    /**
     *
     * @param state
     * @return the mass of the object in slugs
     */
    public abstract double getMass(AeroSystemState state);

    /**
     *
     * @param state
     * @return the moment of inertia of the system about it's own y axis (out
     * the right wing), in slug-square feet
     */
    public abstract double getIyy(AeroSystemState state);

    public abstract AeroSystemState getInitialState();


    // Initialization
    public AerodynamicSystem() {
        this.time = 0.0;
    }


    // Public Methods
    /**
     *
     * @param state the current state
     * @return the lift of the system in pounds
     */
    public abstract double getLift(AeroSystemState state);

    /**
     *
     * @param state the current state
     * @return the drag of the system in pounds
     */
    public abstract double getDrag(AeroSystemState state);

    /**
     *
     * @param state the current state
     * @return the pitching moment of the system in foot-pounds
     */
    public abstract double getPitchingMoment(AeroSystemState state);

    /**
     *
     * @param state the current state of the system
     * @return the thrust of the system, in pounds
     */
    public abstract double getThrust(AeroSystemState state);

    /**
     * Updates the state of the system, using the fourth order Runge Kutta method
     * @param deltaT the time increment, in seconds
     */
    @Override
    public void updateState(double deltaT) {
        AeroSystemState initialState = new AeroSystemState(this.state);

        this.setAccelerations(initialState);
        double xVelInc1 = initialState.get(AeroSystemState.X_ACCEL) * deltaT;
        double zVelInc1 = initialState.get(AeroSystemState.Z_ACCEL)* deltaT;
        double angVelInc1 = initialState.get(AeroSystemState.ANGULAR_ACCEL) * deltaT;
        double xPosInc1 = initialState.get(AeroSystemState.X_VEL) * deltaT;
        double zPosInc1 = initialState.get(AeroSystemState.Z_VEL) * deltaT;
        double angPosInc1 = initialState.get(AeroSystemState.ANGULAR_VEL) * deltaT;
        
        AeroSystemState state2 = new AeroSystemState(initialState);
        state2.set(AeroSystemState.TIME, initialState.get(AeroSystemState.TIME) + deltaT / 2.0);
        state2.set(AeroSystemState.X_VEL, initialState.get(AeroSystemState.X_VEL) + xVelInc1 / 2.0);
        state2.set(AeroSystemState.Z_VEL, initialState.get(AeroSystemState.Z_VEL) + zVelInc1 / 2.0);
        state2.set(AeroSystemState.ANGULAR_VEL, initialState.get(AeroSystemState.ANGULAR_VEL) + angVelInc1 / 2.0);
        state2.set(AeroSystemState.X_POS, initialState.get(AeroSystemState.X_POS) + xPosInc1 / 2.0);
        state2.set(AeroSystemState.Z_POS, initialState.get(AeroSystemState.Z_POS) + zPosInc1 / 2.0);
        state2.set(AeroSystemState.ANGULAR_POS, new Angle(initialState.get(AeroSystemState.ANGULAR_POS).getMeasure() + angPosInc1 / 2.0));
        
        this.setAccelerations(state2);
        double xVelInc2 = state2.get(AeroSystemState.X_ACCEL) * deltaT;
        double zVelInc2 = state2.get(AeroSystemState.Z_ACCEL) * deltaT;
        double angVelInc2 = state2.get(AeroSystemState.ANGULAR_ACCEL) * deltaT;
        double xPosInc2 = state2.get(AeroSystemState.X_VEL) * deltaT;
        double zPosInc2 = state2.get(AeroSystemState.Z_VEL) * deltaT;
        double angPosInc2 = state2.get(AeroSystemState.ANGULAR_VEL) * deltaT;
        
        AeroSystemState state3 = new AeroSystemState(initialState);
        state3.set(AeroSystemState.TIME, initialState.get(AeroSystemState.TIME) + deltaT / 2.0);
        state3.set(AeroSystemState.X_VEL, initialState.get(AeroSystemState.X_VEL) + xVelInc2 / 2.0);
        state3.set(AeroSystemState.Z_VEL, initialState.get(AeroSystemState.Z_VEL) + zVelInc2 / 2.0);
        state3.set(AeroSystemState.ANGULAR_VEL, initialState.get(AeroSystemState.ANGULAR_VEL) + angVelInc2 / 2.0);
        state3.set(AeroSystemState.X_POS, initialState.get(AeroSystemState.X_POS) + xPosInc2 / 2.0);
        state3.set(AeroSystemState.Z_POS, initialState.get(AeroSystemState.Z_POS) + zPosInc2 / 2.0);
        state3.set(AeroSystemState.ANGULAR_POS, new Angle(initialState.get(AeroSystemState.ANGULAR_POS).getMeasure() + angPosInc2 / 2.0));
        
        this.setAccelerations(state3);
        double xVelInc3 = state3.get(AeroSystemState.X_ACCEL) * deltaT;
        double zVelInc3 = state3.get(AeroSystemState.Z_ACCEL) * deltaT;
        double angVelInc3 = state3.get(AeroSystemState.ANGULAR_ACCEL) * deltaT;
        double xPosInc3 = state3.get(AeroSystemState.X_VEL) * deltaT;
        double zPosInc3 = state3.get(AeroSystemState.Z_VEL) * deltaT;
        double angPosInc3 = state3.get(AeroSystemState.ANGULAR_VEL) * deltaT;
        
        AeroSystemState state4 = new AeroSystemState(initialState);
        state4.set(AeroSystemState.TIME, initialState.get(AeroSystemState.TIME) + deltaT);
        state4.set(AeroSystemState.X_VEL, initialState.get(AeroSystemState.X_VEL) + xVelInc3);
        state4.set(AeroSystemState.Z_VEL, initialState.get(AeroSystemState.Z_VEL) + zVelInc3);
        state4.set(AeroSystemState.ANGULAR_VEL, initialState.get(AeroSystemState.ANGULAR_VEL) + angVelInc3);
        state4.set(AeroSystemState.X_POS, initialState.get(AeroSystemState.X_POS) + xPosInc3);
        state4.set(AeroSystemState.Z_POS, initialState.get(AeroSystemState.Z_POS) + zPosInc3);
        state4.set(AeroSystemState.ANGULAR_POS, new Angle(initialState.get(AeroSystemState.ANGULAR_POS).getMeasure() + angPosInc3));
        
        this.setAccelerations(state4);
        double xVelInc4 = state4.get(AeroSystemState.X_ACCEL) * deltaT;
        double zVelInc4 = state4.get(AeroSystemState.Z_ACCEL) * deltaT;
        double angVelInc4 = state4.get(AeroSystemState.ANGULAR_ACCEL) * deltaT;
        double xPosInc4 = state4.get(AeroSystemState.X_VEL) * deltaT;
        double zPosInc4 = state4.get(AeroSystemState.Z_VEL) * deltaT;
        double angPosInc4 = state4.get(AeroSystemState.ANGULAR_VEL) * deltaT;
        
        this.time += deltaT;
        this.state.set(AeroSystemState.TIME, this.time);
        this.state.set(AeroSystemState.X_VEL, initialState.get(AeroSystemState.X_VEL) + xVelInc1 / 6.0 + xVelInc2 / 3.0 + xVelInc3 / 3.0 + xVelInc4 / 6.0);
        this.state.set(AeroSystemState.Z_VEL, initialState.get(AeroSystemState.Z_VEL) + zVelInc1 / 6.0 + zVelInc2 / 3.0 + zVelInc3 / 3.0 + zVelInc4 / 6.0);
        this.state.set(AeroSystemState.ANGULAR_VEL, initialState.get(AeroSystemState.ANGULAR_VEL) + angVelInc1 / 6.0 + angVelInc2 / 3.0 + angVelInc3 / 3.0 + angVelInc4 / 6.0);
        this.state.set(AeroSystemState.X_POS, initialState.get(AeroSystemState.X_POS) + xPosInc1 / 6.0 + xPosInc2 / 3.0 + xPosInc3 / 3.0 + xPosInc4 / 6.0 / 6.0 + xPosInc2 / 3.0 + xPosInc3 / 3.0 + xPosInc4 / 6.0);
        this.state.set(AeroSystemState.Z_POS, initialState.get(AeroSystemState.Z_POS) + zPosInc1 / 6.0 + zPosInc2 / 3.0 + zPosInc3 / 3.0 + zPosInc4 / 6.0);
        this.state.set(AeroSystemState.ANGULAR_POS, new Angle(initialState.get(AeroSystemState.ANGULAR_POS).getMeasure() + angPosInc1 / 6.0 + angPosInc2 / 3.0 + angPosInc3 / 3.0 + angPosInc4 / 6.0));
        this.setAccelerations(this.state);
    }


    // Private Methods
    private void setAccelerations(AeroSystemState state) {
        double lift = this.getLift(state);
        double drag = this.getDrag(state);
        double pitchMoment = this.getPitchingMoment(state);
        Angle fpa = state.get(AeroSystemState.FLIGHT_PATH_ANGLE); // fpa stands for flight path angle
        double thrust = this.getThrust(state);
        Angle theta = state.get(AeroSystemState.ANGULAR_POS);

        // Rotate the forces to inertial X and Z axis systems
        double xLift = -1 * lift * fpa.sin();
        double zLift = lift * fpa.cos();

        double xDrag = -1 * drag * fpa.cos(); // Because drag points backwards
        double zDrag = -1 * drag * fpa.sin();

        double xThrust = thrust * theta.cos();
        double zThrust = thrust * theta.sin();

        double xForce = xLift + xDrag + xThrust;
        double zForce = zLift + zDrag + zThrust - (PhysicalConstants.GRAVITY_ACCELERATION * this.getMass(state));

        state.set(AeroSystemState.X_ACCEL, xForce / this.getMass(state));
        state.set(AeroSystemState.Z_ACCEL, zForce / this.getMass(state));
        state.set(AeroSystemState.ANGULAR_ACCEL, pitchMoment / this.getIyy(state));
    }

}
