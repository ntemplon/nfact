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
public class IdealGasState extends FluidState {
    
    // Fields
    protected final IdealGas idealFluid;
    private final double pressure;
    private final double temperature;
    private final double density;
    private final double speedOfSound;
    
    // Properties
    @Override
    public double getDensity() {
        return this.density;
    }

    @Override
    public double getTemperature() {
        return this.temperature;
    }

    @Override
    public double getPressure() {
        return this.pressure;
    }

    @Override
    public double getGasConstant() {
        return this.idealFluid.getGasConstant();
    }

    @Override
    public double getHeatRatio() {
        return this.idealFluid.getHeatRatio();
    }
    
    @Override
    public double getSpeedOfSound() {
        return this.speedOfSound;
    }
    
    // Initialization
    /**
     * 
     * @param fluid the fluid
     * @param temperature the temperature of the fluid, in degrees Rankine
     * @param pressure the static pressure of the fluid, in pounds per square foot
     */
    public IdealGasState(IdealGas fluid, double temperature, double pressure) {
        super(fluid);
        this.idealFluid = fluid;
        this.temperature = temperature;
        this.pressure = pressure;
        
        this.density = pressure / (temperature * this.idealFluid.getGasConstant());
        this.speedOfSound = Math.sqrt(this.idealFluid.getGasConstant() * temperature);
    }
    
}
