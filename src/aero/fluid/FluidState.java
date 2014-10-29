/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aero.fluid;

/**
 *
 * @author nathant
 */
public abstract class FluidState {

    // Fields
    private final Fluid fluid;

    // Properties
    /**
     *
     * @return the fluid
     */
    public Fluid getFluid() {
        return this.fluid;
    }

    /**
     *
     * @return the density, in slugs per cubic foot
     */
    public abstract double getDensity();

    /**
     *
     * @return the temperature, in degrees Rankine
     */
    public abstract double getTemperature();

    /**
     *
     * @return the pressure, in pounds per square foot
     */
    public abstract double getPressure();

    /**
     *
     * @return the gas constant, in foot-pound per slug-degree Rankine
     */
    public abstract double getGasConstant();
    
    /**
     * 
     * @return the specific heat ratio
     */
    public abstract double getHeatRatio();
    
    /**
     * 
     * @return the speed of sound of the current state, in feet per second
     */
    public abstract double getSpeedOfSound();

    // Initalization
    public FluidState(Fluid fluid) {
        this.fluid = fluid;
    }

}
