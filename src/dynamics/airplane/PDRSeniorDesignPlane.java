/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
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
package dynamics.airplane;

import aero.AerodynamicCoefficientModel;
import com.jupiter.ganymede.math.geometry.Angle;
import com.jupiter.ganymede.math.geometry.Angle.AngleType;
import com.jupiter.ganymede.math.geometry.Angle.MeasureRange;
import dynamics.AerodynamicSystem;
import static dynamics.AerodynamicSystem.CPM_FROM_A;
import dynamics.Inertia;
import dynamics.SystemState;
import dynamics.analysis.InertiaModel;
import propulsion.PropulsionForceModel;
import propulsion.rocket.HobbyRocketEngine;
import util.PhysicalConstants;

/**
 *
 * @author Nathan Templon
 */
public class PDRSeniorDesignPlane implements AerodynamicCoefficientModel, PropulsionForceModel, InertiaModel {

    // Constants (for the plane)
    private static final double BASE_MASS = 2.28 / PhysicalConstants.GRAVITY_ACCELERATION;
    private static final double AR = 4;

    private static final double CL_ALPHA = 4.97;
    private static final double CL_Q = 11.77;
    private static final double CL_DELTA_E = 0.3014;
    private static final double CD0 = 0.023;
    private static final double OSWALD_EFFICIENCY = 0.87;

    private static final double CPM0_TAKEOFF = -0.03048;
    private static final double CPM0_BURNOUT = -0.05026;
    private static final double CPM_ALPHA_TAKEOFF = -0.5479;
    private static final double CPM_ALPHA_BURNOUT = -0.9723;
    private static final double CPM_Q = -25.51;
    private static final double CPM_DELTA_E = 1.1708;

    private static final Angle ALPHA_ZERO_LIFT = new Angle(-2.11, AngleType.DEGREES);
    
    private static final Angle DELTA_E = new Angle(7.0, AngleType.DEGREES);
    
    
    // Fields
    private final HobbyRocketEngine engine = HobbyRocketEngine.G25_POST_BURN;
    private final Inertia inertia = new Inertia();
    
    
    // Initialization
    public PDRSeniorDesignPlane() {
        inertia.setMass(BASE_MASS + this.engine.getMass(0.0));
        inertia.setIxx(0.02043);
        inertia.setIyy(0.0764);
        inertia.setIzz(0.09646);
        inertia.setIxy(0.0001233);
        inertia.setIxz(-0.0009557);
    }


    // Aerodynamic Coefficient Functions
    @Override
    public double cl(SystemState state) {
        Angle totalAlpha = state.get(AerodynamicSystem.ANGLE_OF_ATTACK_GEOMETRIC).plus(ALPHA_ZERO_LIFT.times(-1.0));
        state.getProperties().put(AerodynamicSystem.ANGLE_OF_ATTACK_TOTAL, totalAlpha);
        double cl = CL_ALPHA * totalAlpha.getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus);

        double cle = DELTA_E.getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus) * CL_DELTA_E;
        cl += cle;
        
        double qHat = state.get(AerodynamicSystem.Q_HAT);
        cl += qHat * CL_Q;

        return cl;
    }

    @Override
    public double cd(SystemState state) {
        // Random constants are empiric adjustments to match AVL's data, based on the presence of two lifting surfaces, not one.
        double cdi = (this.cl(state) * this.cl(state) * (1.8 / 2.4) * (1.8 / 2.4) * 1.05 * 1.05) / (Math.PI * OSWALD_EFFICIENCY * AR);
        double cd = CD0 + cdi;
        return cd;
    }

    @Override
    public double csf(SystemState state) {
        return 0.0;
    }

    @Override
    public double cpm(SystemState state) {
        double motorBurnFrac = 1.0;
        if (this.engine.getBurnTime() > 0.0) {
            motorBurnFrac = 1 - (this.engine.getMass(state.getTime()) - this.engine.getMass(
                this.engine.getBurnTime())) / (this.engine.getMass(0.0) - this.engine.getMass(this.engine.getBurnTime()));
        }
        
        double cpm0 = CPM0_TAKEOFF + (CPM0_BURNOUT - CPM0_TAKEOFF) * motorBurnFrac;
        state.getProperties().put(AerodynamicSystem.CPM0, cpm0);

        double cpmAlpha = CPM_ALPHA_TAKEOFF + (CPM_ALPHA_BURNOUT - CPM_ALPHA_TAKEOFF) * motorBurnFrac;
        state.getProperties().put(AerodynamicSystem.CPMA, cpmAlpha);

        double cpmFromAlpha = cpmAlpha * state.get(AerodynamicSystem.ANGLE_OF_ATTACK_GEOMETRIC).getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus);
        state.getProperties().put(CPM_FROM_A, cpmFromAlpha);

        double qHat = state.get(AerodynamicSystem.Q_HAT);
        double cpmFromQ = CPM_Q * qHat;
        state.getProperties().put(AerodynamicSystem.CPM_FROM_Q, cpmFromQ);

        double cpmFromElevator = DELTA_E.getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus) * CPM_DELTA_E;
        
        double cpm = cpm0 + cpmFromAlpha + cpmFromElevator + cpmFromQ;

        return cpm;
    }

    @Override
    public double cym(SystemState state) {
        return 0.0;
    }

    @Override
    public double crm(SystemState state) {
        return 0.0;
    }

    
    // PropulsionForceModel
    @Override
    public double thrust(SystemState state) {
        return this.engine.getThrust(state.getTime());
    }

    
    // InertiaModel
    @Override
    public Inertia getInertia(double time) {
        this.inertia.setMass(BASE_MASS + this.engine.getMass(time));
        return this.inertia;
    }

}
