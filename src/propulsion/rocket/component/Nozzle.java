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

import geometry.PlaneArea.Area;
import propulsion.thermo.Gas;
import propulsion.thermo.Gas.HeatRatio;
import propulsion.thermo.Gas.MachNumber;

/**
 *
 * @author Nathan Templon
 */
public class Nozzle {
    
    public Nozzle(Area throatArea, AreaRatio ratio) {
        this.throatArea = throatArea;
        this.ratio = ratio;
    }
    
    
    public MachNumber exitMach( Gas exhaust ) {
        
        return Nozzle.exitMach(ratio, exhaust.heatRatio());
    }
    
    
    public Area exitArea() {
        return new Area(throatArea.value() * ratio.value());
    }
    
    
    public Area throatArea() {
        return throatArea;
    }
    
    
    public static MachNumber exitMach( AreaRatio ratio, HeatRatio heatRatio ) {
        
        double areaRatio = ratio.value();
        
        double answer = 0;
        double count;
        double lastGuess = 2.5;
        double value = lastGuess;
        boolean noSolution = true;
        
        while (answer < 1) {
            
            noSolution = false;
            
            count = 0;
        
            while(Math.abs(areaRatio(new MachNumber(value), heatRatio) - areaRatio) > .0001 && !noSolution) {
                value = value - areaRatio(new MachNumber(value), heatRatio) / areaRatioDeriv(new MachNumber(value), heatRatio);
                count++;
                
                if (count > 1000000) {
                    noSolution = true;
                }
            }
            
            if (!noSolution) {
                answer = value;
            }
            lastGuess = lastGuess + 1;
            value = lastGuess;
            
        }
        
        return new MachNumber(answer);
        
    }
    
    private static double areaRatio(MachNumber exitMach, HeatRatio heatRatio) {
        double mach = exitMach.value();
        double ratio = heatRatio.value();
        
        return (1 / mach) * Math.pow((2/(ratio + 1) ) * (1 + ((ratio - 1)/2) * mach * mach), (ratio + 1)/(2* (ratio - 1)));
    }
    
    private static double areaRatioDeriv(MachNumber exitMach, HeatRatio heatRatio) {
        double mach = exitMach.value();
        
        return (areaRatio(new MachNumber(mach + 0.001), heatRatio) - areaRatio(new MachNumber(mach - 0.001), heatRatio))/0.002;
    }
    
    public static class AreaRatio {
        
        public AreaRatio( double ratio ) {
            this.ratio = ratio;
        }
        
        public double value() {
            return ratio;
        }
        
        private double ratio;
        
    }
    
    
    private Area throatArea;
    private AreaRatio ratio;
    
}
