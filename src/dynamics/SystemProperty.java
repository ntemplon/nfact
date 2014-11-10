/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

/**
 *
 * @author nathan
 * @param <T>
 */
public abstract class SystemProperty<T> implements Comparable<SystemProperty> {
    
    // Fields
    private final String name;
    
    
    // Properties
    public String getName() {
        return this.name;
    }
    
    
    // Initialization
    public SystemProperty(String name) {
        this.name = name;
    }
    
    // Comparable Implementation
    @Override
    public int compareTo(SystemProperty other) {
        return this.getName().compareTo(other.getName());
    }
    
}
