package unit

import bean.ResponseRates
import bean.ResponseSymbols
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Call
import bean.ExchangeService
import controller.TransactionController
import entity.TransactionTable
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import mocks.CallResponseMock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionControllerTest {

    private val dbContext = Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")

    companion object {
        object Service : ExchangeService {
            override fun listSymbols(): Call<ResponseSymbols> {
                val symbols = mapOf("USD" to "United States Dollar", "BRL" to "Brazil Real")
                val responseSymbols = ResponseSymbols(true, symbols)
                return CallResponseMock(responseSymbols)
            }

            override fun getRates(symbols: String): Call<ResponseRates> {
                val rates = mapOf(
                    "EUR" to 1.0, "USD" to 1.225363, "JPY" to 133.29986, "BRL" to 6.533881
                )
                val responseRates = ResponseRates(true, "EUR", rates)
                return CallResponseMock(responseRates)
            }

        }
    }

    private val ctx = mockk<Context>(relaxed = true)
    private val userId = "55619f6e-bdbd-11eb-8529-0242ac130003"
    private val coinSrc = "BRL"
    private val coinDest = "JPY"
    private val valueSrc = 10.0
    private val valueDest = 204.4

    @Test
    fun `it should create new and convert transaction`() {
        transaction(dbContext) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(TransactionTable)

            every { ctx.pathParam<Double>("value").value!! } returns valueSrc
            every { ctx.pathParam("from") } returns coinSrc
            every { ctx.pathParam("to") } returns coinDest
            every { ctx.queryParam("UserId") } returns userId

            val controller = TransactionController(dbContext, Service)
            val transaction = controller.convert(ctx)

            verify { ctx.json(transaction) }
        }
    }

    @Test
    fun `it should list all transaction from user id`() {
        transaction(dbContext) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(TransactionTable)

            for (i in 0..3){
                TransactionTable.insert {
                    it[user_id] = UUID.randomUUID()
                    it[coin_dest] = coinDest
                    it[coin_src] = coinSrc
                    it[value_dest] = BigDecimal.valueOf(valueDest)
                    it[value_src] = BigDecimal.valueOf(valueSrc)
                    it[creationDate] = LocalDateTime.now()
                    it[rate] = 5.5
                }
            }

            for (i in 0..9){
                TransactionTable.insert {
                    it[user_id] = UUID.fromString(userId)
                    it[coin_dest] = coinDest
                    it[coin_src] = coinSrc
                    it[value_dest] = BigDecimal.valueOf(valueDest)
                    it[value_src] = BigDecimal.valueOf(valueSrc)
                    it[creationDate] = LocalDateTime.now()
                    it[rate] = 5.5
                }
            }

            every { ctx.queryParam("UserId") } returns userId

            val controller = TransactionController(dbContext, Service)
            val transactions = controller.listAll(ctx)

            verify { ctx.json(transactions) }

            assertEquals(10, transactions.size)
        }
    }
}