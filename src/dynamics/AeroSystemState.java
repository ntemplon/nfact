/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import aero.fluid.FluidState;
import geometry.angle.Angle;

/**
 *
 * @author nathant
 */
public class AeroSystemState implements SystemState {

    // Fields
    private FluidState fluidState;

    private double xVelocity;
    private double zVelocity;
    private double angularVelocity;

    private double xPosition;
    private double zPosition;
    private Angle angularPosition;


    // Properties
    /**
     * @return the fluid
     */
    public FluidState getFluidState() {
        return fluidState;
    }

    /**
     * @param state the fluid state to set
     */
    protected void setFluidState(FluidState state) {
        this.fluidState = state;
    }

    /**
     * @return the xVelocity
     */
    public double getXVelocity() {
        return xVelocity;
    }

    /**
     * @param xVelocity the xVelocity to set
     */
    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    /**
     * @return the zVelocity
     */
    public double getZVelocity() {
        return zVelocity;
    }

    /**
     * @param zVelocity the zVelocity to set
     */
    public void setZVelocity(double zVelocity) {
        this.zVelocity = zVelocity;
    }

    /**
     * @return the angularVelocity
     */
    public double getAngularVelocity() {
        return angularVelocity;
    }

    /**
     * @param angularVelocity the angularVelocity to set
     */
    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    /**
     * @return the xPosition
     */
    public double getXPosition() {
        return xPosition;
    }

    /**
     * @param xPosition the xPosition to set
     */
    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * @return the zPosition
     */
    public double getZPosition() {
        return zPosition;
    }

    /**
     * @param zPosition the zPosition to set
     */
    public void setZPosition(double zPosition) {
        this.zPosition = zPosition;
    }

    /**
     * @return the angularPosition
     */
    public Angle getAngularPosition() {
        return angularPosition;
    }

    /**
     * @param angularPosition the angularPosition to set
     */
    public void setAngularPosition(Angle angularPosition) {
        this.angularPosition = angularPosition;
    }

    /**
     *
     * @return the total speed
     */
    public double getSpeed() {
        return Math.sqrt(this.getXVelocity() * this.getXVelocity() + this.getZVelocity() * this.getZVelocity());
    }

    /**
     *
     * @return the dynamic pressure of the current state, in pounds per square
     * foot
     */
    public double getDynamicPressure() {
        return 0.5 * this.fluidState.getDensity() * this.getSpeed() * this.getSpeed();
    }

    /**
     *
     * @return the mach number of the system state
     */
    public double getMachNumber() {
        return this.getSpeed() / this.getFluidState().getSpeedOfSound();
    }

    /**
     *
     * @return the flight path angle
     */
    public Angle getFlightPathAngle() {
        Angle fpa = new Angle(this.getZVelocity() / this.getXVelocity(), Angle.TrigFunction.TANGENT);
        if (Double.isNaN(fpa.measure())) {
            fpa = new Angle(Math.PI / 2);
        }
        return fpa;
    }

    /**
     *
     * @return the angle of attack of the aircraft
     */
    public Angle getAngleOfAttack() {
        return this.getAngularPosition().add(this.getFlightPathAngle().scalarMultiply(-1.0));
    }


    // Initialization
    public AeroSystemState() {

    }

}
