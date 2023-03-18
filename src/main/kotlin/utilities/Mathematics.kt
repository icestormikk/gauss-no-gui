package utilities

import domain.aliases.Matrix
import kotlin.math.abs

/**
 * A class that stores custom mathematical operations that are not contained
 * in the standard Kotlin or Java library
 */
object Mathematics {
    /**
     * Solves a matrix of size N*M using the Gauss method (performs only the
     * reduction of the matrix to the upper diagonal form)
     * @param matrix the matrix to be solved
     * @param selectedColumns a set of columns relative to which the matrix
     * will be reduced to a diagonal type
     * @return the original matrix reduced to the upper diagonal form
     */
    fun gauss(matrix: Matrix<Double>, selectedColumns: Set<Int> = setOf()) : Matrix<Double> {
        for (column in selectedColumns) {
            val i = selectedColumns.indexOf(column)
            if (matrix[i][column] == 0.0) {
                fetchMainElement(matrix, i)
            }

            if (matrix[i][column] == 0.0) { continue }

            for (j in (matrix.size - 1)downTo 0) {
                if (j == i) { continue }
                val coefficient = matrix[j][column] / matrix[i][column]

                for (k in 0 until matrix[j].size) {
                    matrix[j][k] -= coefficient * matrix[i][k]
                }
            }
        }

        for (column in selectedColumns) {
            val i = selectedColumns.indexOf(column)

            val coefficient = matrix[i][column]
            if (coefficient == 0.0) { continue }

            for (j in 0 until matrix[0].size) {
                matrix[i][j] /= coefficient
            }
        }

        return matrix
    }

    private fun fetchMainElement(
        matrix: Matrix<Double>,
        selectedRowIndex: Int
    ) {
        var rowMaxIndex = selectedRowIndex
        var maxElement = Double.MIN_VALUE

        for (i in rowMaxIndex until matrix.size) {
            val currentElement = matrix[i][selectedRowIndex]
            if (currentElement != 0.0 && abs(currentElement) > maxElement) {
                rowMaxIndex = i
                maxElement = abs(currentElement)
            }
        }

        if (rowMaxIndex != selectedRowIndex) {
            for (i in 0 until matrix[0].size) {
                val temp = matrix[rowMaxIndex][i]
                matrix[rowMaxIndex][i] = matrix[selectedRowIndex][i]
                matrix[selectedRowIndex][i] = temp
            }
        }
    }
}
