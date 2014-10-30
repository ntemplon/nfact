/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nathant
 * @param <TState>
 */
public abstract class SystemState{
    
    // Constants
    public static final StateVariable<Double> TIME = new StateVariable("Time");
    
    
    // Fields
    private final Map<StateVariable, Object> values;
    
    
    // Properties
    public Collection<StateVariable> getVariables() {
        return this.values.keySet();
    }
    
    public <T> T get(StateVariable<T> variable) {
        if (variable == null || !this.values.containsKey(variable)) {
            return null;
        }
        return (T)this.values.get(variable);
    }
    
    public <T> void set(StateVariable<T> variable, T value) {
        this.values.put(variable, value);
    }
    
    
    // Initialization
    public SystemState(StateVariable[] variables) {
        this.values = new HashMap<>();
        
        this.values.put(TIME, 0.0);
        for(StateVariable variable : variables) {
            this.values.put(variable, null);
        }
    }
    
    public SystemState(SystemState other) {
        this.values = new HashMap<>();
        
        other.getVariables().stream().forEach((variable) -> {
            this.values.put(variable, other.get(variable));
        });
    }
    
}
