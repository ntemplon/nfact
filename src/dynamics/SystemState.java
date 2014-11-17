/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
    public static final StateVariable<Double> TIME = new StateVariable<>("Time");


    // Fields
    private final Map<SystemProperty, Object> values;
    private final List<DerivedProperty> derived;


    // Properties
    public Collection<SystemProperty> getVariables() {
        return this.values.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(SystemProperty<T> variable) {
        if (variable == null || !this.values.containsKey(variable)) {
            return null;
        }
        try {
            return (T) this.values.get(variable);
        }
        catch (Exception ex) {
            return null;
        }
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
            }
            catch (Exception ex) {
                this.values.put(variable, null);
            }
        });
    }

}
