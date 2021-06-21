package bean

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeService {
    @GET("symbols?access_key=${ExchangeApiConnector.API_KEY}")
    fun listSymbols(): Call<ResponseSymbols>

    @GET("latest?access_key=${ExchangeApiConnector.API_KEY}")
    fun getRates(@Query("symbols") symbols:String): Call<ResponseRates>
}