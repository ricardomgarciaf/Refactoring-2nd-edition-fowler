import org.junit.Assert
import org.junit.Test

class TheatricalCompanyTest {

    private val plays = mapOf(
        "hamlet" to Play(name = "Hamlet", type = PlayType.TRAGEDY),
        "as-like" to Play(name = "As You Like It", type = PlayType.COMEDY),
        "othello" to Play(name = "Othello", type = PlayType.TRAGEDY)
    )

    private val invoice = Invoice(
        customer = "BigCo",
        performances = listOf(
            Performance(playID = "hamlet", audience = 55),
            Performance(playID = "as-like", audience = 35),
            Performance(playID = "othello", audience = 40)
        )
    )

    //Step 1: Add tests
    @Test
    fun testStatement(){
        val company = TheatricalCompany(plays)
        val result = company.statement(invoice)
        val expected = "Statement for BigCo\n" +
                "Hamlet: USD 650.00 (55 seats)\n" +
                "As You Like It: USD 580.00 (35 seats)\n" +
                "Othello: USD 500.00 (40 seats)\n" +
                "Amount owed is USD 1730.00\n" +
                "You earned 47 credits\n"
        Assert.assertEquals(expected, result)
    }
}