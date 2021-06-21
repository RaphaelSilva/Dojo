package bean

import java.math.BigDecimal
import java.util.*

data class TransactionResponse (
    val id: Int,
    val user_id: UUID,
    val coin_src: String,
    val coin_dest: String,
    val value_src: BigDecimal,
    val value_dest: BigDecimal,
    val rate: Double,
    val creationDate: String
)
