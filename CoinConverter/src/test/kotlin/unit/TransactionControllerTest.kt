package unit

import bean.ResponseRates
import bean.ResponseSymbols
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Call
import bean.ExchangeService
import controller.TransactionController
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import mocks.CallResponseMock
import org.jetbrains.exposed.sql.Database

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

    @Test
    fun `it should create new and convert transaction`() {
        every{ ctx.pathParam<Double>("value").value!! } returns 10.0
        every{ ctx.pathParam("from") } returns "BRL"
        every{ ctx.pathParam("to") } returns "JPY"
        every { ctx.queryParam("UserId") } returns "55619f6e-bdbd-11eb-8529-0242ac130003"

        val controller = TransactionController(dbContext, Service)
        controller.convert(ctx)

        verify { ctx.status(201) }
    }

    @Test
    fun `it should list all transaction from user id`() {
        every { ctx.queryParam("UserId") } returns "55619f6e-bdbd-11eb-8529-0242ac130003"

        val controller = TransactionController(dbContext, Service)
        controller.listAll(ctx)

        verify { ctx.status(201) }
    }
}