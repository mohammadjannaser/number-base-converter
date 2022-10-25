import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.pow

fun main() {

    val calculator = Calculator()
    calculator.start()
}


class Calculator {

    fun start() {

        while (true) {

            println("Enter two numbers in format: {source base} {target base} (To quit type /exit) >")

            val sourceTargetBase = readln()
            if (sourceTargetBase == "/exit") break

            val sourceBase = sourceTargetBase.split(" ").first().toInt()
            val targetBase = sourceTargetBase.split(" ").last().toInt()

            while (true) {
                println("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back)")
                val number = readln().uppercase()
                if (number == "/back".uppercase()) break


                if (number.split(".").size > 1) {
                    // number has fractional part
                    // first convert to decimal base and then change the decimal to target base
                    val toDecimal = convertToDecimal(number.substringBefore("."), sourceBase)
                    val fromDecimal = convertFromDecimal(toDecimal, targetBase)

                    val toDecimalFractionalPart = fractionalToDecimal(number.substringAfter("."), base = sourceBase)
                    val fromDecimalFractionPart = fractionFromDecimal(toDecimalFractionalPart, base = targetBase)
                    println("Conversion result: ${fromDecimal.lowercase()}.${fromDecimalFractionPart.lowercase()}")
                }
                else {
                    // number does not have fractional part
                    val toDecimal = convertToDecimal(number, sourceBase)
                    val fromDecimal = convertFromDecimal(toDecimal, targetBase)
                    println("Conversion result: ${fromDecimal.lowercase()}")
                }


            }
        }
    }



    private fun convertToDecimal(decimal: String, base: Int): BigInteger {

        var result = BigInteger("0")
        var power = decimal.length - 1

        for (digit in decimal) {
            val tempDigit = hexToDecimal(digit)
            result += ((tempDigit * base.toDouble().pow(power.toDouble())).toBigDecimal().toBigInteger())
            power--
        }

        return result
    }

    private fun fractionalToDecimal(decimal: String, base: Int): String {

        var result = BigDecimal("0")
        var power = -1


        for (digit in decimal) {
            val tempDigit = hexToDecimal(digit)
            val tempPower = tempDigit * base.toDouble().pow(power)
            result += tempPower.toBigDecimal()
            power--
        }

        return result.toString().substringAfter(".")
    }



    private fun convertFromDecimal(decimal: BigInteger, base: Int): String {
        val powerValues = calculatePowerValues(decimal, base)
        return powerValues.map { convertToDigit(it) }.joinToString("")
    }

    private fun calculatePowerValues(decimal: BigInteger, base: Int): List<Int> {
        val powerValues = mutableListOf<Int>()
        var value = decimal
        val tempBase = base.toBigInteger()
        while (value / tempBase > BigInteger("0")) {
            powerValues.add(value.mod(tempBase).toInt())
            value /= base.toBigInteger()
        }
        powerValues.add((value % tempBase).toInt())
        return powerValues.reversed().toList()
    }

    private fun fractionFromDecimal(decimal: String, base: Int): String {
        val powerValues = calculateFractionPowerValues(decimal, base)
        return powerValues.map { convertToDigit(it) }.joinToString("")
    }

    private fun calculateFractionPowerValues(decimal: String,base: Int) : List<Int> {

        val powers = MutableList(5) {0}
        var tempDecimal = BigDecimal("0.$decimal")

        var count = 0
        while (tempDecimal > BigDecimal("0")) {
            tempDecimal *= BigDecimal(base.toString())
            powers[count] = tempDecimal.toInt()
            tempDecimal = BigDecimal( "0." + tempDecimal.toString().substringAfter("."))
            count++
            if (count >=5) break
        }

        return powers.toList()

    }



    private fun convertToDigit(num: Int): Char {
        if (num < 10) return '0' + num
        return 'A' + num - 10
    }

    fun hexToDecimal(digit: Char): Int {

        if (digit < 'A') return digit.toString().toInt()
        return digit - 'A' + 10


    }


    // stage two
    private fun toDecimal() {
        println("Enter source number:")
        val number = readln().uppercase()
        println("Enter source base: ")
        val sourceBase = readln().toInt()
        println("Conversion to decimal result: " + convertToDecimal(number, sourceBase))

    }

    private fun fromDecimal() {
        println("Enter number in decimal system:")
        val number = readln().toInt()
        println("Enter target base:")
        val targetBase = readln().toInt()
        println("Conversion result: " + convertFromDecimal(number.toBigInteger(), targetBase))
    }


}