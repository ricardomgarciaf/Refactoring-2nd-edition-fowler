import org.joda.money.CurrencyUnit
import org.joda.money.Money
import kotlin.math.floor
import kotlin.math.max

data class Performance(val playID: String, val audience: Int)

data class Invoice(val customer: String, val performances: List<Performance>)

data class Play(val name: String, val type: PlayType)

enum class PlayType { TRAGEDY, COMEDY }

class TheatricalCompany(val plays: Map<String, Play>) {

    fun statement(invoice: Invoice): String {

        //Step 9: Move declaration of the variable next to the loop
        //Step 10: Replace temp with query
        fun totalVolumeCredits(): Int {
            var volumeCredits = 0
            invoice.performances.forEach {
                volumeCredits += volumeCreditsFor(it)
            }
            return volumeCredits
        }

        var totalAmount = 0
        var result = "Statement for ${invoice.customer}\n"

        //Step 8: Split loop
        invoice.performances.forEach { perf ->
            //print line for this order
            result += "${playFor(perf)?.name}: ${usd(amountFor(perf).toDouble())} (${perf.audience} seats)\n"
        }

        //Step 12: Split loop
        invoice.performances.forEach { perf ->
            totalAmount += amountFor(perf)
        }

        //Step 11: Inline variable
        result += "Amount owed is ${usd(totalAmount.toDouble())}\n"
        result += "You earned ${totalVolumeCredits()} credits\n"
        return result
    }

    //Step 2: Decompose function
    //Step 4: Remove play parameter using playFor function
    //Step 5: thisAmount is never updated again which means that inline variable can be used
    private fun amountFor(aPerformance: Performance): Int {
        var result = 0
        when (playFor(aPerformance)?.type) {
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
            else -> throw Error("unknown type : ${playFor(aPerformance)?.type}")
        }
        return result
    }

    //Step 3: Replace temp with query (Remove temporary variables (if possible) replacing them by functions if they are read-only variables)
    private fun playFor(aPerformance: Performance): Play? {
        return plays[aPerformance.playID]
    }

    //Step 6: Move volumeCredits calculation to a function
    private fun volumeCreditsFor(aPerformance: Performance): Int {
        var result = 0
        result += max(aPerformance.audience - 30, 0)
        if (playFor(aPerformance)?.type == PlayType.COMEDY) {
            result += floor(aPerformance.audience.toDouble() / 5).toInt()
        }
        return result
    }

    //Step 7: Change function variable to declared function and rename for better understanding
    private fun usd(value: Double): String {
        return Money.of(CurrencyUnit.USD, value / 100).toString()
    }
}



