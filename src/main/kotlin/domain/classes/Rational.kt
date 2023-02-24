package domain.classes

import kotlin.math.abs
import kotlin.math.roundToLong

/**
 * Abstract representation of a rational fraction
 * @param numerator numerator of fraction
 * @param denominator  denominator of fraction
 */
data class Rational(
    var numerator: Double,
    var denominator: Double
) {
    /**
     * Conversion of rational fraction to real
     * @return
     */
    fun toDouble() : Double =
        numerator / denominator

    override fun toString(): String =
        if (denominator == 1.0) "$numerator" else "$numerator/$denominator"
}

/**
 * Conversion of real fraction to rational
 * @param maxDenominator fraction calculation accuracy
 * @return rational fraction equal to the passed real fraction
 */
fun Double.toRational(
    maxDenominator: Double = 10000.0
) : Rational {
    val bestRational = Rational(1.0, 1.0)
    var error = abs(this - 1)
    var denom = 1.0

    while (error > 0 && denom <= maxDenominator) {
        val numerator = (this * denom).roundToLong()
        val currentError = abs(this - numerator / denom)

        if (currentError >= error) {
            denom++
            continue
        }

        bestRational.numerator = numerator.toDouble()
        bestRational.denominator = denom
        error = currentError

        denom++
    }

    return bestRational
}