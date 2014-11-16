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

package space;

import static util.Util.doubleEquals;
import exception.BuilderNotDefinedException;
import geometry.angle.Angle;
import geometry.angle.Angle.AngleType;
import geometry.angle.Angle.TrigFunction;
import vector.Vector;
import vector.Vector3D;

/**
 * A class representing orbital paths
 * @author Nathan Templon
 */
public class Orbit {
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Constructors  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A constructor that accepts the radius and velocity vectors of a point on the orbit to calculate the orbit from.
     * @param body
     * @param radius
     * @param velocity 
     */
    public Orbit(CelestialBody body, Vector3D radius, Vector3D velocity) {
        this.centralBody = body;
        
        // Calculation of orbital elements
        // Breaks with rectiliniear orbits
        Vector3D iHat = new Vector3D(1, 0, 0);
        Vector3D jHat = new Vector3D(0, 1, 0);
        Vector3D kHat = new Vector3D(0, 0, 1);
        
        // Calculate the magnitudes of the radius and velocity
        double rad = radius.magnitude();
        double vel = velocity.magnitude();
        
        // Calculate the semi major axis of the orbit
        semiMajorAxis = 1.0 / ( (2.0 / rad) - ((vel * vel) / centralBody.gravParam() ) );
        
        // Calculate angular momentum
        Vector3D angMomentum = radius.cross(velocity);
        inclination = new Angle(angMomentum.direction().dot(kHat), TrigFunction.COSINE);
        
        // Calculate eccentricity
        Vector eccVec = velocity.cross(angMomentum).add(radius.direction().scalarMultiply(-1.0 * centralBody.gravParam())).scalarMultiply(1.0 / centralBody.gravParam());
        eccentricity = eccVec.magnitude();
        
        // Calculate the right ascension of ascending node, big omega
        Vector nHat;
        if (!doubleEquals(angMomentum.getComponent(1), 0.0) || !doubleEquals(angMomentum.getComponent(2), 0.0)) {
            nHat = kHat.cross(angMomentum).direction();
        }
        else {
            nHat = kHat;
        }
        double cosBigOmega = nHat.dot(iHat);
        double nDotJ = nHat.dot(jHat);
        Angle bigOmega0 = new Angle(cosBigOmega, TrigFunction.COSINE);
        if (doubleEquals(inclination.getMeasure(), 0.0)) {
            rightAscOfAscendingNode = new Angle(0.0);
        }
        else if (nDotJ < Math.PI) {
            rightAscOfAscendingNode = bigOmega0;
        }
        else {
            rightAscOfAscendingNode = new Angle(2.0 * Math.PI - bigOmega0.getMeasure(), AngleType.RADIANS);
        }
        
        // Calculate the argument of periapsis, little omega
        double cosLittleOmega = nHat.dot(eccVec.direction());
        Angle littleOmega0 = new Angle(cosLittleOmega, TrigFunction.COSINE);
        double eDotK = eccVec.dot(kHat);
        if (doubleEquals(eccentricity, 0.0)) {
            argOfPeri = new Angle(0.0);
        }
        else if (eDotK >= 0.0) {
            argOfPeri = littleOmega0;
        }
        else {
            argOfPeri = new Angle(2.0 * Math.PI - littleOmega0.getMeasure(), AngleType.RADIANS);
        }
    }
    
    
    /**
     * A constructor that accepts the orbital elements of the orbit
     * @param body the central body about which the orbit is located
     * @param sma the semi major axis of the orbit, in km
     * @param ecc the eccentricity of the orbit
     * @param inc the inclination of the orbit
     * @param rAscAN the right ascension of ascending node of the orbit
     * @param argPer the argument of periapsis of the orbit
     */
    public Orbit(CelestialBody body, double sma, double ecc, Angle inc, Angle rAscAN, Angle argPer) {
        this.centralBody = body;
        this.semiMajorAxis = sma;
        this.eccentricity = ecc;
        this.inclination = inc;
        this.rightAscOfAscendingNode = rAscAN;
        this.argOfPeri = argPer;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Property Getters  --------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * Accesses the central body of the orbit
     * @return Returns the central body of the orbit.
     */
    public CelestialBody getCentralBody() {
        return centralBody;
    }
    
    
    /**
     * Accesses the semi major axis of the orbit
     * @return Returns the semi major axis of the orbit.
     */
    public double getSMA() {
        return semiMajorAxis;
    }
    
    
    /**
     * Accesses the eccentricity of the orbit
     * @return Returns the eccentricity of the orbit
     */
    public double getEcc() {
        return eccentricity;
    }
    
    
    /**
     * Accesses the inclination of the orbit
     * @return Returns the inclination of the orbit.
     */
    public Angle getInc() {
        return inclination;
    }
    
    
    /**
     * Accesses the argument of periapsis of the orbit
     * @return Returns the argument of periapsis of the orbit 
     */
    public Angle getArgPeri() {
        return argOfPeri;
    }
    
    
    /**
     * Accesses the right ascension of ascending node of the orbit
     * @return Returns the right ascension of ascending node of the orbit.
     */
    public Angle getRightAscOfAscNode() {
        return rightAscOfAscendingNode;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Caclulated Properties  ---------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * Calculates and returns the period of the orbit
     * @return Returns the period of the orbit, in seconds.
     */
    public double period() {
        return 2*Math.PI * Math.sqrt((semiMajorAxis * semiMajorAxis * semiMajorAxis) / (centralBody.gravParam()));
    }
    
    
    /**
     * Calculates the radius at a given location
     * @param f the true anomaly angle at the desired location
     * @return Returns the magnitude of the radius at the specified true anomaly location, in km.
     */
    public double radiusAt(Angle f) {
        return (semiMajorAxis * (1.0 - eccentricity * eccentricity)) / (1.0 + eccentricity * f.cos());
    }
    
    
    /**
     * Calculates the velocity at a given location
     * @param f the true anomaly angle at the desired location
     * @return Returns the magnitude of the velocity at the specified true anomaly location, in km/s.
     */
    public double velocityAt(Angle f) {
        return Math.sqrt(centralBody.gravParam() * (2.0 / radiusAt(f) - 1.0 / semiMajorAxis));
    }
    
    
    /**
     * Calculates the radius vector at a given location
     * @param f the true anomaly angle at the desired location
     * @return Returns the radius vector at the specified true anomaly location, in km.
     */
    public Vector3D radiusVectorAt(Angle f) {
        Angle theta = argOfPeri.add(f);
        
        Vector3D radiusDirection = new Vector3D(
            rightAscOfAscendingNode.cos() * theta.cos() - rightAscOfAscendingNode.sin() * theta.sin() * inclination.cos(),
            rightAscOfAscendingNode.sin() * theta.cos() + rightAscOfAscendingNode.cos() * theta.sin() * inclination.cos(),
            theta.sin() * inclination.sin()
        );
        
        return (Vector3D)radiusDirection.scalarMultiply(radiusAt(f));
    }
    
    
    /**
     * Calculates the velocity vector at a given location
     * @param f the true anomaly angle at the desired location
     * @return Returns the velocity vector at the specified true anomaly location, in km/s.
     */
    public Vector3D velocityVectorAt(Angle f) {
        double angMoment = Math.sqrt(centralBody.gravParam() * semiMajorAxis * (1 - eccentricity * eccentricity));
        double radialVel = centralBody.gravParam() * eccentricity * f.sin() * (1.0 / angMoment);
        double tangVel = angMoment / radiusAt(f);
        Vector3D radialDir = (Vector3D)radiusVectorAt(f).direction();
        
        Vector3D kHat = new Vector3D(0, 0, 1);
        
        Vector3D tangDir = (Vector3D)kHat.cross(radialDir).direction();
        
        return (Vector3D)radialDir.scalarMultiply(radialVel).add(tangDir.scalarMultiply(tangVel));
    }
    
    
    /**
     * Calculates the time from periapsis to the desired location
     * @param f the true anomaly angle at the desired location
     * @return Returns time from periapsis from the specified true anomaly location, in km.
     */
    public double timeFromPeri(Angle f) {
        // Only handles elipses now
        double cosEccAnom = (eccentricity + f.cos()) / (1 + eccentricity * f.cos());
        Angle eccAnom0 = new Angle(cosEccAnom, TrigFunction.COSINE);
        Angle eccAnom;
        if (f.getMeasure() < Math.PI) {
            eccAnom = eccAnom0;
        }
        else {
            eccAnom = new Angle(2.0 * Math.PI - eccAnom0.getMeasure());
        }
        
        Angle meanAnom = eccAnom.add(new Angle(eccAnom.sin() * -1.0 * eccentricity));
        
        double meanMotion = Math.sqrt(centralBody.gravParam() / (semiMajorAxis * semiMajorAxis * semiMajorAxis));
        
        return meanAnom.getMeasure() / meanMotion;
    }
    
    
    /**
     * Calculates the time of flight from the initial true anomaly angle to the final true anomaly angle
     * @param initTrueAnom the initial true anomaly angle
     * @param finalTrueAnom the final true anomaly angle
     * @return Returns the time of flight along the orbit from the initial to final true anomaly angle, in seconds.
     */
    public double timeBetween(Angle initTrueAnom, Angle finalTrueAnom) {
        double time = timeFromPeri(finalTrueAnom) - timeFromPeri(initTrueAnom);
        
        while (time < 0) {
            time += period();
        }
        
        return time;
    }
    
    
    /**
     * Calculates and returns which conic the orbit is.
     * @return Returns the conical classification of the orbit - circular, elliptical, parabolic, or hyperbolic.
     */
    public OrbitType orbitType() {
        if (doubleEquals(eccentricity, 0.0)) {
            return OrbitType.CIRCULAR;
        }
        else if (doubleEquals(eccentricity, 1.0)) {
            return OrbitType.PARABOLIC;
        }
        else if (eccentricity > 0.0 && eccentricity < 1.0) {
            return OrbitType.ELLIPTIC;
        }
        else if (eccentricity > 1.0) {
            return OrbitType.HYPERBOLIC;
        }
        else {
            throw new UnsupportedOperationException("The orbit type is not supported.");
        }
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Static Methods  ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    public static Angle trueAnomalyAt(CelestialBody body, Vector3D radius, Vector3D velocity) {
        Vector3D angularMomentum = radius.cross(velocity);
        double mu = body.gravParam();
        Vector3D eccVec = (Vector3D)velocity.cross(angularMomentum).add(radius.direction().scalarMultiply(-1.0 * mu)).scalarMultiply(1.0 / mu);
        double cosTrueAnom = radius.dot(eccVec) / (radius.magnitude() * eccVec.magnitude());
        
        double trueAnom0 = Math.acos(cosTrueAnom);
        
        double rDotV = radius.dot(velocity);
        if (rDotV > 0) {
            return new Angle(trueAnom0);
        }
        else {
            return new Angle(2 * Math.PI - trueAnom0);
        }
    }
    
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  State Variables  ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    
    private CelestialBody centralBody; // The central body of the orbit
    private double semiMajorAxis; // The semi major axis of the orbit
    private double eccentricity; // The eccentricity of the orbit
    private Angle inclination; // The inclination of the orbit
    private Angle rightAscOfAscendingNode; // The right ascension of ascending node of the orbit
    private Angle argOfPeri; // The argument of periapsis of the orbit
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Enumerations  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * An enumeration of the types of conic sections that the orbit could be
     */
    public enum OrbitType {
        CIRCULAR,
        ELLIPTIC,
        PARABOLIC,
        HYPERBOLIC
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Helper Classes  ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    
    /**
     * A class for easily building orbital elements
     */
    public static class OrbitBuilder {
        
        // -------------------------------------------------------------------------------------------------
        // ---------------------------  Constructors  ------------------------------------------------------
        // -------------------------------------------------------------------------------------------------
        
        /**
         * Default constructor - creates a new OrbitBuilder of definition type SCALAR_ELEMENT
         */
        public OrbitBuilder() {
            this.defType = OrbitDefinitionType.SCALAR_ELEMENT;
            init();
        }
        
        
        /**
         * Standard constructor - allows the user to specify the definition type
         * @param type the OrbitDefinitionType to use for this OrbitBuilder
         */
        public OrbitBuilder( OrbitDefinitionType type ) {
            this.defType = type;
            init();
        }

        
        
        
        
        
        // -------------------------------------------------------------------------------------------------
        // ---------------------------  Property Providers  ------------------------------------------------
        // -------------------------------------------------------------------------------------------------
        
        /**
         * Sets the central body for the orbit
         * @param body the central body of the orbit
         */
        public void setCentralBody(CelestialBody body) {
            this.centralBody = body;
            hasCentralBody = true;
        }
        
        
        /**
         * Sets the radius vector of the orbit
         * @param rad the radius vector of the orbit
         */
        public void setRadius(Vector3D rad) {
            this.radius = rad;
            hasRadius = true;
        }
        
        
        /**
         * Sets the velocity vector of the orbit
         * @param vel the velocity vector of the orbit
         */
        public void setVelocity(Vector3D vel) {
            this.velocity = vel;
            hasVelocity = true;
        }
        
        
        /**
         * Sets the semi major axis of the orbit
         * @param sma the semi major axis of the orbit
         */
        public void setSMA(double sma) {
            this.semiMajorAxis = sma;
            hasSemiMajorAxis = true;
        }
        
        
        /**
         * Sets the getEcc of the orbit
         * @param ecc the getEcc of the orbit
         */
        public void setEcc(double ecc) {
            this.eccentricity = ecc;
            hasEccentricity = true;
        }
        
        
        /**
         * Sets the getInc of the orbit
         * @param inc the getInc of the orbit
         */
        public void setInc(Angle inc) {
            this.inclination = inc;
            hasInclination = true;
        }
        
        
        /**
         * Sets the right ascension of ascending node for the orbit
         * @param rAscOFAscNode the right ascension of ascending node for the orbit
         */
        public void setRightAscOfAscNode(Angle rAscOFAscNode) {
            this.rightAscOfAscNode = rAscOFAscNode;
            hasRightAsc = true;
        }
        
        
        /**
         * Sets the argument of periapsis for the orbit
         * @param argOfPeri the argument of periapsis for the orbit
         */
        public void setArgOfPeri(Angle argOfPeri) {
            this.argOfPeri = argOfPeri;
            hasArgOfPeri = true;
        }
        
        
        
        
        
        // -------------------------------------------------------------------------------------------------
        // ---------------------------  Building Utilities  ------------------------------------------------
        // -------------------------------------------------------------------------------------------------
        
        /**
         * Checks if the Orbit is ready to be built
         * @return Returns true if the orbit is ready to be built, false if it is not.
         */
        public boolean readyToBuild() {
            if (defType == OrbitDefinitionType.SCALAR_ELEMENT) {
                return hasCentralBody && hasSemiMajorAxis && hasEccentricity && hasInclination
                        && hasRightAsc && hasArgOfPeri;
            }
            else if (defType == OrbitDefinitionType.VECTOR) {
                return hasCentralBody && hasRadius && hasVelocity;
            }
            else {
                return false;
            }
        }
        
        
        /**
         * Builds the orbit from the previously provided info.
         * @return Returns the assembled Orbit.
         * @throws BuilderNotDefinedException if the Orbit was not sufficiently characterized before calling the method.
         */
        public Orbit build() {
            if (readyToBuild()) {
                if (defType == OrbitDefinitionType.SCALAR_ELEMENT) {
                    return new Orbit(centralBody, semiMajorAxis, eccentricity, inclination, rightAscOfAscNode, argOfPeri);
                }
                else if (defType == OrbitDefinitionType.VECTOR) {
                    return new Orbit(centralBody, radius, velocity);
                }
                else {
                    throw new UnsupportedOperationException("Builder type \"" + defType.toString() + "\" is not supported.");
                }
            }
            else {
                throw new BuilderNotDefinedException();
            }
        }
                
        
        
        
        
        // -------------------------------------------------------------------------------------------------
        // ---------------------------  Internal Methods  --------------------------------------------------
        // -------------------------------------------------------------------------------------------------
        
        /**
         * Initiates the object
         */
        private void init() {
            
            // Indicates that none of the values have been defined
            hasCentralBody = false;
            if (defType == OrbitDefinitionType.SCALAR_ELEMENT) {
                hasSemiMajorAxis = false;
                hasEccentricity = false;
                hasInclination = false;
                hasRightAsc = false;
                hasArgOfPeri = false;
            }
            else if (defType == OrbitDefinitionType.VECTOR) {
                hasRadius = false;
                hasVelocity = false;
            }
        }
        
        
        
        
        // -------------------------------------------------------------------------------------------------
        // ---------------------------  Enumerations  ------------------------------------------------------
        // -------------------------------------------------------------------------------------------------
        
        /**
         * An enum of the types of ways to define an orbit using an OrbitBuilder
         */
        public enum OrbitDefinitionType {
            VECTOR,
            SCALAR_ELEMENT
        }
        
        
        
        
        
        // -------------------------------------------------------------------------------------------------
        // ---------------------------  State Variables  ---------------------------------------------------
        // -------------------------------------------------------------------------------------------------
        
        private OrbitDefinitionType defType; // The definition type of this OrbitBuilder
        private boolean reqMet; // Stores whether or not the OrbitBuilder has been completely defined
        
        // Universal type values and booleans
        private CelestialBody centralBody; // the central body of the orbit to be created
        private boolean hasCentralBody; // whether or not a central body has been provided
        
        // SCALAR_ELEMENT type values and booleans
        private double semiMajorAxis; // the semi major axis of the orbit to be created
        private boolean hasSemiMajorAxis; // whether or not a semi major axis has been provided
        private double eccentricity; // the eccentricity of the orbit to be created
        private boolean hasEccentricity; // whether or not an eccentricity has been provided
        private Angle inclination; // the inclination of the orbit to be created
        private boolean hasInclination; // whether or not an inclination has been provided
        private Angle rightAscOfAscNode; // the right ascension of ascending node of the orbit to be created
        private boolean hasRightAsc; // whether or not a right ascension of ascending node has been provided
        private Angle argOfPeri; // the argument of periapsis of the orbit to be created
        private boolean hasArgOfPeri; // whether or not an argument of periapsis has been provided
        
        // VECTOR type values and booleans
        private Vector3D radius; // the radius vector of the orbit to be created at a given position
        private boolean hasRadius; // whether or not a radius vector has been provided
        private Vector3D velocity; // the velocity vector of the orbit to be created at a given position
        private boolean hasVelocity; // whether or not a velocity vector has been provided
        
    }
    
}
