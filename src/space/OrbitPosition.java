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

import static Util.Util.doubleEquals;
import geometry.angle.Angle;
import geometry.angle.Angle.TrigFunction;
import javax.swing.JOptionPane;
import vector.Vector3D;

/**
 *
 * @author Nathan Templon
 */
public class OrbitPosition {
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Constructors  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A constructor that accepts a central body, a radius vector, and a velocity vector
     * @param centralBody the central body of the orbit
     * @param radius the radius vector, in km
     * @param velocity the velocity vector, in km
     */
    public OrbitPosition(CelestialBody centralBody, Vector3D radius, Vector3D velocity) {
        this.orbit = new Orbit(centralBody, radius, velocity);
        
        // Calculation of True Anomaly Angle
        Vector3D angularMomentum = radius.cross(velocity);
        double mu = centralBody.gravParam();
        Vector3D eccVec = (Vector3D)velocity.cross(angularMomentum).add(radius.direction().scalarMultiply(-1.0 * mu)).scalarMultiply(1.0 / mu);
        double cosTrueAnom = radius.dot(eccVec) / (radius.magnitude() * eccVec.magnitude());
        
        // Correct for double precision errors
        if (doubleEquals(cosTrueAnom, 1.0)) {
            cosTrueAnom = 1.0;
        }
        else if (doubleEquals(cosTrueAnom, -1.0)) {
            cosTrueAnom = -1.0;
        }
        else if (doubleEquals(cosTrueAnom, 0.0)) {
            cosTrueAnom = 0.0;
        }
        
        double rDotV = radius.dot(velocity);
        if (rDotV > 0) {
            trueAnomaly = new Angle(cosTrueAnom, TrigFunction.COSINE);
        }
        else {
            trueAnomaly = new Angle(2 * Math.PI - Math.acos(cosTrueAnom));
        }
    }
    
    
    /**
     * A constructor that accepts a central body, a radius vector, a velocity vector, and a time
     * @param centralBody the central body of the orbit
     * @param radius the radius vector at the provided time, in km
     * @param velocity the velocity vector at the provided time, in km/s
     * @param time the time at which the information provided is valid, in seconds
     */
    public OrbitPosition(CelestialBody centralBody, Vector3D radius, Vector3D velocity, double time) {
        this.orbit = new Orbit(centralBody, radius, velocity);
        
        // Calculation of True Anomaly Angle
        Vector3D angularMomentum = radius.cross(velocity);
        double mu = centralBody.gravParam();
        Vector3D eccVec = (Vector3D)velocity.cross(angularMomentum).add(radius.direction().scalarMultiply(-1.0 * mu)).scalarMultiply(1.0 / mu);
        double cosTrueAnom = radius.dot(eccVec) / (radius.magnitude() * eccVec.magnitude());
        
        // Correct for double precision errors
        if (doubleEquals(cosTrueAnom, 1.0)) {
            cosTrueAnom = 1.0;
        }
        else if (doubleEquals(cosTrueAnom, -1.0)) {
            cosTrueAnom = -1.0;
        }
        else if (doubleEquals(cosTrueAnom, 0.0)) {
            cosTrueAnom = 0.0;
        }
        
        double rDotV = radius.dot(velocity);
        if (rDotV > 0) {
            trueAnomaly = new Angle(cosTrueAnom, TrigFunction.COSINE);
        }
        else {
            trueAnomaly = new Angle(2 * Math.PI - Math.acos(cosTrueAnom));
        }
        JOptionPane.showMessageDialog(null, trueAnomaly.getMeasure());
    }
    
    
    /**
     * A constructor that accepts and orbit and a true anomaly angle
     * @param orbit the orbit that this orbit position is on
     * @param trueAnomaly the true anomaly on the orbit that this position represents
     */
    public OrbitPosition(Orbit orbit, Angle trueAnomaly) {
        this.orbit = orbit;
        this.trueAnomaly = trueAnomaly;
    }
    
    
    /**
     * A constructor that accepts and orbit, a true anomaly angle, and a time
     * @param orbit the orbit that this orbit position is on
     * @param trueAnomaly the true anomaly on the orbit that this position represents
     * @param time the time that this information is valid for, in seconds
     */
    public OrbitPosition(Orbit orbit, Angle trueAnomaly, double time) {
        this.orbit = orbit;
        this.trueAnomaly = trueAnomaly;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Getters and Setters  -----------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    public void setTrueAnomaly(Angle trueAnomaly) {
        this.trueAnomaly = trueAnomaly;
    }
    
    
    public Orbit getOrbit() {
        return orbit;
    }
    
    
    public Angle getTrueAnomaly() {
        return trueAnomaly;
    }
    
    
    public CelestialBody getCentralBody() {
        return orbit.getCentralBody();
    }
    
    
    public double getSMA() {
        return orbit.getSMA();
    }
    
    
    public double getEcc() {
        return orbit.getEcc();
    }
    
    
    public Angle getInc() {
        return orbit.getInc();
    }
    
    
    public Angle getArgPeri() {
        return orbit.getArgPeri();
    }
    
    
    public Angle getRightAscOfAscNode() {
        return orbit.getRightAscOfAscNode();
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Calculated Values  -------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    public double radius() {
        return orbit.radiusAt(trueAnomaly);
    }
    
    
    public Vector3D radiusVector() {
        return orbit.radiusVectorAt(trueAnomaly);
    }
    
    
    public double velocity() {
        return Math.sqrt(orbit.getCentralBody().gravParam() * (2.0 / radius() - 1.0 / orbit.getSMA()));
    }
    
    
    public Vector3D velocityVector() {
        return orbit.velocityVectorAt(trueAnomaly);
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  State Variables  ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    private Orbit orbit;
    private Angle trueAnomaly;
    
    private double timeOffset;
    
}
