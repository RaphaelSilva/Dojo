package repository

import Exchange
import bean.ExchangeService
import java.math.BigDecimal

class ExchangeBusiness(private val service: ExchangeService) {

    fun convert(value: Double, from: String, to: String): Exchange {
        val resp = service.getRates("EUR,$from,$to").execute()
        val body = resp.body()
        val convert = (value / body?.rates?.get(from)!!) * body.rates.get(to)!!
        return Exchange(
            from, to,
            BigDecimal.valueOf(value),
            BigDecimal.valueOf(convert),
            convert / value
        )
    }
}