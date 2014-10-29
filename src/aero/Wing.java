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
package aero;

import function.SingleVariableFunction;
import geometry.angle.Angle;

/**
 *
 * @author NathanT
 */
public abstract class Wing {

    // Protected Static Methods
    protected static double getDataMeasureFor(Angle angle) {
        double measure = angle.getMeasure(Angle.AngleType.DEGREES);
        if (measure > 180) {
            measure = measure * -1.0;
        }
        return measure;
    }

    public abstract WingPlanform getPlanform();

    public abstract double spanEfficiency();

    public abstract WingSection sectionAt(double station);

    public abstract double cl(Angle alpha);

    public abstract double cd(Angle alpha);

    public abstract double cpm(Angle alpha);

    public abstract SingleVariableFunction clFunction();

    public abstract SingleVariableFunction cdFunction();
    
    public abstract SingleVariableFunction cpmFunction();

}
