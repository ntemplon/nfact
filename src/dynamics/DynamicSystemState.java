/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import function.Function;
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

    public static final DerivedProperty<DynamicSystemState, Double> SPEED = new DerivedProperty("Speed",
            new Function<DynamicSystemState, Double>() {
                @Override
                public Double evaluate(DynamicSystemState state) {
                    return Math.sqrt(state.get(X_VEL) * state.get(X_VEL) + state.get(Z_VEL) * state.get(Z_VEL));
                }
            }
    );

    public static final SystemProperty[] DYNAMIC_VARIABLES = new SystemProperty[]{
        X_ACCEL, Z_ACCEL, ANGULAR_ACCEL,
        X_VEL, Z_VEL, ANGULAR_VEL,
        X_POS, Z_POS, ANGULAR_POS,
        SPEED
    };

    // Properties
    /**
     *
     * @return the total speed in feet per second
     */
    public double getSpeed() {
        double speed = Math.sqrt(this.get(X_VEL) * this.get(X_VEL) + this.get(Z_VEL) * this.get(Z_VEL));
        this.set(SPEED, speed);
        return speed;
    }


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
