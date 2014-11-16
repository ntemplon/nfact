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

package propulsion.thermo;

import propulsion.thermo.Gas.HeatRatio;
import propulsion.thermo.Gas.MolarMass;

/**
 *
 * @author Nathan Templon
 */
public class IdealGas implements Gas{
    
    public IdealGas(HeatRatio heatRatio, MolarMass molarMass) {
        this.heatRatio = heatRatio;
        this.molarMass = molarMass.value();
    }
    
    
    @Override
    public double heatRatioDouble() {
        return heatRatio.value();
    }
    
    
    @Override
    public double molarMass() {
        return molarMass;
    }
    
    
    @Override
    public double specificGasConstant() {
        return (UNIVERSAL_GAS_CONSTANT / molarMass);
    }
    
    
    @Override
    public HeatRatio heatRatio() {
        return heatRatio;
    }
    
    
    public double cp() {
        return (specificGasConstant() * heatRatio().value()) / (heatRatio().value() - 1);
    }
    
    
    @Override
    public double speedOfSound(GasState state) {
        return Math.sqrt( heatRatio.value() * specificGasConstant() *  state.temp().value() );
    }
    
    
    private HeatRatio heatRatio;
    private double molarMass;
    
    public static double UNIVERSAL_GAS_CONSTANT = 8314;
    
}
