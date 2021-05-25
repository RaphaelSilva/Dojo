package repository

import Exchange
import bean.ExchangeService
import java.math.BigDecimal

class ExchangeBusiness (service: ExchangeService) {
    val service = service

    fun Convert(value: Double, coin_src: String, coin_dest: String): Exchange {
        val resp = service.getRates("EUR,$coin_src,$coin_dest").execute()
        val body = resp.body()
        val convert = (value / body?.rates?.get(coin_src)!!) * body?.rates?.get(coin_dest)!!
        return Exchange(
            coin_src, coin_dest,
            BigDecimal.valueOf(value),
            BigDecimal.valueOf(convert),
            value / convert
        )
    }
}