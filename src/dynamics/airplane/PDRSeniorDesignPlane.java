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
    private static final double BASE_MASS = (2.84 - 0.166 + 0.25) / PhysicalConstants.GRAVITY_ACCELERATION;
    private static final double AR = 4;

    private static final double CL_ALPHA = 4.97;
    private static final double CL_Q = 11.77;
    private static final double CL_DELTA_E = 0.3014;
    
    private static final double CD0 = 0.023;
    private static final double OSWALD_EFFICIENCY = 0.87;

    private static final double CPM0_TAKEOFF = -0.11804;
    private static final double CPM0_BURNOUT = -0.13659;
    private static final double CPM_ALPHA_TAKEOFF = -0.4179;
    private static final double CPM_ALPHA_BURNOUT = -0.9723;
    private static final double CPM_Q = -25.51;
    private static final double CPM_DELTA_E = 1.1708;
    
    private static final double CSF_BETA = -0.5737;
    private static final double CSF_ROLL = -0.0181;
    private static final double CSF_YAW = 0.3272;
    
    private static final double CYM_BETA = 0.107;
    private static final double CYM_ROLL = -0.0017;
    private static final double CYM_YAW = -0.2138;
    
    private static final double CRM_BETA = -0.0501;
    private static final double CRM_ROLL = -0.3654;
    private static final double CRM_YAW = 0.08013;

    private static final Angle ALPHA_ZERO_LIFT = new Angle(-2.11, AngleType.DEGREES);
    
    
    // Fields
    private final HobbyRocketEngine engine = HobbyRocketEngine.G25;
//    private final HobbyRocketEngine engine = HobbyRocketEngine.G25_POST_BURN;
    private final Inertia inertia = new Inertia();
    private Angle deltaE = new Angle(0.0, AngleType.DEGREES);
    
    
    // Properties
    public Angle getDeltaE() {
        return this.deltaE;
    }
    
    public void setDeltaE(Angle deltaE) {
        this.deltaE = deltaE;
    }
    
    
    // Initialization
    public PDRSeniorDesignPlane() {
        inertia.setMass(BASE_MASS + this.engine.getMass(0.0));
        inertia.setIxx(0.011801);
        inertia.setIyy(0.056055);
        inertia.setIzz(0.0671182);
        inertia.setIxy(0.0000064);
        inertia.setIxz(-0.0005178);
    }


    // Aerodynamic Coefficient Functions
    @Override
    public double cl(SystemState state) {
        Angle totalAlpha = state.get(AerodynamicSystem.ANGLE_OF_ATTACK_GEOMETRIC).plus(ALPHA_ZERO_LIFT.times(-1.0));
        state.getProperties().put(AerodynamicSystem.ANGLE_OF_ATTACK_TOTAL, totalAlpha);
        
        double cl = CL_ALPHA * totalAlpha.getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus);

        double cle = this.getDeltaE().getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus) * CL_DELTA_E;
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
        double beta = state.get(AerodynamicSystem.SIDESLIP_ANGLE).getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus);
        
        double csf = beta * CSF_BETA;
        csf += CSF_ROLL * state.get(AerodynamicSystem.ROLL_RATE);
        csf += CSF_YAW * state.get(AerodynamicSystem.YAW_RATE);
        
        if (Math.abs(csf) < 1e-6) {
            csf = 0.0;
        }
        
        return csf;
    }

    @Override
    public double cpm(SystemState state) {
        double motorBurnFrac = this.getBurnFraction(state.getTime());
        
        double cpm0 = CPM0_TAKEOFF + (CPM0_BURNOUT - CPM0_TAKEOFF) * motorBurnFrac;
        state.getProperties().put(AerodynamicSystem.CPM0, cpm0);

        double cpmAlpha = CPM_ALPHA_TAKEOFF + (CPM_ALPHA_BURNOUT - CPM_ALPHA_TAKEOFF) * motorBurnFrac;
        state.getProperties().put(AerodynamicSystem.CPMA, cpmAlpha);

        double cpmFromAlpha = cpmAlpha * state.get(AerodynamicSystem.ANGLE_OF_ATTACK_GEOMETRIC).getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus);
        state.getProperties().put(CPM_FROM_A, cpmFromAlpha);

        double qHat = state.get(AerodynamicSystem.Q_HAT);
        double cpmFromQ = CPM_Q * qHat;
        state.getProperties().put(AerodynamicSystem.CPM_FROM_Q, cpmFromQ);

        double cpmFromElevator = this.getDeltaE().getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus) * CPM_DELTA_E;
        
        double cpm = cpm0 + cpmFromAlpha + cpmFromElevator + cpmFromQ;
        
        if (Math.abs(cpm) < 1e-6) { 
            cpm = 0.0;
        }

        return cpm;
    }

    @Override
    public double cym(SystemState state) {
        double beta = state.get(AerodynamicSystem.SIDESLIP_ANGLE).getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus);
        
        double cym = beta * CYM_BETA;
        cym += CYM_ROLL * state.get(AerodynamicSystem.ROLL_RATE);
        cym += CYM_YAW * state.get(AerodynamicSystem.YAW_RATE);
        
        if (Math.abs(cym) < 1e-6) {
            cym = 0.0;
        }
        
        return cym;
    }

    @Override
    public double crm(SystemState state) {
        double beta = state.get(AerodynamicSystem.SIDESLIP_ANGLE).getMeasure(AngleType.RADIANS, MeasureRange.PlusMinus);
        
        double crm = beta * CRM_BETA;
        crm += CRM_ROLL * state.get(AerodynamicSystem.ROLL_RATE);
        crm += CRM_YAW * state.get(AerodynamicSystem.YAW_RATE);
        
        if (Math.abs(crm) < 1e-6) {
            crm = 0.0;
        }
        
        return crm;
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
    
    
    // Private Methods
    private double getBurnFraction(double time) {
        double initialMass = this.engine.getMass(0.0);
        double finalMass = this.engine.getMass(this.engine.getBurnTime());
        double currentMass = this.engine.getMass(time);
        
        double totalDifference = finalMass - initialMass;
        if (totalDifference == 0.0) {
            return 1.0;
        }
        
        double currentDifference = currentMass - initialMass;
        return Math.max(0.0, Math.min(1.0, currentDifference / totalDifference));
    }

}
