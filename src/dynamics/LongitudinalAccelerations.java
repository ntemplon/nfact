/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

/**
 *
 * @author nathant
 */
public class LongitudinalAccelerations {
    
    // Fields
    private double xAcceleration;
    private double zAcceleration;
    private double angularAcceleration;
    
    
    // Properties
    /**
     * @return the xAcceleration
     */
    public double getXAcceleration() {
        return xAcceleration;
    }

    /**
     * @param xAcceleration the xAcceleration to set
     */
    public void setXAcceleration(double xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    /**
     * @return the yAcceleration
     */
    public double getZAcceleration() {
        return zAcceleration;
    }

    /**
     * @param yAcceleration the yAcceleration to set
     */
    public void setZAcceleration(double yAcceleration) {
        this.zAcceleration = yAcceleration;
    }

    /**
     * @return the angularAcceleration
     */
    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    /**
     * @param angularAcceleration the angularAcceleration to set
     */
    public void setAngularAcceleration(double angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }
    
    
    // Initialization
    public LongitudinalAccelerations() {
        
    }
    
}
