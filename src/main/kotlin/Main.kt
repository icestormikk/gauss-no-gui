import domain.ROUNDING_ACCURACY
import domain.aliases.Matrix
import domain.classes.toRational
import utilities.Files
import utilities.Mathematics
import java.math.RoundingMode
import kotlin.io.path.Path


fun main(args: Array<String>) {
    if (args.size < 3) {
        print(
            """
            Insufficient number of arguments passed.
            USAGE: [input_file_path] [output_file_path] [...indexes of columns]
            Example: input.txt output.txt 0 2 4
            
            """.trimIndent()
        )
        return
    }

    // Getting arguments for the program passed by the user
    val input = args[0]
    val output = args[1]
    var columns = args
        .sliceArray(2 until args.size)
        .map { it.toInt() }
        .toMutableSet()
    val matrix: Matrix<Double>

    /* Forming a matrix and checking the condition for the number of rows (M) and columns (N)
    (N >= M) */
    try {
        matrix = Files.readAsMatrix(Path(input))
    } catch (e: Exception) {
        System.err.println("Error while reading: ${e.message}")
        return
    }
    if (matrix[0].size < matrix.size) {
        System.err.println(
            """
            The number of rows in the matrix cannot be greater than the number of columns.
            Dimensions of the read matrix: ${matrix.size} rows * ${matrix[0].size} columns
            """.trimIndent())
        return
    }

    /* Checking the index list of columns and selecting suitable ones
    (they do not exceed acceptable limits and are unique). */
    columns = columns.take(matrix.size).filter {
        if (it !in 0 until (matrix[0].size - 1)) {
            println("The column with index $it does not exist in the matrix")
            false
        } else true
    }.toMutableSet()
    if (columns.size == 0) {
        System.err.println("No matching columns found, canceling the application of the Gauss method")
        return
    }
    if (columns.size != matrix.size) {
        System.err.println(
            """
                The number of matching columns (${columns.size}) and the parameter m (${matrix.size}) do not match.
                Suitable columns: $columns
                Dimensions of the read matrix: ${matrix.size} * ${matrix[0].size}
            """
            .trimIndent()
        )
        return
    }

    // Application of the improved Gauss method to the matrix
    Mathematics.gauss(matrix, columns.toSet())

    try {
        Files.writeMatrixToFile(Path(output), matrix)
        println("Calculations completed successfully. The result is written to a file ${Path(output).toUri()}")
    } catch (e: Exception) {
        println("Error while saving: ${e.message}")
    }

    println("A more readable version of the result:")
    matrix.forEach { array ->
        array.forEachIndexed { index, value ->
            val number = value.toBigDecimal().setScale(ROUNDING_ACCURACY, RoundingMode.HALF_EVEN)
            val rational = number.toDouble().toRational()
            print("${if (index == array.size - 1) "| " else " "}${String.format("%-12s ", rational)}")
        }
        println()
    }
}
