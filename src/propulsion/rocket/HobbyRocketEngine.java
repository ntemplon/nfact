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

import com.jupiter.ganymede.math.function.Function;
import com.jupiter.ganymede.math.function.Function.FuncPoint;
import com.jupiter.ganymede.math.function.SingleVariableRealFunction;
import com.jupiter.ganymede.math.function.SingleVariableTableFunction;

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
                new Function.FuncPoint(-1, 0.0),
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
                new Function.FuncPoint(5.3, 0),
                new Function.FuncPoint(5.4, 0)
            }),
            new SingleVariableTableFunction(0.0, 5.3, new FuncPoint[]{
                new Function.FuncPoint(-1.0, 0.011455),
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
                new Function.FuncPoint(5.3, 0.007326),
                new Function.FuncPoint(5.4, 0.007326)
            }),
            5.3
    );

    public static final HobbyRocketEngine G25_POST_BURN = new HobbyRocketEngine("G25 - Post Burn",
            (Double time) ->
            0.0,
            (Double time) ->
            0.007326,
            0.0
    );

    public static final HobbyRocketEngine F40 = new HobbyRocketEngine("F40",
            new SingleVariableTableFunction(0, 2.06, new FuncPoint[]{
                new FuncPoint(-1, 0.0),
                new FuncPoint(0, 0.0),
                new FuncPoint(0.015, 3.996),
                new FuncPoint(0.049, 9.221),
                new FuncPoint(0.089, 13.217),
                new FuncPoint(0.124, 14.140),
                new FuncPoint(0.148, 14.651),
                new FuncPoint(0.183, 14.038),
                new FuncPoint(0.242, 15.303),
                new FuncPoint(0.292, 13.627),
                new FuncPoint(0.321, 13.831),
                new FuncPoint(0.415, 13.627),
                new FuncPoint(0.524, 13.114),
                new FuncPoint(0.741, 11.783),
                new FuncPoint(0.87, 10.861),
                new FuncPoint(0.889, 11.065),
                new FuncPoint(0.914, 10.655),
                new FuncPoint(1.102, 9.017),
                new FuncPoint(1.285, 7.582),
                new FuncPoint(1.492, 5.635),
                new FuncPoint(1.665, 3.586),
                new FuncPoint(1.808, 1.947),
                new FuncPoint(1.942, 0.717),
                new FuncPoint(2.06, 0.0),
                new FuncPoint(2.1, 0.0)
            }),
            new SingleVariableTableFunction(0, 2.06, new FuncPoint[]{
                new FuncPoint(0, 0.00857),
                new FuncPoint(0.015, 0.00857),
                new FuncPoint(0.049, 0.00856),
                new FuncPoint(0.089, 0.00853),
                new FuncPoint(0.124, 0.00846),
                new FuncPoint(0.148, 0.00838),
                new FuncPoint(0.183, 0.00833),
                new FuncPoint(0.242, 0.00825),
                new FuncPoint(0.292, 0.00811),
                new FuncPoint(0.321, 0.00800),
                new FuncPoint(0.415, 0.00794),
                new FuncPoint(0.524, 0.00774),
                new FuncPoint(0.741, 0.00751),
                new FuncPoint(0.87, 0.00709),
                new FuncPoint(0.889, 0.00686),
                new FuncPoint(0.914, 0.00683),
                new FuncPoint(1.102, 0.00678),
                new FuncPoint(1.285, 0.00649),
                new FuncPoint(1.492, 0.00626),
                new FuncPoint(1.665, 0.00604),
                new FuncPoint(1.808, 0.00592),
                new FuncPoint(1.942, 0.00586),
                new FuncPoint(2.06, 0.00582),
                new FuncPoint(2.1, 0.00582)
            }),
            2.06
    );

    public static final HobbyRocketEngine M750 = new HobbyRocketEngine("M750",
            new SingleVariableTableFunction(0, 16.0, new FuncPoint[]{
                new FuncPoint(-1.0, 232.00),
                new FuncPoint(0.0, 232.00),
                new FuncPoint(0.1, 223.01),
                new FuncPoint(0.3, 218.96),
                new FuncPoint(0.48, 217.17),
                new FuncPoint(1.0, 237.17),
                new FuncPoint(1.5, 258.98),
                new FuncPoint(2.0, 267.97),
                new FuncPoint(2.5, 273.82),
                new FuncPoint(4.0, 247.96),
                new FuncPoint(6.0, 183.89),
                new FuncPoint(8.0, 126.12),
                new FuncPoint(10.0, 71.49),
                new FuncPoint(11.0, 48.56),
                new FuncPoint(12.0, 28.10),
                new FuncPoint(13.0, 17.09),
                new FuncPoint(14.0, 10.57),
                new FuncPoint(15.0, 5.17),
                new FuncPoint(15.5, 2.02),
                new FuncPoint(16.0, 0.0),
                new FuncPoint(17.0, 0.0)
            }),
            new SingleVariableTableFunction(0, 16.0, new FuncPoint[]{
                new FuncPoint(-1.0, 0.60009),
                new FuncPoint(0.0, 0.60009),
                new FuncPoint(0.1, 0.59807),
                new FuncPoint(0.2, 0.59410),
                new FuncPoint(0.3, 0.59026),
                new FuncPoint(0.48, 0.58342),
                new FuncPoint(1.0, 0.56385),
                new FuncPoint(1.5, 0.54124),
                new FuncPoint(2.0, 0.51830),
                new FuncPoint(2.5, 0.49471),
                new FuncPoint(4.0, 0.42655),
                new FuncPoint(6.0, 0.35134),
                new FuncPoint(8.0, 0.29734),
                new FuncPoint(10.0, 0.26293),
                new FuncPoint(11.0, 0.25247),
                new FuncPoint(12.0, 0.24580),
                new FuncPoint(13.0, 0.241860),
                new FuncPoint(14.0, 0.23945),
                new FuncPoint(15.0, 0.23808),
                new FuncPoint(15.5, 0.23777),
                new FuncPoint(16.0, 0.23768),
                new FuncPoint(17.0, 0.23768)
            }),
            16.0
    );


    // Fields
    private final String name;
    private final double burnTime;
    private final SingleVariableRealFunction thrust;
    private final SingleVariableRealFunction mass;


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
    public HobbyRocketEngine(String name, SingleVariableRealFunction thrust, SingleVariableRealFunction mass, double burnTime) {
        this.name = name;
        this.thrust = thrust;
        this.mass = mass;

        this.burnTime = burnTime;
    }


    // Public Methods
    /**
     *
     * @param time the time since ignition, in seconds
     *
     * @return the thrust of the engine, in pounds
     */
    @Override
    public double getThrust(double time) {
        return thrust.evaluate(time);
    }

    @Override
    public double getMass(double time) {
        return mass.evaluate(time);
    }

    public HobbyRocketEngine getThrustVariationEngine(double variation) {
        String newName = this.getName() + " THRUST_VAR_" + variation;
        SingleVariableRealFunction newThrust = (Double input) ->
                HobbyRocketEngine.this.thrust.evaluate(input) * variation;

        return new HobbyRocketEngine(newName, newThrust, mass, this.burnTime);
    }

}
