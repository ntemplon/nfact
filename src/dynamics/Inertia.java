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
package dynamics;

/**
 *
 * @author Nathan Templon
 */
public class Inertia {
    
    // Fields
    private double mass;
    private double ixx;
    private double iyy;
    private double izz;
    private double ixy;
    private double ixz;
    private double iyz;
    
    
    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * @return the ixx
     */
    public double getIxx() {
        return ixx;
    }

    /**
     * @param ixx the ixx to set
     */
    public void setIxx(double ixx) {
        this.ixx = ixx;
    }

    /**
     * @return the iyy
     */
    public double getIyy() {
        return iyy;
    }

    /**
     * @param iyy the iyy to set
     */
    public void setIyy(double iyy) {
        this.iyy = iyy;
    }

    /**
     * @return the izz
     */
    public double getIzz() {
        return izz;
    }

    /**
     * @param izz the izz to set
     */
    public void setIzz(double izz) {
        this.izz = izz;
    }

    /**
     * @return the ixy
     */
    public double getIxy() {
        return ixy;
    }

    /**
     * @param ixy the ixy to set
     */
    public void setIxy(double ixy) {
        this.ixy = ixy;
    }

    /**
     * @return the ixz
     */
    public double getIxz() {
        return ixz;
    }

    /**
     * @param ixz the ixz to set
     */
    public void setIxz(double ixz) {
        this.ixz = ixz;
    }

    /**
     * @return the iyz
     */
    public double getIyz() {
        return iyz;
    }

    /**
     * @param iyz the iyz to set
     */
    public void setIyz(double iyz) {
        this.iyz = iyz;
    }
    
    
    // Initialization
    public Inertia() {
        
    }
    
}
