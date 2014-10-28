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

package propulsion.propellant;

import propulsion.thermo.Gas;
import propulsion.thermo.Gas.Temperature;


/**
 *
 * @author Nathan Templon
 */
public class LiquidPropellant implements Propellant {
    
    public LiquidPropellant( Gas exhaust, Temperature flameTemp ) {
        this.flameTemp = flameTemp;
        this.exhaust = exhaust;
        
        double heatRatio = exhaust.heatRatio().value();
        this.charVelocity = Math.sqrt( (1/heatRatio) * Math.pow( 
                (heatRatio + 1) / 2, (heatRatio + 1) / (heatRatio - 1)) 
                * exhaust.specificGasConstant() * flameTemp.value());
    }
    
    
    public Gas exhaust() {
        return exhaust;
    }
    
    public Temperature flameTemp() {
        return flameTemp;
    }
    
    public double charVelocity() {
        return charVelocity;
    }
    
    private Gas exhaust;
    private Temperature flameTemp;
    private double charVelocity;
    
}
