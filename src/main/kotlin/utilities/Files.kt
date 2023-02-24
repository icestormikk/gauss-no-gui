package utilities

import domain.ROUNDING_ACCURACY
import domain.aliases.Matrix
import java.io.File
import java.math.RoundingMode

/**
 * A class that stores functions for manipulating files
 */
object Files {
    /**
     * Reading a two-dimensional array from a file that the user specifies the path to.
     * @param filepath the path to the file with a two-dimensional array
     * @return an object containing a matrix with values read from a file
     */
    fun readAsMatrix(filepath: String): Matrix<Double> {
        val inputStream = File(filepath).inputStream()
        val result: Matrix<Double> = mutableListOf()

        inputStream.bufferedReader().forEachLine { line ->
            val elements = line
                .split(Regex("\\s"))
                .map { it.toDouble() }

            isInputSuitable(result, elements)
            result.add(elements.toMutableList())
        }

        return result
    }

    /**
     * Saving a two-dimensional array with real values to a file
     * @param destination the path where the matrix file will be created/updated
     * @param matrix the matrix whose values need to be saved
     */
    fun writeMatrixToFile(destination: String, matrix: Matrix<Double>) {
        File(destination).printWriter().use { writer ->
            matrix.forEach { array ->
                array.forEach {
                    val number = it.toBigDecimal().setScale(ROUNDING_ACCURACY, RoundingMode.HALF_EVEN)
                    writer.print("$number ")
                }
                writer.println()
            }
        }
    }

    private fun isInputSuitable(
        matrix: Matrix<Double>,
        elements: List<Double>
    ) : Boolean {
        var result = true

        if (matrix.size > 0 && matrix[0].size != elements.size) {
            error("Обнаружен различный размер строк")
        }

        return result
    }
}