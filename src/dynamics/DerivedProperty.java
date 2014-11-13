/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import function.Function;

/**
 *
 * @author nathan
 * @param <TState>
 * @param <TType>
 */
public class DerivedProperty<TState extends SystemState, TType> extends SystemProperty<TType> {
    
    // Fields
    protected StateFunction<TState, TType> value;
    
    
    // Properties
    public SystemProperty[] getDependencies() {
        return this.value.getDependencies();
    }
    
    
    // Initialization
    public DerivedProperty(String name, StateFunction<TState, TType> value) {
        super(name);
        this.value = value;
    }
    
    
    // Public Methods
    public TType valueAt(TState state) {
        return this.value.evaluate(state);
    }
    
}
