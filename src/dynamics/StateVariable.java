/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

/**
 *
 * @author nathant
 * @param <T>
 */
public class StateVariable<T> implements Comparable<StateVariable> {
    
    // Fields
    private final String name;
    
    
    // Properties
    public String getName() {
        return this.name;
    }
    
    
    // Initialization
    public StateVariable(String name) {
        this.name = name;
    }
    
    
    // Comparable Implementation
    @Override
    public int compareTo(StateVariable other) {
        return this.getName().compareTo(other.getName());
    }
    
}
