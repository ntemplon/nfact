/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package propulsion.rocket;

import function.SingleVariableFunction;

/**
 *
 * @author nathant
 */
public class HobbyRocketEngine implements RocketEngine {
    
    // Fields
    private final SingleVariableFunction thrust;
    
    // Initialization
    public HobbyRocketEngine(SingleVariableFunction thrust) {
        this.thrust = thrust;
    }
    
    // Public Methods
    /**
     * 
     * @param time the time since ignition, in seconds
     * @return the thrust of the engine, in pounds
     */
    public double getThrustAt(double time) {
        if (thrust.hasFiniteDomain) {
            if (time < thrust.domainMin || thrust.domainMax < time) {
                return 0;
            }
        }
        return thrust.evaluate(time);
    }
    
}
