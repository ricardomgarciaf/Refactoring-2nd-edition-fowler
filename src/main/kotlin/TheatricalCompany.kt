import org.joda.money.CurrencyUnit
import org.joda.money.Money
import kotlin.math.floor
import kotlin.math.max

data class Performance(val playID: String, val audience: Int)

//Step 17: Create structure
data class EnrichedPerformance(val play: Play, val audience: Int)

data class Invoice(val customer: String, val performances: List<Performance>)

data class Play(val name: String, val type: PlayType)

enum class PlayType { TRAGEDY, COMEDY }

data class StatementData(var customer: String = "", var performances: List<EnrichedPerformance> = emptyList())

//Step 16: Split phase
fun statement(invoice: Invoice, plays: Map<String, Play>): String {

    //Step 3: Replace temp with query (Remove temporary variables (if possible) replacing them by functions if they are read-only variables)
    fun playFor(aPerformance: Performance) = plays[aPerformance.playID]

    fun enrichPerformance(aPerformance: Performance): EnrichedPerformance {
        return EnrichedPerformance(playFor(aPerformance) ?: error("Play not found"), aPerformance.audience)
    }

    fun renderPlainText(data: StatementData, plays: Map<String, Play>): String {
        //Step 2: Decompose function
        //Step 4: Remove play parameter using playFor function
        //Step 5: thisAmount is never updated again which means that inline variable can be used
        fun amountFor(aPerformance: EnrichedPerformance): Int {
            var result = 0
            when (aPerformance.play.type) {
                PlayType.TRAGEDY -> {
                    result = 40000
                    if (aPerformance.audience > 30) {
                        result += 1000 * (aPerformance.audience - 30)
                    }
                }
                PlayType.COMEDY -> {
                    result = 30000
                    if (aPerformance.audience > 20) {
                        result += 10000 + 500 * (aPerformance.audience - 20)
                    }
                    result += 300 * aPerformance.audience
                }
            }
            return result
        }


        //Step 6: Move volumeCredits calculation to a function
        fun volumeCreditsFor(aPerformance: EnrichedPerformance): Int {
            var result = 0
            result += max(aPerformance.audience - 30, 0)
            if (aPerformance.play.type == PlayType.COMEDY) {
                result += floor(aPerformance.audience.toDouble() / 5).toInt()
            }
            return result
        }

        //Step 7: Change function variable to declared function and rename for better understanding
        fun usd(value: Double): String {
            return Money.of(CurrencyUnit.USD, value / 100).toString()
        }

        //Step 9: Move declaration of the variable next to the loop
        //Step 10: Replace temp with query
        fun totalVolumeCredits(): Int {
            var result = 0
            data.performances.forEach {
                result += volumeCreditsFor(it)
            }
            return result
        }

        //Step 12: Split loop
        //Step 13: Slide statement
        //Step 14: Extract function
        fun totalAmount(): Double {
            var result = 0
            data.performances.forEach { perf ->
                result += amountFor(perf)
            }
            return result.toDouble()
        }

        var result = "Statement for ${data.customer}\n"

        //Step 8: Split loop
        data.performances.forEach { perf ->
            //print line for this order
            result += "${perf.play.name}: ${usd(amountFor(perf).toDouble())} (${perf.audience} seats)\n"
        }

        //Step 15: Inline variable
        result += "Amount owed is ${usd(totalAmount())}\n"
        //Step 11: Inline variable
        result += "You earned ${totalVolumeCredits()} credits\n"
        return result
    }

    val statementData = StatementData()
    statementData.customer = invoice.customer
    statementData.performances = invoice.performances.map { enrichPerformance(it) }
    return renderPlainText(statementData, plays)
}





