/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aero;

import geometry.angle.Angle;

/**
 *
 * @author nathant
 */
public interface Flap {
    
    // Properties
    Wing getWing();
    double getSpan();
    double getChord();
    void setSRef();
    double getSRef();
    
    
    // Public Methods
    double getClIncrement(Angle deflection, WingState state);
    
}
