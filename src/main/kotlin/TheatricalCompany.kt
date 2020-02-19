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
        var totalAmount = 0
        var volumeCredits = 0
        var result = "Statement for ${invoice.customer}\n"
        val format: (value: Double) -> String = {
            Money.of(CurrencyUnit.USD, it).toString()
        }

        for (perf in invoice.performances) {
            //add volume credits
            volumeCredits += max(perf.audience - 30, 0)
            //add extra credit for every ten comedy attendees
            if (playFor(perf)?.type == PlayType.COMEDY) {
                volumeCredits += floor(perf.audience.toDouble() / 5).toInt()
            }

            //print line for this order
            result += "${playFor(perf)?.name}: ${format((amountFor(perf).toDouble() / 100))} (${perf.audience} seats)\n"
            totalAmount += amountFor(perf)
        }
        result += "Amount owed is ${format(totalAmount.toDouble() / 100)}\n"
        result += "You earned ${volumeCredits} credits\n"
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
}



