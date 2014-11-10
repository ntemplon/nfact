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
public class DerivedVariable<TState extends SystemState, TType> extends StateVariable<TType> {
    
    // Fields
    protected Function<TState, TType> value;
    
    
    // Initialization
    public DerivedVariable(String name, Function<TState, TType> value) {
        super(name);
        this.value = value;
    }
    
    
    // Public Methods
    public TType valueAt(TState state) {
        return this.value.evaluate(state);
    }
    
}
