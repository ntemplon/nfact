/*
 * Copyright (C) 2014 Nathan Templon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package space;

import geometry.angle.Angle;
import javax.swing.JOptionPane;
import log.Logger;
import space.Orbit.OrbitBuilder;
import space.Orbit.OrbitBuilder.OrbitDefinitionType;
import vector.Vector3D;

/**
 *
 * @author Nathan Templon
 */
public class LambertSolver {
    
    public LambertSolver(OrbitPosition orbit1, OrbitPosition orbit2) {
        this.orbit1 = orbit1;
        this.orbit2 = orbit2;
        revs = 0;
    }
    
    
    private void calcAMin() {
        theta = orbit1.radiusVector().angleTo(orbit2.radiusVector());
        chord = Math.sqrt(orbit1.radius() * orbit1.radius() + orbit2.radius() * orbit2.radius() - 2.0 * orbit1.radius() * orbit2.radius() * theta.cos());
        semiPerim = (orbit1.radius() + orbit2.radius() + chord) / 2.0;
        aMin = semiPerim / 2.0;
    }
    
    
    public double aMin() {
        calcAMin();
        return aMin;
    }
    
    
    private void calcAngles(double sma, boolean greaterThanTm) {
        double alpha0 = 2.0 * Math.asin(Math.sqrt(semiPerim / (2.0 * sma)));
        if (greaterThanTm) {
            alpha = new Angle((2.0 * Math.PI) - alpha0);
        }
        else {
            alpha = new Angle(alpha0);
        }
        
        double beta0 = 2.0 * Math.asin(Math.sqrt( (semiPerim - chord) / (2.0 * sma)));
        if (theta.getMeasure(Angle.AngleType.RADIANS) > Math.PI) {
            beta = new Angle((2.0 * Math.PI) - beta0);
        }
        else {
            beta = new Angle(beta0);
        }
    }
    
    
    private void calcVelVecs(double sma) {
        if (theta.equals(new Angle(Math.PI))) {
            double radius1 = firstRadius.magnitude();
            double radius2 = finalRadius.magnitude();
            
            Vector3D firstVelDir = (Vector3D)orbit1.velocityVector().direction();
            Vector3D secondVelDir = (Vector3D)orbit2.velocityVector().direction();
            
            double firstVelMag = Math.sqrt(orbit1.getCentralBody().gravParam() * (2 / radius1 - 2 / (radius1 + radius2)));
            double secondVelMag = Math.sqrt(orbit1.getCentralBody().gravParam() * (2 / radius2 - 2 / (radius1 + radius2)));
            
            firstVelocity = (Vector3D)firstVelDir.scalarMultiply(firstVelMag);
            finalVelocity = (Vector3D)secondVelDir.scalarMultiply(secondVelMag);
        }
        else {
            double a = Math.sqrt(orbit1.getCentralBody().gravParam() / (4.0 * sma)) * alpha.scalarMultiply(0.5).cot();
            double b = Math.sqrt(orbit1.getCentralBody().gravParam() / (4.0 * sma)) * beta.scalarMultiply(0.5).cot();
            
            Vector3D uC = (Vector3D)finalRadius.add(firstRadius.scalarMultiply(-1.0)).direction();
            Vector3D uR1 = (Vector3D)firstRadius.direction();
            Vector3D uR2 = (Vector3D)finalRadius.direction();
            
            firstVelocity = (Vector3D)uC.scalarMultiply(b + a).add(uR1.scalarMultiply(b-a));
            finalVelocity = (Vector3D)uC.scalarMultiply(b + a).add(uR2.scalarMultiply(a-b));
        }
    }
    
    
    public void setRevs(int revs) {
        this.revs = revs;
    }
    
    
    private void calcProps(double sma, boolean greaterThanTm) {
        firstRadius = orbit1.radiusVector();
        finalRadius = orbit2.radiusVector();
        calcAMin();
        calcAngles(sma, greaterThanTm);
        calcVelVecs(sma);
        orbitalPath = new Orbit(orbit1.getCentralBody(), firstRadius, firstVelocity);
    }
    
    
    public LambertSolution solution(double sma, boolean greaterThanTm) {
        if (sma < aMin()) {
            throw new UnsupportedOperationException("Cannot handle parabolic or hyperbolic transfers.");
        }
        calcProps(sma, greaterThanTm);
        return new LambertSolution(orbitalPath, orbit1, orbit2, firstVelocity, finalVelocity, revs);
    }
    
    
    
    
    private OrbitPosition orbit1;
    private OrbitPosition orbit2;
    
    private int revs;
    
    private double aMin;
    private double semiPerim;
    private double chord;
    private Angle theta;
    private Angle alpha;
    private Angle beta;
    
    private Orbit orbitalPath;
    
    private Vector3D firstRadius;
    private Vector3D finalRadius;
    private Vector3D firstVelocity;
    private Vector3D finalVelocity;
    
    private static Logger logger = Logger.getDefaultLogger();
    
    
    public static class LambertSolution {
        public LambertSolution(Orbit orbitalPath, OrbitPosition orbit1, OrbitPosition orbit2, Vector3D firstVel, Vector3D secondVel, int revs) {
            this.orbitalPath = orbitalPath;
            this.orbit1 = orbit1;
            this.orbit2 = orbit2;
            this.revs = revs;
            this.firstVelocity = firstVel;
            this.secondVelocity = secondVel;
            
            calculateProperties();
        }
        
        
        /**
         * Accesses the orbital path followed by the Lambert's solution
         * @return Returns the orbital path followed by the Lambert's solution.
         */
        public Orbit orbitalPath() {
            return orbitalPath;
        }
        
        
        /**
         * Accesses the initial orbit position for the Lambert's solution
         * @return Returns the initial orbit position for the Lambert's solution.
         */
        public OrbitPosition initialPosition() {
            return orbit1;
        }
        
        
        /**
         * Accesses the final orbit position for the Lambert's solution
         * @return Returns the final orbit position for the Lambert's solution.
         */
        public OrbitPosition finalPosition() {
            return orbit2;
        }
        
        
        /**
         * Accesses the number of intermediate revolutions for the Lambert's solution
         * @return Returns the number of intermediate revolutions for the Lambert's solution.
         */
        public int revolutions() {
            return revs;
        }
        
        
        public double timeOfFlight() {
            return timeOfFlight;
        }
        
        
        private void calculateProperties() {
            Vector3D initialRadius = orbit1.radiusVector();
            Vector3D finalRadius = orbit2.radiusVector();
            
            OrbitBuilder builder = new OrbitBuilder(OrbitDefinitionType.VECTOR);
            builder.setCentralBody(orbit1.getCentralBody());
            builder.setRadius(initialRadius);
            builder.setVelocity(firstVelocity);
            orbitalPath = builder.build();
            
            Angle firstTrueAnom = Orbit.trueAnomalyAt(orbit1.getCentralBody(), initialRadius, firstVelocity);
            Angle secondTrueAnom = Orbit.trueAnomalyAt(orbit2.getCentralBody(), finalRadius, secondVelocity);
            
            timeOfFlight = orbitalPath.timeBetween(firstTrueAnom, secondTrueAnom) + orbitalPath.period() * revs;
        }
        
        
        private Orbit orbitalPath;
        private final OrbitPosition orbit1;
        private final OrbitPosition orbit2;
        private final int revs;
        
        private Angle initialTrueAnomaly;
        private Angle finalTrueAnomaly;
        private double timeOfFlight;
        
        private final Vector3D firstVelocity;
        private final Vector3D secondVelocity;
    }
    
}
