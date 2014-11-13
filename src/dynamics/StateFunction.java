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
 * @param <TValue>
 */
public class StateFunction<TState extends SystemState, TValue> implements Function<TState, TValue> {

    // Fields
    private final Function<TState, TValue> function;
    private final SystemProperty[] dependencies;
    
    
    // Properties
    public SystemProperty[] getDependencies() {
        return this.dependencies;
    }
    
    
    // Initialization
    public StateFunction(Function<TState, TValue> function, SystemProperty... dependencies) {
        this.function = function;
        this.dependencies = dependencies;
    }
    
    
    // Function Implementation
    @Override
    public  TValue evaluate(TState input) {
        return this.function.evaluate(input);
    }
    
}
