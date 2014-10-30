/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

/**
 *
 * @author nathant
 * @param <TState>
 */
public abstract class SystemState<TState extends SystemState> {
    
    // Fields
    private double time;
    
    
    // Properties
    /**
     * @return the time in seconds
     */
    public double getTime() {
        return time;
    }

    /**
     * @param time the time in seconds to set
     */
    public void setTime(double time) {
        this.time = time;
    }
    
}
