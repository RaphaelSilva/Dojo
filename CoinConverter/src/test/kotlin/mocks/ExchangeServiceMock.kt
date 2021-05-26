package mocks

import bean.ExchangeService
import bean.ResponseRates
import bean.ResponseSymbols
import retrofit2.Call

class ExchangeServiceMock: ExchangeService {
    override fun listSymbols(): Call<ResponseSymbols> {
        val symbols = mapOf("USD" to "United States Dollar")
        val responseSymbols = ResponseSymbols(true, symbols)
        return CallResponseMock(responseSymbols)
    }

    override fun getRates(symbols: String): Call<ResponseRates> {
        val rates = mapOf(
            "EUR" to 1.0, "USD" to 5.0, "JPY" to 20.0, "BRL" to 6.0
        )
        val responseRates = ResponseRates(true, "EUR", rates)
        return CallResponseMock(responseRates)
    }

}