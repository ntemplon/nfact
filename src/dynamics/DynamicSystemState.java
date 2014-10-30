/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import geometry.angle.Angle;

/**
 *
 * @author nathant
 * @param <TState>
 */
public class DynamicSystemState<TState extends DynamicSystemState> extends SystemState<TState> {
    
    // Fields
    private double xAcceleration;
    private double zAcceleration;
    private double angularAcceleration;
    
    private double xVelocity;
    private double zVelocity;
    private double angularVelocity;

    private double xPosition;
    private double zPosition;
    private Angle angularPosition;
    
    // Properties
    /**
     * @return the xAcceleration in feet per second squared
     */
    public double getXAcceleration() {
        return xAcceleration;
    }

    /**
     * @param xAcceleration the xAcceleration in feet per second squared to set
     */
    public void setXAcceleration(double xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    /**
     * @return the zAcceleration in feet per second squared
     */
    public double getZAcceleration() {
        return zAcceleration;
    }

    /**
     * @param zAcceleration the zAcceleration in feet per second squared to set
     */
    public void setZAcceleration(double zAcceleration) {
        this.zAcceleration = zAcceleration;
    }

    /**
     * @return the angularAcceleration in radians per second squared
     */
    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    /**
     * @param angularAcceleration the angularAcceleration in radians per second squared to set
     */
    public void setAngularAcceleration(double angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }

    /**
     * @return the xVelocity in feet per second
     */
    public double getXVelocity() {
        return xVelocity;
    }

    /**
     * @param xVelocity the xVelocity in feet per second to set
     */
    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    /**
     * @return the zVelocity in feet per second
     */
    public double getZVelocity() {
        return zVelocity;
    }

    /**
     * @param zVelocity the zVelocity in feet per second to set
     */
    public void setZVelocity(double zVelocity) {
        this.zVelocity = zVelocity;
    }

    /**
     * @return the angularVelocity in radians per second
     */
    public double getAngularVelocity() {
        return angularVelocity;
    }

    /**
     * @param angularVelocity the angularVelocity in radians per second to set
     */
    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    /**
     * @return the xPosition in feet
     */
    public double getXPosition() {
        return xPosition;
    }

    /**
     * @param xPosition the xPosition in feet to set
     */
    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * @return the zPosition in feet
     */
    public double getZPosition() {
        return zPosition;
    }

    /**
     * @param zPosition the zPosition in feet to set
     */
    public void setZPosition(double zPosition) {
        this.zPosition = zPosition;
    }

    /**
     * @return the angularPosition in radians
     */
    public Angle getAngularPosition() {
        return angularPosition;
    }

    /**
     * @param angularPosition the angularPosition in radians to set
     */
    public void setAngularPosition(Angle angularPosition) {
        this.angularPosition = angularPosition;
    }

    /**
     *
     * @return the total speed in feet per second
     */
    public double getSpeed() {
        return Math.sqrt(this.getXVelocity() * this.getXVelocity() + this.getZVelocity() * this.getZVelocity());
    }
    
    
    // Initialization
    public DynamicSystemState() {
        
    }
    
}
