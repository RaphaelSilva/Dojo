import bean.ResponseRates
import bean.ResponseSymbols
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import repository.ExchangeBusiness
import retrofit2.Call
import bean.ExchangeService
import java.math.BigDecimal
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExchangeBusinessTest {

    companion object {
        object Service : ExchangeService {
            override fun listSymbols(): Call<ResponseSymbols> {
                val symbols = mapOf("USD" to "United States Dollar")
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

    @Test
    fun `it should convert value`() {
        val business = ExchangeBusiness(Service)
        val exp = business.convert(10.0, "BRL", "JPY")
        assertEquals(exp.coin_src, "BRL")
        assertEquals(exp.coin_dest, "JPY")
        assertEquals(exp.value_src, BigDecimal.valueOf(10.0))
        assertEquals(exp.value_dest,BigDecimal.valueOf( 204.01329623236174))
        assertEquals(exp.tax, 20.401329623236174)
    }
}