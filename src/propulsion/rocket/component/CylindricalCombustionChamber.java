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

package propulsion.rocket.component;

import propulsion.thermo.Gas.GasState;
import propulsion.thermo.Gas.Pressure;
import propulsion.thermo.Gas.Temperature;


/**
 *
 * @author Nathan Templon
 */
public class CylindricalCombustionChamber implements CombustionChamber {
    
    public CylindricalCombustionChamber( GasState state ) {
        this.stagTemp = state.temp();
        this.stagPres = state.pres();
    }
    
    
    public CylindricalCombustionChamber() {
        this.stagTemp = new Temperature(0);
        this.stagPres = new Pressure(0);
    }
    
    
    @Override
    public Temperature stagTemp() {
        return stagTemp;
    }
    
    
    @Override
    public Pressure stagPres() {
        return stagPres;
    }
    
    
    @Override
    public void setState( GasState state ) {
        this.stagTemp = state.temp();
        this.stagPres = state.pres();
    }
    
    
    private Temperature stagTemp;
    private Pressure stagPres;
    private double contractionRatio;
    private CharLength charLength;
    
}
