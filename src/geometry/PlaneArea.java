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
