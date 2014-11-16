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
package propulsion.rocket;

import function.Function;
import function.SingleVariableFunction;
import function.SingleVariableTableFunction;

/**
 *
 * @author nathant
 */
public class HobbyRocketEngine implements SolidRocketEngine {
    
    // Constants
    public static final double NEWT0NS_TO_POUNDS = 0.224808943;

    public static final HobbyRocketEngine G78 = new HobbyRocketEngine("G78",
            new SingleVariableTableFunction(0.0, 1.808, new Function.FuncPoint[]{
                new Function.FuncPoint(0.0, 0.0),
                new Function.FuncPoint(0.006, 0.260),
                new Function.FuncPoint(0.008, 1.684),
                new Function.FuncPoint(0.010, 7.589),
                new Function.FuncPoint(0.012, 14.522),
                new Function.FuncPoint(0.014, 14.148),
                new Function.FuncPoint(0.016, 13.225),
                new Function.FuncPoint(0.018, 16.841),
                new Function.FuncPoint(0.020, 19.110),
                new Function.FuncPoint(0.022, 20.482),
                new Function.FuncPoint(0.026, 21.130),
                new Function.FuncPoint(0.028, 22.128),
                new Function.FuncPoint(0.032, 21.953),
                new Function.FuncPoint(0.038, 22.975),
                new Function.FuncPoint(0.074, 21.878),
                new Function.FuncPoint(0.124, 21.454),
                new Function.FuncPoint(0.376, 22.327),
                new Function.FuncPoint(0.680, 22.352),
                new Function.FuncPoint(0.994, 20.606),
                new Function.FuncPoint(1.246, 18.661),
                new Function.FuncPoint(1.282, 13.923),
                new Function.FuncPoint(1.316, 13.923),
                new Function.FuncPoint(1.360, 10.033),
                new Function.FuncPoint(1.424, 6.542),
                new Function.FuncPoint(1.504, 4.771),
                new Function.FuncPoint(1.598, 4.347),
                new Function.FuncPoint(1.656, 3.674),
                new Function.FuncPoint(1.676, 1.145),
                new Function.FuncPoint(1.678, 0.312),
                new Function.FuncPoint(1.714, 1.145),
                new Function.FuncPoint(1.734, 0.312),
                new Function.FuncPoint(1.808, 0)
            })
    );

    public static final HobbyRocketEngine G25 = new HobbyRocketEngine("G25",
            new SingleVariableTableFunction(0.0, 5.3, new Function.FuncPoint[]{
                new Function.FuncPoint(0.0, 0.0),
                new Function.FuncPoint(0.035, 30.99 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.047, 36.712 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.059, 41.18 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.13, 40.669 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.177, 38.969 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.295, 38.969 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.343, 40.947 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.413, 40.38 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.437, 38.69 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.484, 39.824 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.532, 37.845 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.65, 37.557 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.721, 38.969 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.803, 38.69 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.85, 37.279 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(0.98, 39.535 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(1.063, 36.434 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(1.098, 38.124 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(1.252, 37.845 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(1.37, 37.279 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(1.583, 37.000 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(1.819, 35.3 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(1.984, 33.61 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(2.185, 31.344 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(2.315, 28.809 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(2.622, 24.286 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(3.024, 18.917 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(3.39, 13.838 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(3.839, 7.624 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(4.323, 4.518 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(4.783, 2.541 * NEWT0NS_TO_POUNDS),
                new Function.FuncPoint(5.3, 0)
            })
    );
    
    
    // Fields
    private final String name;
    private final double burnTime;
    private final SingleVariableFunction thrust;
    
    
    // Properties
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public double getBurnTime() {
        return this.burnTime;
    }
    
    
    // Initialization
    public HobbyRocketEngine(String name, SingleVariableFunction thrust) {
        this.name = name;
        this.thrust = thrust;
        
        // Calculate burn time
        if (this.thrust.hasFiniteDomain) {
            this.burnTime = this.thrust.domainMax - this.thrust.domainMin;
        }
        else {
            this.burnTime = 0.0;
        }
    }
    
    
    // Public Methods
    /**
     * 
     * @param time the time since ignition, in seconds
     * @return the thrust of the engine, in pounds
     */
    @Override
    public double getThrust(double time) {
        if (thrust.hasFiniteDomain) {
            if (time < thrust.domainMin || thrust.domainMax < time) {
                return 0;
            }
        }
        return thrust.evaluate(time);
    }
    
    public HobbyRocketEngine getThrustVariationEngine(double variation) {
        String newName = this.getName() + " THRUST_VAR_" + variation;
        SingleVariableFunction newThrust = new SingleVariableFunction(this.thrust.domainMin, this.thrust.domainMax) {
            @Override
            public Double evaluate(Double input) {
                return HobbyRocketEngine.this.thrust.evaluate(input) * variation;
            }
        };
        
        return new HobbyRocketEngine(newName, newThrust);
    }
    
}
