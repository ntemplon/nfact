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
