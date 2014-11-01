/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aero;

import geometry.angle.Angle;

/**
 *
 * @author nathan
 */
public interface WingState {
    
    // Properties
    Angle getAngleOfAttack();
    double getCl();
    double getCd();
    double getCpm();
    
}
