import bean.ExchangeService
import bean.ResponseRates
import bean.ResponseSymbols
import okhttp3.Request
import okio.Timeout
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import repository.ExchangeBusiness
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExchangeBusinessTest {

    companion object{
        object Service:ExchangeService{
            override fun listSymbols(): Call<ResponseSymbols> {

                val symbols = mapOf<String, String>("USD" to "United States Dollar")

                return object: Call<ResponseSymbols> {
                    override fun clone(): Call<ResponseSymbols> {
                        TODO("Not yet implemented")
                    }

                    override fun execute(): Response<ResponseSymbols> {
                        return Response.success(ResponseSymbols(true,  symbols))
                    }

                    override fun enqueue(callback: Callback<ResponseSymbols>) {
                        TODO("Not yet implemented")
                    }

                    override fun isExecuted(): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun cancel() {
                        TODO("Not yet implemented")
                    }

                    override fun isCanceled(): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun request(): Request {
                        TODO("Not yet implemented")
                    }

                    override fun timeout(): Timeout {
                        TODO("Not yet implemented")
                    }
                }
            }

            override fun getRates(symbols: String): Call<ResponseRates> {

                val rates = mapOf<String, Double>(
                    "EUR" to 1.0,
                    "USD" to 5.0,
                    "JPY" to 20.0,
                    "BRL" to 6.0
                )

                return object: Call<ResponseRates> {
                    override fun clone(): Call<ResponseRates> {
                        TODO("Not yet implemented")
                    }

                    override fun execute(): Response<ResponseRates> {
                        return Response.success(ResponseRates(true,"EUR",  rates))
                    }

                    override fun enqueue(callback: Callback<ResponseRates>) {
                        TODO("Not yet implemented")
                    }

                    override fun isExecuted(): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun cancel() {
                        TODO("Not yet implemented")
                    }

                    override fun isCanceled(): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun request(): Request {
                        TODO("Not yet implemented")
                    }

                    override fun timeout(): Timeout {
                        TODO("Not yet implemented")
                    }
                }
            }

        }
    }

    @Test
    fun `it should convert value`(){
        val business = ExchangeBusiness(Service)
        val exp = business.Convert(10.0, "BRL","JPY")
    }
}