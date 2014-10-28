/*
 * Copyright (C) 2014 Nathan Templon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
