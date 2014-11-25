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
package dynamics.airplane;

import dynamics.AeroSystemState;
import geometry.angle.Angle;
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
    
    private double baseMass;
    private double iyy;
    
    private SolidRocketEngine rocketEngine;
    
    private double clAlpha;
    private Angle alphaZeroLift;
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
    public double getBaseMass() {
        return baseMass;
    }

    /**
     * @param mass the mass to set
     */
    public void setBaseMass(double mass) {
        this.baseMass = mass;
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
     * @return the alphaZeroLift
     */
    public Angle getAlphaZeroLift() {
        return alphaZeroLift;
    }

    /**
     * @param alphaZeroLift the alphaZeroLift to set
     */
    public void setAlphaZeroLift(Angle alphaZeroLift) {
        this.alphaZeroLift = alphaZeroLift;
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
