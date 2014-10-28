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
 */
public abstract class SingleVariableFunction implements Function<Double, Double> {
    
    // Private Members
    public final boolean hasFiniteDomain;
    public final double domainMin;
    public final double domainMax;
    
    
    // Initialization
    public SingleVariableFunction() {
        this.hasFiniteDomain = false;
        this.domainMin = 0.0;
        this.domainMax = 0.0;
    }
    
    public SingleVariableFunction(double minDomainValue, double maxDomainValue) {
        this.hasFiniteDomain = true;
        this.domainMin = minDomainValue;
        this.domainMax = maxDomainValue;
    }
    
}
