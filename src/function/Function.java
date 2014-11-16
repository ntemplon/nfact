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
