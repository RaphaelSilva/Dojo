package bean

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeApiConnector {
    companion object {
        const val API_KEY = "3fc87ec26fe14298253292e7dd439658"
        fun <T> Create(service: Class<T>): T {
            return Retrofit.Builder()
                .baseUrl("http://api.exchangeratesapi.io/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(service)
        }
    }
}