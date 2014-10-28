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

package matrix;

import exception.InvalidOperationException;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 * A class to represent Matrices and perform Matrix operations
 * @author Nathan Templon
 */
public class Matrix {
    
    // -------------------------------------------------------------------------------------------------
    // ---------------------------  Constructors  ------------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A constructor that creates a Matrix of the specified dimensions filled with all 0's
     * @param numRows the number of rows the new matrix will have
     * @param numCols the number of columns the new matrix will have
     */
    public Matrix(int numRows, int numCols) {
        // Initialize state variables
        values = new double[numRows][numCols];
        this.numRows = numRows;
        this.numCols = numCols;
        
        // Fill the array with zeros to avoid null pointers
        for (double[] row : values) {
            Arrays.fill(row, 0);
        }
    }
    
    
    /**
     * A constructor that creates a Matrix from the provided two-dimensional array.
     * @param values a two-dimensional double array storing the values to make up the Matrix object.
     * @throws IllegalArgumentException If values is a jagged array
     */
    public Matrix(double[][] values) {
        // First, verify that values is a legal array for a matrix
        //   i.e., it is not jagged
        if (isJagged(values)) {
            throw new IllegalArgumentException("Cannot initialize a matrix from a jagged array.");
        }
        
        // Initialize state variables
        this.values = values;
        this.numRows = values.length;
        this.numCols = values[0].length;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  Matrix Operations  ----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method that compares this Matrix to another for equality.
     * @param other another Matrix to be checked with this one for equality.
     * @return Returns a boolean indicating if the two matrices are equal.
     */
    @Override
    public boolean equals(Object other) {
        
        if (other instanceof Matrix) {
        Matrix matrix = (Matrix)other;
        
            // If the dimensions are not equal, neither are the matrices.
            if (!matrix.hasSameDimAs(this)) {
                return false;
            }
        
            // If any of the elements in the matrices aren't equal, neither are the matrices.
            for(int row = 1; row <= numRows; row++) {
                for (int col = 1; col <= numCols; col ++) {
                    if (matrix.getElement(row, col) != this.getElement(row, col)) {
                        return false;
                    }
                }
            }
            
            // If it passed those two tests, the matrices are equal.
            return true;
        }
        
        else {
            return false;
        }
        
    }

    
    /**
     * A method that generates a number to represent the Matrix.
     * @return Returns the Hash Code of the Matrix.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.deepHashCode(this.values);
        hash = 89 * hash + this.numRows;
        hash = 89 * hash + this.numCols;
        return hash;
    }
    
    
    /**
     * A method which multiplies the Matrix by a specified scalar.
     * @param scalar the scalar by which to be multiplied
     * @return Returns the matrix multiplied by a scalar double.
     */
    public Matrix scalarMultiply(double scalar) {
        double[][] newValues = new double[numRows][numCols];
        
        // Multiply each element by the scalar
        for(int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                newValues[row][col] = scalar * values[row][col];
            }
        }
        
        return new Matrix(newValues);
    }
    
    
    /**
     * A method which adds another Matrix to this one.
     * @param other the matrix that will be added to this one
     * @return the Matrix object that is the sum of this matrix and the provided matrix
     * @throws InvalidOperationException if the two matrices do not have the same dimensions
     */
    public Matrix add(Matrix other) {
        if (!this.hasSameDimAs(other)) {
            throw new InvalidOperationException("Matrix addition only works for matrices of the same size.");
        }
        
        double[][] newValues = new double[numRows][numCols];
        
        for (int row = 1; row <= numRows; row++) {
            for (int col = 1; col <= numCols; col++) {
                // -1's account for difference between internal array and matrix indexing
                newValues[row - 1][col - 1] = this.getElement(row, col) + other.getElement(row, col);
            }
        }
        
        return new Matrix(newValues);
    }
    
    
    /**
     * A method which multiplies this Matrix with another.
     * @param other the matrix to multiply with this one
     * @return Returns the result of multiplying this matrix with the provided one.  This matrix is treated as being on the left hand side of the multiplication.
     */
    public Matrix multiply(Matrix other) {
        if (!(this.getNumColumns() == other.getNumRows())) {
            throw new InvalidOperationException("Invalid dimensions for matrix multiplication.");
        }
        
        double[][] newValues = new double[this.getNumRows()][other.getNumColumns()];
        
        for (int row = 1; row <= this.getNumRows(); row++) {
            for (int col = 1; col <= other.getNumColumns(); col++) {
                double sum = 0;
                for (int index = 1; index <= this.getNumColumns(); index++) {
                    sum += this.getElement(row, index) * other.getElement(index, col);
                }
                
                newValues[row - 1][col - 1] = sum;
            }
        }
        
        return new Matrix(newValues);
    }
    
    
    /**
     * A method which multiplies this Matrix with another, with this Matrix being the right-hand one.
     * @param other the matrix to multiply with this one
     * @return Returns the result of multiplying this matrix with the provided one.  This matrix is treated as being on the right hand side of the multiplication.
     */
    public Matrix rightSideMultiply(Matrix other) {
        return other.multiply(this);
    }
    
    
    /**
     * A method that returns the minor of the Matrix about a specified element.
     * @param row the row of the element about which to take the minor
     * @param column the column of the element about which to take the minor
     * @return Returns a new Matrix object that is the minor of this matrix about the specified element.
     */
    public Matrix minor(int row, int column) {
        // The minor will be smaller by one row and column
        double[][] newValues = new double[numRows - 1][numCols - 1];
        
        int microRow = 0;
        int microColumn = 0;
        
        for (int macroRow = 1; macroRow <= numRows; macroRow++) {
            if (macroRow != row) {
                microColumn = 0;
                for (int macroColumn = 1; macroColumn <= numCols; macroColumn++) {
                    if (macroColumn != column) {
                        newValues[microRow][microColumn] = this.getElement(macroRow, macroColumn);
                        microColumn++;
                    }
                }
                microRow++;
            }
        }
        
        Matrix output = new Matrix(newValues);
        return output;
    }
    
    
    /**
     * A method that calculates the determinant of the Matrix.
     * @return Returns the determinant of the matrix.
     * @throws InvalidOperationException if the matrix is not square
     */
    public double det() {
        if (!isSquare()) {
            throw new InvalidOperationException("Cannot take determinant of a non-square matrix.");
        }
        if (numRows == 1) {return values[0][0];}
        else {
            double det = 0;
            for (int index = 1; index <= numRows; index++) {
                det += ( this.getElement(1, index) * cofactor(1, index) );
            }
            return det;
        }
    }
    
    
    /**
     * A method that calculates the transpose of the Matrix.
     * @return Returns the transpose of the matrix.
     */
    public Matrix transpose() {
        double[][] newValues = new double[numCols][numRows];
        
        // reverse the position of rows and columns.
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                newValues[col][row] = values[row][col];
            }
        }
        
        return new Matrix(newValues);
    }
    
    
    /**
     * A method that calculates the cofactor of a specified element.
     * @param row the row of the element to get the cofactor of
     * @param col the column of the element to get the cofactor of
     * @return Returns the cofactor of the specified element in the matrix
     * @throws InvalidOperationException if the matrix is not square
     */
    public double cofactor(int row, int col) {
        if (!isSquare()) {
            throw new InvalidOperationException("Cannot find cofactors for a non-square matrix.");
        }
        
        return this.minor(row, col).det() * Math.pow( -1, row + col );
    }
    
    
    /**
     * A method that calculates the cofactor matrix of this matrix.
     * @return Returns the cofactor matrix for this matrix.
     * @throws InvalidOperationException if the matrix is not square.
     */
    public Matrix cofactorMatrix() {
        if (!this.isSquare()) {
            throw new InvalidOperationException("Cannot find cofactor matrix for a non-square matrix.");
        }
        double newValues[][] = new double[numRows][numCols];
        
        // Calculate the cofactors for each position in the matrix
        for (int row = 1; row <= numRows; row++) {
            for (int col = 1; col <= numCols; col++) {
                // -1's account for difference between internal array and matrix indexing
                newValues[row - 1][col - 1] = cofactor(row, col);
            }
        }
        
        return new Matrix(newValues);
    }
    
    
    /**
     * A method that calculates the adjoint matrix of this Matrix.
     * @return Returns the adjoint matrix for this matrix.
     * @throws InvalidOperationException if the matrix is not square.
     */
    public Matrix adjoint() {
        if (!this.isSquare()) {
            throw new InvalidOperationException("Cannot find adjoint matrix for a non-square matrix.");
        }
        return this.cofactorMatrix().transpose();
    }
    
    
    /**
     * A method that calculates the inverse matrix of this Matrix.
     * @return Returns the inverse of the matrix.
     * @throws InvalidOperationException if the matrix is not square
     */
    public Matrix inverse() {
        if (this.isSingular()) {
            throw new InvalidOperationException("Inverses do not exist for singular matrices.");
        }
        return this.adjoint().scalarMultiply( 1.0/this.det() );
    }
    
    
    /**
     * A method that exchanges the values in two rows of the Matrix.
     * @param first the first of the two rows to be swapped.
     * @param second the second of the two rows to be swapped.
     */
    public void swapRows(int first, int second) {
        double[] temp = new double[numCols];
        temp = values[first - 1];
        values[first - 1] = values[second - 1];
        values[second - 1] = temp;
    }
    
    
    /**
     * A method that multiplies one row of the Matrix by a scalar.
     * @param row the row to multiply by the scalar.
     * @param scalar the scalar to multiply the row by.
     */
    public void rowMultScalar(int row, double scalar) {
        
        for (int col = 0; col < numCols; col++) {
            values[row - 1][col] = scalar * values[row-1][col];
        }
        
    }
    
    
    /**
     * A method that adds a scalar multiple of one row of the Matrix to another.
     * @param rowToAdd the row to multiply by the scalar, then add to another row.
     * @param scalar the scalar to multiply the first row by.
     * @param rowToAddTo the row to add the multiple of the first row to./=
     */
    public void addRowMultiple(int rowToAdd, double scalar, int rowToAddTo) {
        for (int index = 0; index < numCols; index++) {
            values[rowToAddTo - 1][index] += values[rowToAdd - 1][index] * scalar;
        }
    }
    
    
    /**
     * A method that calculates the row-echelon form of the Matrix using a Gaussian elimination with partial pivoting algorithm.
     * @return Returns the matrix in row-echelon form.
     */
    public Matrix ref() {
        
        if (numCols != numRows + 1) throw new InvalidOperationException("The Matrix is not of the correct size to have a "
                + " row echelon form.");
        
        Matrix ref = new Matrix(values);
       
        // Iterate through each row in the matrix
        for (int row = 1; row <= numRows; row++) {
            int rowWithMax = ref.rowOfMaxMagnitudeWithinRange(row, row, numRows);
            ref.swapRows(row, rowWithMax);
            
            // If the maximum magnitude left in this column is a zero, then the row-echelon form does not exist.
            if (Math.abs( getElement(row, row ) ) < 10E-12 ) throw new InvalidOperationException("The matrix is not of sufficient to have a"
                + " row echelon form.");
            
            // Iterate through the elements in the row below the diagonal, zeroing them out as possible.
            for (int col = 1; col < row; col++) {
                addRowMultiple(col, -1 * getElement(row, col) / getElement(col, col), row);
            }
        }
        
        // change the diagonal terms to 1's
        for (int row = 1; row <= numRows; row++) {
            rowMultScalar(row, 1 / getElement (row, row) );
            if (getElement(row, row) <  0) rowMultScalar(row, -1);
        }
        
        return ref;
    }
    
    
    /**
     * A method that calculates the reduced row-echelon form of the Matrix.
     * @return Returns the matrix in reduced row echelon form.
     */
    public Matrix rref() {
        Matrix rref = ref();
        
        // reduce the off-diagonal terms to zero
        for (int col = numCols - 1; col >= 1; col--) {
            for (int row = col - 1; row >= 1; row--) {
                addRowMultiple(col, -1 * getElement( row, col) / getElement(col, col), row);
            }
        }
        
        return rref;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  External Methods  -----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method that returns the contents of the Matrix as a String
     * @return Returns a String representing the Matrix
     */
    @Override
    public String toString() {
        String output = "[";
        for (int index1 = 0; index1 < numRows; index1++) {
            output += "[";
            double [] row = values[index1];
            for (int index2 = 0; index2 < numCols; index2++) {
                double value = row[index2];
                output += value;
                if (!(index2 == numCols - 1)) {output += ", ";}
            }
            output += "]";
            if (!(index1 == numRows - 1)) {output +="\n";}
        }
        output += "]";
        return output;
    }
    
    
    /**
     * A method for checking if two Matrices are the same size.
     * @param other the Matrix object whose size is to be compared with
     * @return Returns if the two matrices are the same size.
     */
    public boolean hasSameDimAs(Matrix other) {
        return ( (this.getNumRows() == other.getNumRows()) && (this.getNumColumns() == other.getNumColumns()) );
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -----------------------  Getters and Setters  ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    /**
     * A method for returning the number of rows in the Matrix.
     * @return Returns the number of rows in the matrix.
     */
    public int getNumRows() {
        return numRows;
    }
    
    
    /**
     * A method for returning the number of columns in the Matrix.
     * @return Returns the number of columns in the matrix.
     */
    public int getNumColumns() {
        return numCols;
    }
    
    
    /**
     * A method for obtaining the value of an element in the Matrix.
     * @param row the row of the desired element
     * @param col the column of the desired element
     * @return Returns the element at the requested position in the matrix.
     */
    public double getElement(int row, int col) {
        // +1's account for difference between internal array and matrix indexing
        return values[row - 1][col - 1];
    }
    
    
    /**
     * A method for setting the value of an element in the Matrix.
     * @param row the row in which to place the element
     * @param col the column in which to place the element
     * @param val the value to assign the element
     * @return Returns true if the assignment was successful, false if it was not.
     */
    public boolean setElement(int row, int col, double val) {
        if ( (row >= numRows) || (col >= numCols) ) {
            return false;
        }
        // -1's account for difference between internal array and matrix indexing
        values[row - 1][col - 1] = val;
        testedForSingular = false;
        return true;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  Property Returners  ---------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    /**
     * A method for checking if a Matrix is square.
     * @return Returns if the matrix is square.
     */
    public boolean isSquare() {
        return (numRows == numCols);
    }
    
    
    /**
     * A method for checking if a Matrix is singular.
     * @return Returns if the matrix is singular.
     * @throws InvalidOperationException if the matrix is not square
     */
    public boolean isSingular() {
        
        if (!isSquare()) {
            throw new InvalidOperationException("Singularity is only defined for square matrices.");
        }
        
        if (!testedForSingular) {
            isSingular = (det() == 0);
            testedForSingular = true;
        }
        return isSingular;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // ------------------------  Internal Methods  -----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    /**
     * A method that checks if a two-dimensional array is jagged.
     * @param array a two-dimensional double array to be checked for jaggedness
     * @return Returns if the array is jagged.
     */
    private boolean isJagged(double[][] array) {
        // set length equal to something impossible, so that we can tell if it has been initialized inside
        //   of the loop later
        int length = -1;
        for(double[] line : array) {
            // If we don't have an initial length yet, get one
            if (length == -1) {
                length = line.length;
            }
            
            // Else, if any row has more or less columns than any other, the array is jagged and invalid.
            else {
                if (length != line.length) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
     * A method that returns the row with the maximum value in a specified column.
     * @param col the column to be searched for the row of maximum value
     * @return Returns the row of the matrix with the maximum value in the column specified.
     */
    private int rowOfMax(int col) {
        int rowOfMax = 1;
        double maxValue = getElement(1, col);
        
        for (int row = 1; row <= numRows; row++) {
            if ( getElement(row, col) > maxValue ) {
                maxValue = getElement(row, col);
                rowOfMax = row;
            }
        }
        
        return rowOfMax;
    }
    
    
    /**
     * A method that returns the row with the maximum value in a specified column within a specified range of rows.
     * @param col the column to be searched for the row of maximum value
     * @param start the row at which to start searching (top of the range, inclusive)
     * @param end the row at which to stop searching (bottom of the range, inclusive)
     * @return Returns the row of the matrix with the maximum value in the column specified.
     */
    private int rowOfMaxWithinRange(int col, int start, int end) {
        int rowOfMax = start;
        double maxValue = getElement(start, col);
        
        for (int row = start; row <= end; row++) {
            if ( getElement(row, col) > maxValue ) {
                maxValue = getElement(row, col);
                rowOfMax = row;
            }
        }
        
        return rowOfMax;
    }
    
    
     /**
     * A method that returns the row with the maximum magnitude in a specified column within a specified range of rows.
     * @param col the column to be searched for the row of maximum value
     * @param start the row at which to start searching (top of the range, inclusive)
     * @param end the row at which to stop searching (bottom of the range, inclusive)
     * @return Returns the row of the matrix with the maximum absolute value in the column specified.
     */
    private int rowOfMaxMagnitudeWithinRange(int col, int start, int end) {
        int rowOfMax = start;
        double maxValue = Math.abs( getElement(start, col) );
        
        for (int row = start; row <= end; row++) {
            if ( Math.abs( getElement(row, col) ) > maxValue ) {
                maxValue = Math.abs( getElement(row, col) );
                rowOfMax = row;
            }
        }
        
        return rowOfMax;
    }
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  State Variables  -----------------------------------------------------
    // -------------------------------------------------------------------------------------------------
    // Primary two-dimensional array, to store the values in the matrix.
    private double[][] values;
    
    // variables storing the number of rows and columns in the matrix
    private int numRows, numCols;
    
    // variables storing whether or not we know if we are singular or not, and whether or not we are
    private boolean isSingular;
    private boolean testedForSingular = false;
    
    
    
    
    
    // -------------------------------------------------------------------------------------------------
    // -------------------------  Static Methods and Variables -----------------------------------------
    // -------------------------------------------------------------------------------------------------
    
    /**
     * A method that returns an identity matrix of the specified size.
     * @param size the size of the desired Identity Matrix
     * @return Returns an Identity Matrix object of the specified size.
     */
    public static Matrix Identity(int size) {
        double[][] newValues = new double[size][size];
        
        for (double[] row : newValues) {
            Arrays.fill(row, 0);
        }
        
        for (int index = 0; index < size; index++) {
            newValues[index][index] = 1.0;
        }
        
        return new Matrix(newValues);
    }
}
