/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aero;

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
    public static double CLFlapPink(double CLFlap, double FlapDelf, double perCtlSface, double SFlap, double SRef){
        double dClfPERdCl;
        double dClfPERdDel;
        
        dClfPERdCl = 15.341* Math.pow(perCtlSface, 5) - 35.383* Math.pow(perCtlSface, 4) + 29.895* Math.pow(perCtlSface, 3) - 11.148*Math.pow(perCtlSface, 2) + 2.2949*perCtlSface;
        dClfPERdDel = -2.575 * perCtlSface + 2.575; 
        
        return (dClfPERdDel * FlapDelf + CLFlap * dClfPERdCl)*(SFlap/SRef);
    }
    
    
    // Hacky-Cheaty stuff
    private Flaps() {
        
    }
    
}
