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

package geometry;

/**
 * A general interface for any Plane Areas
 * @author Hortator
 */
public interface PlaneArea {
    
    /**
     * A method that returns the Area of the PlaneArea.
     * @return Returns the area as an Area object.
     */
    Area area();
    
    /**
     * A wrapper class used to designate doubles as areas
     */
    public class Area {
        
        /**
         * A default constructor that creates an Area with value '0'
         */
        public Area() {
            this.area = 0;
        }
        
        /**
         * A constructor that allows the user to specify the desired area.
         * @param area the double to wrap as an Area
         */
        public Area(double area) {
            this.area = area;
        }
        
        
        /**
         * A method for obtaining the numerical value of the Area.
         * @return Returns the numerical value of the area.
         */
        public double value() {
            return area;
        }
        
        
        /**
         * Returns a String representing the Area
         * @return Returns the numerical value of the area as a String
         */
        @Override
        public String toString() {
            return area + "";
        }
        
        private double area;
    }
    
}
