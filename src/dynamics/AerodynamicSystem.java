/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import geometry.angle.Angle;
import geometry.angle.Angle.AngleType;
import geometry.angle.Angle.MeasureRange;
import util.PhysicalConstants;

/**
 *
 * @author nathant
 */
public abstract class AerodynamicSystem implements DynamicSystem<AeroSystemState> {

    // Fields
    private final AeroSystemState state;
    private double time;


    // Properties
    @Override
    public AeroSystemState getState() {
        return this.state;
    }

    /**
     *
     * @return the mass of the object in slugs
     */
    public abstract double getMass();

    /**
     *
     * @return the moment of inertia of the system about it's own y axis (out
     * the right wing), in slug-square feet
     */
    public abstract double getIyy();

    public abstract AeroSystemState getInitialState();


    // Initialization
    public AerodynamicSystem() {
        this.time = 0.0;
        this.state = this.getInitialState();
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
        double xVelInc1 = initialState.getXAcceleration() * deltaT;
        double zVelInc1 = initialState.getZAcceleration() * deltaT;
        double angVelInc1 = initialState.getAngularAcceleration() * deltaT;
        double xPosInc1 = initialState.getXVelocity() * deltaT;
        double zPosInc1 = initialState.getZVelocity() * deltaT;
        double angPosInc1 = initialState.getAngularVelocity() * deltaT;
        
        AeroSystemState state2 = new AeroSystemState(initialState);
        state2.setTime(initialState.getTime() + deltaT / 2.0);
        state2.setXVelocity(initialState.getXVelocity() + xVelInc1 / 2.0);
        state2.setZVelocity(initialState.getZVelocity() + zVelInc1 / 2.0);
        state2.setAngularVelocity(initialState.getAngularVelocity() + angVelInc1 / 2.0);
        state2.setXPosition(initialState.getXPosition() + xPosInc1 / 2.0);
        state2.setZPosition(initialState.getZPosition() + zPosInc1 / 2.0);
        state2.setAngularPosition(new Angle(initialState.getAngularPosition().getMeasure() + angPosInc1 / 2.0));
        
        this.setAccelerations(state2);
        double xVelInc2 = state2.getXAcceleration() * deltaT;
        double zVelInc2 = state2.getZAcceleration() * deltaT;
        double angVelInc2 = state2.getAngularAcceleration() * deltaT;
        double xPosInc2 = state2.getXVelocity() * deltaT;
        double zPosInc2 = state2.getZVelocity() * deltaT;
        double angPosInc2 = state2.getAngularVelocity() * deltaT;
        
        AeroSystemState state3 = new AeroSystemState(initialState);
        state3.setTime(initialState.getTime() + deltaT / 2.0);
        state3.setXVelocity(initialState.getXVelocity() + xVelInc2 / 2.0);
        state3.setZVelocity(initialState.getZVelocity() + zVelInc2 / 2.0);
        state3.setAngularVelocity(initialState.getAngularVelocity() + angVelInc2 / 2.0);
        state3.setXPosition(initialState.getXPosition() + xPosInc2 / 2.0);
        state3.setZPosition(initialState.getZPosition() + zPosInc2 / 2.0);
        state3.setAngularPosition(new Angle(initialState.getAngularPosition().getMeasure() + angPosInc2 / 2.0));
        
        this.setAccelerations(state3);
        double xVelInc3 = state3.getXAcceleration() * deltaT;
        double zVelInc3 = state3.getZAcceleration() * deltaT;
        double angVelInc3 = state3.getAngularAcceleration() * deltaT;
        double xPosInc3 = state3.getXVelocity() * deltaT;
        double zPosInc3 = state3.getZVelocity() * deltaT;
        double angPosInc3 = state3.getAngularVelocity() * deltaT;
        
        AeroSystemState state4 = new AeroSystemState(initialState);
        state4.setTime(initialState.getTime() + deltaT);
        state4.setXVelocity(initialState.getXVelocity() + xVelInc3);
        state4.setZVelocity(initialState.getZVelocity() + zVelInc3);
        state4.setAngularVelocity(initialState.getAngularVelocity() + angVelInc3);
        state4.setXPosition(initialState.getXPosition() + xPosInc3);
        state4.setZPosition(initialState.getZPosition() + zPosInc3);
        state4.setAngularPosition(new Angle(initialState.getAngularPosition().getMeasure() + angPosInc3));
        
        this.setAccelerations(state4);
        double xVelInc4 = state4.getXAcceleration() * deltaT;
        double zVelInc4 = state4.getZAcceleration() * deltaT;
        double angVelInc4 = state4.getAngularAcceleration() * deltaT;
        double xPosInc4 = state4.getXVelocity() * deltaT;
        double zPosInc4 = state4.getZVelocity() * deltaT;
        double angPosInc4 = state4.getAngularVelocity() * deltaT;
        
        this.time += deltaT;
        this.state.setTime(this.time);
        this.state.setXVelocity(initialState.getXVelocity() + xVelInc1 / 6.0 + xVelInc2 / 3.0 + xVelInc3 / 3.0 + xVelInc4 / 6.0);
        this.state.setZVelocity(initialState.getZVelocity() + zVelInc1 / 6.0 + zVelInc2 / 3.0 + zVelInc3 / 3.0 + zVelInc4 / 6.0);
        this.state.setAngularVelocity(initialState.getAngularVelocity() + angVelInc1 / 6.0 + angVelInc2 / 3.0 + angVelInc3 / 3.0 + angVelInc4 / 6.0);
        this.state.setXPosition(initialState.getXPosition() + xPosInc1 / 6.0 + xPosInc2 / 3.0 + xPosInc3 / 3.0 + xPosInc4 / 6.0 / 6.0 + xPosInc2 / 3.0 + xPosInc3 / 3.0 + xPosInc4 / 6.0);
        this.state.setZPosition(initialState.getZPosition() + zPosInc1 / 6.0 + zPosInc2 / 3.0 + zPosInc3 / 3.0 + zPosInc4 / 6.0);
        this.state.setAngularPosition(new Angle(initialState.getAngularPosition().getMeasure() + angPosInc1 / 6.0 + angPosInc2 / 3.0 + angPosInc3 / 3.0 + angPosInc4 / 6.0));
        this.setAccelerations(this.state);
    }


    // Private Methods
    private void setAccelerations(AeroSystemState state) {
        double lift = this.getLift(state);
        double drag = this.getDrag(state);
        double pitchMoment = this.getPitchingMoment(state);
        Angle fpa = state.getFlightPathAngle(); // fpa stands for flight path angle
        double thrust = this.getThrust(state);
        Angle theta = state.getAngularPosition();

        // Rotate the forces to inertial X and Z axis systems
        double xLift = -1 * lift * fpa.sin();
        double zLift = lift * fpa.cos();

        double xDrag = -1 * drag * fpa.cos(); // Because drag points backwards
        double zDrag = -1 * drag * fpa.sin();

        double xThrust = thrust * theta.cos();
        double zThrust = thrust * theta.sin();

        double xForce = xLift + xDrag + xThrust;
        double zForce = zLift + zDrag + zThrust - (PhysicalConstants.GRAVITY_ACCELERATION * this.getMass());

        state.setXAcceleration(xForce / this.getMass());
        state.setZAcceleration(zForce / this.getMass());
        state.setAngularAcceleration(pitchMoment / this.getIyy());
    }

}
