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

package exception;

/**
 * An exception class representing an exception generated in the mathematics of the project.
 * @author Hortator
 */
public class WuCalcException extends RuntimeException{
    
    /**
     * A default constructor.
     */
    public WuCalcException() {
        super();
    }
    
    /**
     * A constructor that allows the user to specify an error message.
     * @param s The error message for the exception.
     */
    public WuCalcException(String s) {
        super(s);
    }
}
