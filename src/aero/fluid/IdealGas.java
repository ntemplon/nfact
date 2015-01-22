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
public class IdealGas implements Fluid {
    
    // Fields
    private final double molarMass;
    private final double heatRatio;
    private final double gasConstant;
    private final double viscosity;
    
    // Properties
    @Override
    public double getMolarMass() {
        return this.molarMass;
    }
    
    public double getHeatRatio() {
        return this.heatRatio;
    }
    
    public double getGasConstant() {
        return this.gasConstant;
    }
    
    @Override
    public double getViscosity() {
        return this.viscosity;
    }
    
    // Initialization
    public IdealGas(double molarMass, double heatRatio, double viscosity) {
        this.molarMass = molarMass;
        this.heatRatio = heatRatio;
        this.viscosity = viscosity;
        
        this.gasConstant = Fluid.GAS_CONSTANT / this.molarMass;
    }
    
}
