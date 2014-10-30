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
public class AeroSystemState extends DynamicSystemState {

    // Fields
    private FluidState fluidState;
    
    private double cl;
    private double cd;
    private double cpm;
    
    private double thrust;


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
     * @return the cl
     */
    public double getCl() {
        return cl;
    }

    /**
     * @param cl the cl to set
     */
    public void setCl(double cl) {
        this.cl = cl;
    }

    /**
     * @return the cd
     */
    public double getCd() {
        return cd;
    }

    /**
     * @param cd the cd to set
     */
    public void setCd(double cd) {
        this.cd = cd;
    }

    /**
     * @return the cpm
     */
    public double getCpm() {
        return cpm;
    }

    /**
     * @param cpm the cpm to set
     */
    public void setCpm(double cpm) {
        this.cpm = cpm;
    }
    
    /**
     * @return the thrust in pounds
     */
    public double getThrust() {
        return thrust;
    }

    /**
     * @param thrust the thrust in pounds to set
     */
    public void setThrust(double thrust) {
        this.thrust = thrust;
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
        if (Double.isNaN(fpa.getMeasure())) {
            fpa = new Angle(Math.PI / 2);
        }
        return fpa;
    }

    /**
     *
     * @return the angle of attack of the aircraft
     */
    public Angle getAngleOfAttack() {
        return new Angle(this.getAngularPosition().getMeasure() - this.getFlightPathAngle().getMeasure());
//        return this.getAngularPosition().add(this.getFlightPathAngle().scalarMultiply(-1.0));
    }


    // Initialization
    public AeroSystemState() {

    }
    
    public AeroSystemState(AeroSystemState copy) {
        this.setTime(copy.getTime());
        
        this.setXAcceleration(copy.getXAcceleration());
        this.setZAcceleration(copy.getZAcceleration());
        this.setAngularAcceleration(copy.getAngularAcceleration());
        this.setXVelocity(copy.getXVelocity());
        this.setZVelocity(copy.getZVelocity());
        this.setAngularVelocity(copy.getAngularVelocity());
        this.setXPosition(copy.getXPosition());
        this.setZPosition(copy.getZPosition());
        this.setAngularPosition(copy.getAngularPosition());
        
        this.setThrust(copy.getThrust());
        this.setCl(copy.getCl());
        this.setCd(copy.getCd());
        this.setCpm(copy.getCpm());
        this.setFluidState(copy.getFluidState());
    }
    
}