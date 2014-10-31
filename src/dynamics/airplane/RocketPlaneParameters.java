/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics.airplane;

import dynamics.AeroSystemState;
import propulsion.rocket.SolidRocketEngine;

/**
 *
 * @author nathan
 */
public class RocketPlaneParameters {
    
    // Fields
    private double sRef;
    private double cBar;
    private double spanEfficiency;
    private double aspectRatio;
    private double zThrust;
    
    private double mass;
    private double iyy;
    
    private SolidRocketEngine rocketEngine;
    
    private double clAlpha;
    private double clDeltaE;
    private double clQ;
    private double cd0;
    private double cpm0;
    private double cpmAlpha;
    private double cpmQ;
    
    private AeroSystemState initialState;
    
    
    // Properties
    /**
     * @return the sRef
     */
    public double getSRef() {
        return sRef;
    }

    /**
     * @param sRef the sRef to set
     */
    public void setSRef(double sRef) {
        this.sRef = sRef;
    }

    /**
     * @return the cBar
     */
    public double getCBar() {
        return cBar;
    }

    /**
     * @param cBar the cBar to set
     */
    public void setCBar(double cBar) {
        this.cBar = cBar;
    }

    /**
     * @return the spanEfficiency
     */
    public double getSpanEfficiency() {
        return spanEfficiency;
    }

    /**
     * @param spanEfficiency the spanEfficiency to set
     */
    public void setSpanEfficiency(double spanEfficiency) {
        this.spanEfficiency = spanEfficiency;
    }

    /**
     * @return the aspectRatio
     */
    public double getAspectRatio() {
        return aspectRatio;
    }

    /**
     * @param aspectRatio the aspectRatio to set
     */
    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * @return the zThrust
     */
    public double getZThrust() {
        return zThrust;
    }

    /**
     * @param zThrust the zThrust to set
     */
    public void setZThrust(double zThrust) {
        this.zThrust = zThrust;
    }

    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * @return the iyy
     */
    public double getIyy() {
        return iyy;
    }

    /**
     * @param iyy the iyy to set
     */
    public void setIyy(double iyy) {
        this.iyy = iyy;
    }

    /**
     * @return the rocketEngine
     */
    public SolidRocketEngine getRocketEngine() {
        return rocketEngine;
    }

    /**
     * @param rocketEngine the rocketEngine to set
     */
    public void setRocketEngine(SolidRocketEngine rocketEngine) {
        this.rocketEngine = rocketEngine;
    }

    /**
     * @return the clAlpha
     */
    public double getClAlpha() {
        return clAlpha;
    }

    /**
     * @param clAlpha the clAlpha to set
     */
    public void setClAlpha(double clAlpha) {
        this.clAlpha = clAlpha;
    }

    /**
     * @return the clDeltaE
     */
    public double getClDeltaE() {
        return clDeltaE;
    }

    /**
     * @param clDeltaE the clDeltaE to set
     */
    public void setClDeltaE(double clDeltaE) {
        this.clDeltaE = clDeltaE;
    }

    /**
     * @return the clQ
     */
    public double getClQ() {
        return clQ;
    }

    /**
     * @param clQ the clQ to set
     */
    public void setClQ(double clQ) {
        this.clQ = clQ;
    }

    /**
     * @return the cd0
     */
    public double getCd0() {
        return cd0;
    }

    /**
     * @param cd0 the cd0 to set
     */
    public void setCd0(double cd0) {
        this.cd0 = cd0;
    }

    /**
     * @return the cpm0
     */
    public double getCpm0() {
        return cpm0;
    }

    /**
     * @param cpm0 the cpm0 to set
     */
    public void setCpm0(double cpm0) {
        this.cpm0 = cpm0;
    }

    /**
     * @return the cpmAlpha
     */
    public double getCpmAlpha() {
        return cpmAlpha;
    }

    /**
     * @param cpmAlpha the cpmAlpha to set
     */
    public void setCpmAlpha(double cpmAlpha) {
        this.cpmAlpha = cpmAlpha;
    }

    /**
     * @return the cpmQ
     */
    public double getCpmQ() {
        return cpmQ;
    }

    /**
     * @param cpmQ the cpmQ to set
     */
    public void setCpmQ(double cpmQ) {
        this.cpmQ = cpmQ;
    }

    /**
     * @return the initialState
     */
    public AeroSystemState getInitialState() {
        return initialState;
    }

    /**
     * @param initialState the initialState to set
     */
    public void setInitialState(AeroSystemState initialState) {
        this.initialState = initialState;
    }
    
    
    // Initialization
    public RocketPlaneParameters() {
        
    }
    
}
