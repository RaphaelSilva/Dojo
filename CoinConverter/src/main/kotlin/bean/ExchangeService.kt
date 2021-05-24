package bean

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ExchangeService {
    @GET("symbols?access_key=${ExchangeApiConnector.API_KEY}")
    fun listSymbols(): Call<ResponseSymbols>
}