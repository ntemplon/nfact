/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package propulsion.rocket;

/**
 *
 * @author nathan
 */
public interface SolidRocketEngine extends RocketEngine {
    
    // Properties
    String getName();
    double getBurnTime();
    
    
    // Public Methods
    double getThrust(double time);
    
}
