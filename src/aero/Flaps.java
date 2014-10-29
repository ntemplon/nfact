/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aero;

import geometry.angle.Angle;
import geometry.angle.Angle.AngleType;

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
