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
package aero;

import com.jupiter.ganymede.math.geometry.Angle;
import com.jupiter.ganymede.math.geometry.Angle.AngleType;

/**
 *
 * @author nathant
 */
public final class Flaps {
    /**
     * Math to determine the coefficient of lift of an infinite span simple flap
     * from Pinkerton as outlined in "Theory of Wing Sections". Note: the
     * lifting surface and flap must be constant chord, full span and be
     * unsweeped for this method.
     *
     * @param cLSurface value for the lift coefficient of the surface the flap
     * is on
     * @param flapDelf value for the deflection of the flap in radians
     * @param perCtlSface percentage of the lifting surface that is taken up by
     * the flap. Note: The area must be full span and have a constant chord
     * value.
     * @param sSurface Area of the the surface the flap is on
     * @param sRef Reference area for the vehicle
     * @return
     */
    public static double cLFlapPink(double cLSurface, Angle flapDelf, double perCtlSface, double sSurface, double sRef) {
        double dClfPERdCl;
        double dClfPERdDel;

        dClfPERdCl = 15.341 * Math.pow(perCtlSface, 5) - 35.383 * Math.pow(perCtlSface, 4) + 29.895 * Math.pow(perCtlSface, 3) - 11.148 * Math.pow(perCtlSface, 2) + 2.2949 * perCtlSface;
        dClfPERdDel = -2.575 * perCtlSface + 2.575;

        return (dClfPERdDel * flapDelf.getMeasure(AngleType.RADIANS) + cLSurface * dClfPERdCl) * (sSurface / sRef);
    }

    // Hacky-Cheaty stuff
    private Flaps() {

    }

}
