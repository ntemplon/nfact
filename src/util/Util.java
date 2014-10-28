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

package Util;

/**
 *
 * @author Nathan Templon
 */
public class Util {
    
    public static boolean doubleEquals(double d1, double d2) {
       double diff = Math.abs(d1 - d2);
       
       double smaller = Math.min(Math.abs(d1), Math.abs(d2));
       if (smaller == 0) {
           smaller = 10E-54;
       }
       double margin = Math.abs(smaller * 10E-6);
       
       return diff < margin;
    }
    
}
