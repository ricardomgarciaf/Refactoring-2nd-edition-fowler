import org.joda.money.CurrencyUnit
import org.joda.money.Money
import kotlin.math.floor
import kotlin.math.max

data class Performance(val playID: String, val audience: Int)

data class Invoice(val customer: String, val performances: List<Performance>)

data class Play(val name: String, val type: PlayType)

enum class PlayType { TRAGEDY, COMEDY }

fun statement(invoice: Invoice, plays: Map<String, Play>): String {
    var totalAmount = 0
    var volumeCredits = 0
    var result = "Statement for ${invoice.customer}\n"
    val format: (value: Double) -> String = {
        Money.of(CurrencyUnit.USD,it).toString()
    }

    for (perf in invoice.performances) {
        val play = plays[perf.playID]

        var thisAmount = amountFor(play, perf)

        //add volume credits
        volumeCredits += max(perf.audience - 30, 0)
        //add extra credit for every ten comedy attendees
        if (play?.type == PlayType.COMEDY) {
            volumeCredits += floor(perf.audience.toDouble() / 5).toInt()
        }

        //print line for this order
        result += "${play?.name}: ${format((thisAmount.toDouble() / 100))} (${perf.audience} seats)\n"
        totalAmount += thisAmount
    }
    result += "Amount owed is ${format(totalAmount.toDouble() / 100)}\n"
    result += "You earned ${volumeCredits} credits\n"
    return result
}

//Step 2: Decompose function
private fun amountFor(play: Play?, aPerformance: Performance): Int {
    var result = 0
    when (play?.type) {
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
        else -> throw Error("unknown type : ${play?.type}")
    }
    return result
}

