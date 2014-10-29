/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aero.fluid;

/**
 *
 * @author nathant
 */
public interface Fluid {
    
    // Constants
    /**
     * The universal gas constant, in foot-pounds per slug-mole
     */
    public static final double GAS_CONSTANT = 49713.88;
    
    // Properties
    /**
     * 
     * @return the molar mass of the fluid
     */
    double getMolarMass();
    
}
