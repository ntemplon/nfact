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

package function;

/**
 *
 * @author Nathan Templon
 * @param <TIn>
 * @param <TOut>
 */
public interface Function<TIn, TOut> {
    
    TOut evaluate(TIn input);
    
    
    public class FuncPoint implements Comparable {
        
        // Constants
        public static String VALUE_SEPARATION_STRING = ",";
        
        
        // Static Methods
        public static FuncPoint fromString(String line) {
            String value = line.trim();
            String[] parts = value.split(VALUE_SEPARATION_STRING);
            try {
                double x = Double.parseDouble(parts[0].trim());
                double y = Double.parseDouble(parts[1].trim());
                return new FuncPoint(x, y);
            }
            catch (NumberFormatException ex) {
                // Pass on and return a null value
            }
            return null;
        }
        
        
        // Private Members
        public final double x;
        public final  double y;
        
        // Initialization
        public FuncPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        
        // Getters and Setters
        @Deprecated
        public double x() {
            return x;
        }
        
        @Deprecated
        public double y() {
            return y;
        }

        
        // Public Methods
        @Override
        public int compareTo(Object o) {
            if (!(o instanceof FuncPoint)) {
                return 0;
            }
            FuncPoint other = (FuncPoint)o;
            return Double.compare(this.x(), other.x());
        }
    }
    
}
