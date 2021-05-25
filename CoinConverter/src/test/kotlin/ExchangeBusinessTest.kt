import bean.ExchangeService
import bean.ResponseRates
import bean.ResponseSymbols
import okhttp3.Request
import okio.Timeout
import repository.ExchangeBusiness
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeBusinessTest {

    companion object{
        object Service:ExchangeService{
            override fun listSymbols(): Call<ResponseSymbols> {
                val symbols = mapOf<String, String>("USD" to "United States Dollar")
                ResponseSymbols(true,  symbols)
                return object: Call<ResponseSymbols> {
                    override fun clone(): Call<ResponseSymbols> {
                        TODO("Not yet implemented")
                    }

                    override fun execute(): Response<ResponseSymbols> {
                        TODO("Not yet implemented")
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
                TODO("Not yet implemented")
            }

        }
    }

    fun `convert value`(){
        ExchangeBusiness(Service)
    }
}