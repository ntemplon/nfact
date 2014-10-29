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

    @Override
    public void updateState(double deltaT) {
        this.time += deltaT;
        this.state.setTime(this.time);

        // Velocity increments
        this.setAccelerations(this.state);
        this.state.setXVelocity(this.state.getXVelocity() + this.state.getXAcceleration() * deltaT);
        this.state.setZVelocity(this.state.getZVelocity() + this.state.getZAcceleration() * deltaT);
        this.state.setAngularVelocity(this.state.getAngularVelocity() + this.state.getAngularAcceleration() * deltaT);

        // Position increments
        this.state.setXPosition(this.state.getXPosition() + this.state.getXVelocity() * deltaT);
        this.state.setZPosition(this.state.getZPosition() + this.state.getZVelocity() * deltaT);
        this.state.setAngularPosition(new Angle(this.state.getAngularPosition().getMeasure() + this.state.getAngularVelocity() * deltaT));
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

        // Should this have the Math.PI / 180.0?  The other spreadsheet assumes the units are degrees, but I am not so sure...
        state.setAngularAcceleration(pitchMoment / this.getIyy());
    }

}
