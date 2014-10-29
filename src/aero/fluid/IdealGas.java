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
public class IdealGas implements Fluid {
    
    // Fields
    private final double molarMass;
    private final double heatRatio;
    private final double gasConstant;
    
    // Properties
    @Override
    public double getMolarMass() {
        return this.molarMass;
    }
    
    public double getHeatRatio() {
        return this.heatRatio;
    }
    
    public double getGasConstant() {
        return this.gasConstant;
    }
    
    // Initialization
    public IdealGas(double molarMass, double heatRatio) {
        this.molarMass = molarMass;
        this.heatRatio = heatRatio;
        
        this.gasConstant = Fluid.GAS_CONSTANT / this.molarMass;
    }
    
}
