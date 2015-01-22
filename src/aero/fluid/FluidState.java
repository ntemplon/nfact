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
    
    public final double getViscosity() {
        return this.fluid.getViscosity();
    }

    // Initalization
    public FluidState(Fluid fluid) {
        this.fluid = fluid;
    }

}
