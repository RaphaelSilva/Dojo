import java.math.BigDecimal

data class Exchange(val coin_src: String, val coin_dest: String, val value_src: BigDecimal, val value_dest: BigDecimal, val rate: Double)

