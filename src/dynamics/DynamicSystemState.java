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

import geometry.angle.Angle;
import util.ArrayUtil;

/**
 *
 * @author nathant
 */
public class DynamicSystemState extends SystemState {

    // Constants
    public static final StateVariable<Double> X_ACCEL = new StateVariable("X Acceleration");
    public static final StateVariable<Double> Z_ACCEL = new StateVariable("Z Acceleration");
    public static final StateVariable<Double> ANGULAR_ACCEL = new StateVariable("Angular Acceleration");

    public static final StateVariable<Double> X_VEL = new StateVariable("X Velocity");
    public static final StateVariable<Double> Z_VEL = new StateVariable("Z Velocity");
    public static final StateVariable<Double> ANGULAR_VEL = new StateVariable("Angular Velocity");

    public static final StateVariable<Double> X_POS = new StateVariable("X Position");
    public static final StateVariable<Double> Z_POS = new StateVariable("Z Position");
    public static final StateVariable<Angle> ANGULAR_POS = new StateVariable("Angular Position");
    
    public static final StateVariable<Double> MASS = new StateVariable("Mass");

    public static final DerivedProperty<DynamicSystemState, Double> SPEED = new DerivedProperty("Speed", new StateFunction<>(
            (DynamicSystemState state) -> Math.sqrt(state.get(X_VEL) * state.get(X_VEL) + state.get(Z_VEL) * state.get(Z_VEL)),
            new SystemProperty[]{X_VEL, Z_VEL}));

    public static final SystemProperty[] DYNAMIC_VARIABLES = new SystemProperty[]{
        X_ACCEL, Z_ACCEL, ANGULAR_ACCEL,
        X_VEL, Z_VEL, ANGULAR_VEL,
        X_POS, Z_POS, ANGULAR_POS,
        SPEED
    };

    // Initialization
    public DynamicSystemState() {
        super(DYNAMIC_VARIABLES);
    }

    public DynamicSystemState(SystemProperty[] variables) {
        super(ArrayUtil.concat(variables, DYNAMIC_VARIABLES));
    }

    public DynamicSystemState(DynamicSystemState other) {
        super(other);
    }

}
