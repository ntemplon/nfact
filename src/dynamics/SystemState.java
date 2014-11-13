/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nathant
 */
public abstract class SystemState {

    // Constants
    public static final StateVariable<Double> TIME = new StateVariable("Time");


    // Fields
    private final Map<SystemProperty, Object> values;
    private final List<DerivedProperty> derived;


    // Properties
    public Collection<SystemProperty> getVariables() {
        return this.values.keySet();
    }

    public <T> T get(SystemProperty<T> variable) {
        if (variable == null || !this.values.containsKey(variable)) {
            return null;
        }
        return (T) this.values.get(variable);
    }

    public <T> void set(SystemProperty<T> variable, T value) {
        this.values.put(variable, value);

        this.updateDerivedVariables(variable);
    }


    // Initialization
    public SystemState(SystemProperty[] variables) {
        this.values = new HashMap<>();
        this.derived = new ArrayList<>();

        this.values.put(TIME, 0.0);
        for (SystemProperty variable : variables) {
            if (variable instanceof DerivedProperty) {
                this.derived.add((DerivedProperty) variable);
            }
            this.values.put(variable, null);
        }
    }

    public SystemState(SystemState other) {
        this.values = new HashMap<>();
        this.derived = new ArrayList<>();

        other.getVariables().stream().forEach((variable) -> {
            if (variable instanceof DerivedProperty) {
                this.derived.add((DerivedProperty) variable);
            }
            this.values.put(variable, other.get(variable));
        });
    }


    // Private Methods
    private void updateDerivedVariables(SystemProperty property) {
        this.derived.stream().forEach((variable) -> {
            try {
                List<SystemProperty> props = null;
                if (variable.getDependencies() != null) {
                    props = Arrays.asList(variable.getDependencies());
                }
                if (props == null || props.isEmpty() || props.contains(property)) {
                    Object value = variable.valueAt(this);
                    this.values.put(variable, value);
                    this.updateDerivedVariables(variable);
                }
            } catch (Exception ex) {
                this.values.put(variable, null);
            }
        });
    }

}
