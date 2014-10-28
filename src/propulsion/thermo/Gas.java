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
