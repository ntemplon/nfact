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

package propulsion.rocket;

import propulsion.propellant.LiquidPropellant;
import propulsion.rocket.component.CombustionChamber;
import propulsion.rocket.component.Nozzle;
import propulsion.rocket.component.Throat;
import propulsion.thermo.Gas;
import propulsion.thermo.Gas.GasState;
import propulsion.thermo.Gas.MachNumber;
import propulsion.thermo.Gas.Pressure;
import propulsion.thermo.Gas.Temperature;



/**
 * A class representing Liquid Rocket Engines
 * @author Nathan Templon
 */
public class LiquidRocketEngine implements RocketEngine {
    
    /**
     * The standard constructor for a LiquidRocketEngine object.
     * @param nozzle the Nozzle of the engine
     * @param chamber the Combustion Chamber of the engine
     * @param throat the Throat of the Engine 
     */
    public LiquidRocketEngine(Nozzle nozzle, CombustionChamber chamber, Throat throat) {
        this.nozzle = nozzle;
        this.chamber = chamber;
        this.throat = throat;
        this.ambPres = new Pressure(0);
    }
    
    
    /**
     * A method that sets the state of the engine.
     * @param state the state (entrance mass flow rate and propellant) of the engine
     */
    public void setState( LiquidEngineState state ) {
        this.state = state;
        this.propellant = state.propellant();
        exhaust = propellant.exhaust();
        
        double rBar = exhaust.specificGasConstant();
        double heatRatio = exhaust.heatRatio().value();
        
        double flameTemp = state.propellant().flameTemp().value();
        
        chamberPres = new Pressure( ((state.flowRate().value() / throat.area().value())
            * Math.sqrt(rBar * flameTemp)) / Math.sqrt(heatRatio * Math.pow(2 / (heatRatio + 1),
            (heatRatio + 1) / (heatRatio - 1))) );
        
        chamber.setState(new GasState( state.propellant().flameTemp(), chamberPres ));
        
        throatPres = new Pressure(chamberPres.value() * Math.pow( 1 + ((heatRatio - 1) / 2),
            (-1 * heatRatio) / (heatRatio - 1) ));
        
        throatTemp = new Temperature(chamber.stagTemp().value() * ( 1 / ( 1 + ((heatRatio - 1) / 2) ) ));
        
        throat.setState(new GasState(throatTemp, throatPres));
        
        double throatSpeedOfSound = exhaust.speedOfSound(throat.state());
        
        exitMach = nozzle.exitMach(exhaust);
        
        exitPres = new Pressure( chamberPres.value() / Math.pow( 1 + (((heatRatio - 1) / 2) 
                * exitMach.value() * exitMach.value()), heatRatio / (heatRatio - 1) ) );
        
        exitTemp = new Temperature( chamber.stagTemp().value() / ( 1 + (((heatRatio - 1) / 2 ) * exitMach.value() * exitMach.value())) );
        
        exitState = new GasState(exitTemp, exitPres);
        
        double exitSpeedOfSound = exhaust.speedOfSound(exitState);
        
        exitVelocity = exitSpeedOfSound * exitMach.value();
        
        effExitVelocity = exitVelocity + (((exitPres.value() - ambPres.value()) / state.flowRate().value()) * nozzle.exitArea().value());
        
        isp = effExitVelocity / GRAVITY;
        
        thrust = effExitVelocity * state.flowRate().value();
    }
    
    
    /**
     * A method to set the ambient pressure of the engine.
     * @param pres the ambient pressure of the engine.
     */
    public void setAmbientPres( Pressure pres ) {
        this.ambPres = pres;
    }
    
    
    /**
     * A method for getting the current state of the engine.
     * @return Returns the current state of the engine.
     */
    public LiquidEngineState state() {
        return state;
    }
    
    
    /**
     * A method for getting the current Propellant of the engine.
     * @return Returns the Propellant of the engine.
     */
    public LiquidPropellant propellant() {
        return propellant;
    }
    
    
    /**
     * A method for getting the characteristic velocity of the engine.
     * @return Returns the characteristic velocity of the engine.
     */
    public double charVelocity() {
        return this.propellant.charVelocity();
    }
    
    
    /**
     * A method for getting the current mass flow rate of the engine.
     * @return Returns the current mass flow rate of the engine.
     */
    public MassFlowRate massFlowRate() {
        return state.flowRate();
    }
    
    
    /**
     * A method for getting the current chamber pressure of the engine.
     * @return Returns the current chamber pressure of the engine.
     */
    public Pressure chamberPres() {
        return chamberPres;
    }
    
    
    /**
     * A method for getting the current throat pressure of the engine.
     * @return Returns the current th pressure of the engine.
     */
    public Pressure throatPres() {
        return throatPres;
    }
    
    
    /**
     * A method for getting the current throat temperature of the engine.
     * @return Returns the current throat temperature of the engine.
     */
    public Temperature throatTemp() {
        return throatTemp;
    }
    
    
    /**
     * A method for getting the current exit pressure of the engine.
     * @return Returns the current exit pressure of the engine.
     */
    public Pressure exitPres() {
        return exitPres;
    }
    
    
    /**
     * A method for getting the current exit temperature of the engine.
     * @return Returns the current exit temperature of the engine.
     */
    public Temperature exitTemp() {
        return exitTemp;
    }
    
    
    /**
     * A method for getting the current exit state of the engine.
     * @return Returns the current exit state of the engine.
     */
    public GasState exitState() {
        return exitState;
    }
    
    
    /**
     * A method for getting the current exit velocity of the engine.
     * @return Returns the current exit velocity of the engine.
     */
    public double exitVelocity() {
        return exitVelocity;
    }
    
    
    /**
     * A method for getting the current effective exit velocity of the engine.
     * @return Returns the current effective exit velocity of the engine.
     */
    public double effExitVelocity() {
        return effExitVelocity;
    }
    
    
    /**
     * A method for getting the current specific impulse of the engine.
     * @return Returns the current specific impulse of the engine.
     */
    public double isp() {
        return isp;
    }
    
    
    /**
     * A method for getting the current thrust of the engine.
     * @return Returns the current thrust of the engine.
     */
    public double thrust() {
        return thrust;
    }
    
    
    /**
     * A method for getting the current exit Mach number of the engine.
     * @return Returns the current exit Mach number of the engine.
     */
    public MachNumber exitMach() {
        return exitMach;
    }
    
    
    private Nozzle nozzle;
    private CombustionChamber chamber;
    private Throat throat;
    private Gas exhaust;
    private Pressure ambPres;
    
    private LiquidEngineState state;
    private LiquidPropellant propellant;
    
    private Pressure throatPres;
    private Pressure chamberPres;
    private Temperature throatTemp;
    private Pressure exitPres;
    private Temperature exitTemp;
    private GasState exitState;
    private MachNumber exitMach;
    
    private double exitVelocity;
    private double effExitVelocity;
    private double isp;
    private double thrust;
    
    public static final double GRAVITY = 9.81;
    
    
    /**
     * A class for storing the state of liquid engines.
     */
    public static class LiquidEngineState {
        
        /**
         * The standard constructor the a LiquidEngineState
         * @param flowRate the current mass flow rate into the engine
         * @param propellant the current propellant being used by the engine
         */
        public LiquidEngineState( MassFlowRate flowRate, LiquidPropellant propellant ) {
            this.flowRate = flowRate;
            this.propellant = propellant;
        }
        
        
        /**
         * A method for getting the current mass flow rate.
         * @return Returns the current mass flow rate of the engine.
         */
        public MassFlowRate flowRate() {
            return flowRate;
        }
        
        
        /**
         * A method for getting the current propellant used by the engine.
         * @return Returns the current propellant used by the engine.
         */
        public LiquidPropellant propellant() {
            return propellant;
        }
        
        private MassFlowRate flowRate;
        private LiquidPropellant propellant;
        
    }
    
    
    /**
     * A class for representing mass flow rates into liquid engines.
     */
    public static class MassFlowRate {
        
        /**
         * The standard constructor.
         * @param rate the rate, in kg/s, at which propellant is flowing into the engine.
         */
        public MassFlowRate( double rate ) {
            this.rate = rate;
        }
        
        
        /**
         * A method for getting the numerical value of the mass flow rate.
         * @return Returns the numerical value of the mass flow rate.
         */
        public double value() {
            return rate;
        }
        
        
        /**
         * A method for getting a string representing the MassFlowRate object.
         * @return Returns the numerical value of the mass flow rate as a String.
         */
        @Override
        public String toString() {
            return "" + rate;
        }
        
        private double rate;
        
    }
}
