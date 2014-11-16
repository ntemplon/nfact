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

/**
 *
 * @author Nathan Templon
 */
public interface Gas {
    
    double heatRatioDouble();
    double molarMass();
    double specificGasConstant();
    
    HeatRatio heatRatio();
    
    double speedOfSound(GasState state);
    
    static public class HeatRatio {
        public HeatRatio( double ratio ) {
            this.ratio = ratio;
        }
        
        public double value() {
            return ratio;
        }
        
        private double ratio;
    }
    
    static public class MolarMass {
        public MolarMass( double mass ) {
            this.mass = mass;
        }
        
        public double value() {
            return mass;
        }
        
        private double mass;
    }
    
    
    static class GasState {
        public GasState( Temperature temp, Pressure pres ) {
            this.temp = temp;
            this.pres = pres;
        }
        
        public GasState() {
            this.temp = new Temperature(0);
            this.pres = new Pressure(0);
        }


        public Temperature temp() {
            return temp;
        }
        
        public Pressure pres() {
            return pres;
        }
        
        private Temperature temp;
        private Pressure pres;
    }
    
    static class Temperature {
        public Temperature( double temp ) {
            this.temp = temp;
        }
        
        public double value() {
            return temp;
        }
        
        private double temp;
    }
    
    static class Pressure {
        public Pressure( double pres ) {
            this.pres = pres;
        }
        
        public double value() {
            return pres;
        }
        
        private double pres;
    }
    
    
    static class MachNumber {
        public MachNumber( double machNum ) {
            this.machNum = machNum;
        }
        
        public double value() {
            return machNum;
        }
        
        private double machNum;
    }
    
}
