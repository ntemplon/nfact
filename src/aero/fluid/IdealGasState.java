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
public class IdealGasState extends FluidState {
    
    // Fields
    protected final IdealGas idealFluid;
    private final double pressure;
    private final double temperature;
    private final double density;
    private final double speedOfSound;
    
    // Properties
    @Override
    public double getDensity() {
        return this.density;
    }

    @Override
    public double getTemperature() {
        return this.temperature;
    }

    @Override
    public double getPressure() {
        return this.pressure;
    }

    @Override
    public double getGasConstant() {
        return this.idealFluid.getGasConstant();
    }

    @Override
    public double getHeatRatio() {
        return this.idealFluid.getHeatRatio();
    }
    
    @Override
    public double getSpeedOfSound() {
        return this.speedOfSound;
    }
    
    // Initialization
    /**
     * 
     * @param fluid the fluid
     * @param temperature the temperature of the fluid, in degrees Rankine
     * @param pressure the static pressure of the fluid, in pounds per square foot
     */
    public IdealGasState(IdealGas fluid, double temperature, double pressure) {
        super(fluid);
        this.idealFluid = fluid;
        this.temperature = temperature;
        this.pressure = pressure;
        
        this.density = pressure / (temperature * this.idealFluid.getGasConstant());
        this.speedOfSound = Math.sqrt(this.idealFluid.getGasConstant() * temperature);
    }
    
}
