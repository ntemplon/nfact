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

//    public static final HobbyRocketEngine G78 = new HobbyRocketEngine("G78",
//            new SingleVariableTableFunction(0.0, 1.808, new Function.FuncPoint[]{
//                new Function.FuncPoint(0.0, 0.0),
//                new Function.FuncPoint(0.006, 0.260),
//                new Function.FuncPoint(0.008, 1.684),
//                new Function.FuncPoint(0.010, 7.589),
//                new Function.FuncPoint(0.012, 14.522),
//                new Function.FuncPoint(0.014, 14.148),
//                new Function.FuncPoint(0.016, 13.225),
//                new Function.FuncPoint(0.018, 16.841),
//                new Function.FuncPoint(0.020, 19.110),
//                new Function.FuncPoint(0.022, 20.482),
//                new Function.FuncPoint(0.026, 21.130),
//                new Function.FuncPoint(0.028, 22.128),
//                new Function.FuncPoint(0.032, 21.953),
//                new Function.FuncPoint(0.038, 22.975),
//                new Function.FuncPoint(0.074, 21.878),
//                new Function.FuncPoint(0.124, 21.454),
//                new Function.FuncPoint(0.376, 22.327),
//                new Function.FuncPoint(0.680, 22.352),
//                new Function.FuncPoint(0.994, 20.606),
//                new Function.FuncPoint(1.246, 18.661),
//                new Function.FuncPoint(1.282, 13.923),
//                new Function.FuncPoint(1.316, 13.923),
//                new Function.FuncPoint(1.360, 10.033),
//                new Function.FuncPoint(1.424, 6.542),
//                new Function.FuncPoint(1.504, 4.771),
//                new Function.FuncPoint(1.598, 4.347),
//                new Function.FuncPoint(1.656, 3.674),
//                new Function.FuncPoint(1.676, 1.145),
//                new Function.FuncPoint(1.678, 0.312),
//                new Function.FuncPoint(1.714, 1.145),
//                new Function.FuncPoint(1.734, 0.312),
//                new Function.FuncPoint(1.808, 0)
//            })
//    );

    public static final HobbyRocketEngine G25 = new HobbyRocketEngine("G25",
            new SingleVariableTableFunction(0.0, 5.3, new Function.FuncPoint[]{
                new Function.FuncPoint(0.0, 0.0),
                new Function.FuncPoint(0.0001, 1.124),
                new Function.FuncPoint(0.13, 9.143),
                new Function.FuncPoint(0.177, 8.761),
                new Function.FuncPoint(0.295, 8.761),
                new Function.FuncPoint(0.343, 9.205),
                new Function.FuncPoint(0.413, 9.078),
                new Function.FuncPoint(0.437, 8.698),
                new Function.FuncPoint(0.484, 8.953),
                new Function.FuncPoint(0.532, 8.508),
                new Function.FuncPoint(0.65, 8.443),
                new Function.FuncPoint(0.721, 8.761),
                new Function.FuncPoint(0.803, 8.698),
                new Function.FuncPoint(0.85, 8.381),
                new Function.FuncPoint(0.98, 8.888),
                new Function.FuncPoint(1.063, 8.191),
                new Function.FuncPoint(1.098, 8.571),
                new Function.FuncPoint(1.252, 8.508),
                new Function.FuncPoint(1.37, 8.381),
                new Function.FuncPoint(1.583, 8.318),
                new Function.FuncPoint(1.819, 7.936),
                new Function.FuncPoint(1.984, 7.556),
                new Function.FuncPoint(2.185, 7.046),
                new Function.FuncPoint(2.315, 6.477),
                new Function.FuncPoint(2.622, 5.460),
                new Function.FuncPoint(3.024, 4.253),
                new Function.FuncPoint(3.39, 3.111),
                new Function.FuncPoint(3.839, 1.714),
                new Function.FuncPoint(4.323, 1.016),
                new Function.FuncPoint(4.783, 0.571),
                new Function.FuncPoint(5.3, 0),}),
            new SingleVariableTableFunction(0.0, 5.3, new Function.FuncPoint[]{
                new Function.FuncPoint(0.0, 0.011455),
                new Function.FuncPoint(0.0001, 0.011455),
                new Function.FuncPoint(0.13, 0.011455),
                new Function.FuncPoint(0.177, 0.011387),
                new Function.FuncPoint(0.295, 0.011219),
                new Function.FuncPoint(0.343, 0.011149),
                new Function.FuncPoint(0.413, 0.011045),
                new Function.FuncPoint(0.437, 0.011011),
                new Function.FuncPoint(0.484, 0.010943),
                new Function.FuncPoint(0.532, 0.010875),
                new Function.FuncPoint(0.65, 0.010713),
                new Function.FuncPoint(0.721, 0.010614),
                new Function.FuncPoint(0.803, 0.010498),
                new Function.FuncPoint(0.85, 0.010433),
                new Function.FuncPoint(0.98, 0.010088),
                new Function.FuncPoint(1.063, 0.010136),
                new Function.FuncPoint(1.098, 0.010088),
                new Function.FuncPoint(1.252, 0.009875),
                new Function.FuncPoint(1.37, 0.009713),
                new Function.FuncPoint(1.583, 0.009424),
                new Function.FuncPoint(1.819, 0.009113),
                new Function.FuncPoint(1.984, 0.008906),
                new Function.FuncPoint(2.185, 0.008668),
                new Function.FuncPoint(2.315, 0.008525),
                new Function.FuncPoint(2.622, 0.008228),
                new Function.FuncPoint(3.024, 0.007911),
                new Function.FuncPoint(3.39, 0.007693),
                new Function.FuncPoint(3.839, 0.007517),
                new Function.FuncPoint(4.323, 0.007410),
                new Function.FuncPoint(4.783, 0.007350),
                new Function.FuncPoint(5.3, 0.007326),})
    );

    public static final HobbyRocketEngine G25_POST_BURN = new HobbyRocketEngine("G25 - Post Burn",
            new SingleVariableFunction() {
                @Override
                public Double evaluate(Double time) {
                    return 0.0;
                }
            },
            new SingleVariableFunction() {
                @Override
                public Double evaluate(Double time) {
                    return 0.007326;
                }
            });


    // Fields
    private final String name;
    private final double burnTime;
    private final SingleVariableFunction thrust;
    private final SingleVariableFunction mass;


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
    public HobbyRocketEngine(String name, SingleVariableFunction thrust, SingleVariableFunction mass) {
        this.name = name;
        this.thrust = thrust;
        this.mass = mass;

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

    @Override
    public double getMass(double time) {
        if (mass.hasFiniteDomain) {
            if (time < mass.domainMin) {
                return mass.evaluate(mass.domainMin);
            }
            else if (time > mass.domainMax) {
                return mass.evaluate(mass.domainMax);
            }
        }
        return mass.evaluate(time);
    }

    public HobbyRocketEngine getThrustVariationEngine(double variation) {
        String newName = this.getName() + " THRUST_VAR_" + variation;
        SingleVariableFunction newThrust = new SingleVariableFunction(
                this.thrust.domainMin, this.thrust.domainMax) {
                    @Override
                    public Double evaluate(Double input) {
                        return HobbyRocketEngine.this.thrust.evaluate(input) * variation;
                    }
                };

        return new HobbyRocketEngine(newName, newThrust, mass);
    }

}
