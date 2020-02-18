import java.text.NumberFormat
import java.util.*
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
        NumberFormat.getCurrencyInstance(Locale.US).format(it)
    }

    for (perf in invoice.performances) {
        val play = plays[perf.playID]
        var thisAmount = 0

        when (play?.type) {
            PlayType.TRAGEDY -> {
                thisAmount = 40000
                if (perf.audience > 30) {
                    thisAmount += 1000 * (perf.audience - 30)
                }
            }
            PlayType.COMEDY -> {
                thisAmount = 30000
                if (perf.audience > 20) {
                    thisAmount += 10000 + 500 * (perf.audience - 20)
                }
                thisAmount += 300 * perf.audience
            }
            else -> throw Error("unknown type : ${play?.type}")
        }

        //add volume credits
        volumeCredits += max(perf.audience - 30, 0)
        //add extra credit for every ten comedy attendees
        if (play.type == PlayType.COMEDY) {
            volumeCredits += floor(perf.audience.toDouble() / 5).toInt()
        }

        //print line for this order
        result += "${play.name}: ${format((thisAmount.toDouble() / 100))} (${perf.audience} seats)\n"
        totalAmount += thisAmount
    }
    result += "Amount owed is ${format(totalAmount.toDouble() / 100)}\n"
    result += "You earned ${volumeCredits} credits\n"
    return result
}

