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

    // Static Methods
//    public static double cfcFor(double cf, double span) {
//        // Do math
//        return 4;
//    }
    //Math to determine the coefficient of lift of an infinite span simple flap from pinkerton as outlined in "Theory of Wing Sections"
    public static double cLFlapPink(double cLFlap, Angle flapDelf, double perCtlSface, double sFlap, double sRef) {
        double dClfPERdCl;
        double dClfPERdDel;

        dClfPERdCl = 15.341 * Math.pow(perCtlSface, 5) - 35.383 * Math.pow(perCtlSface, 4) + 29.895 * Math.pow(perCtlSface, 3) - 11.148 * Math.pow(perCtlSface, 2) + 2.2949 * perCtlSface;
        dClfPERdDel = -2.575 * perCtlSface + 2.575;

        return (dClfPERdDel * flapDelf.measure(AngleType.RADIANS) + cLFlap * dClfPERdCl) * (sFlap / sRef);
    }

    // Hacky-Cheaty stuff
    private Flaps() {

    }

}
