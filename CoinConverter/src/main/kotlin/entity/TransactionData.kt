package entity

import org.jetbrains.exposed.dao.id.EntityID
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

interface TransactionData {
    val id: EntityID<Int>
    val user_id: UUID
    val coin_src: String
    val coin_dest: String
    val value_src: BigDecimal
    val value_dest: BigDecimal
    val rate: Float
    val creationDate: LocalDateTime
}