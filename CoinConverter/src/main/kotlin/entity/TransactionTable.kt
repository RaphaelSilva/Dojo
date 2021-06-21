package entity

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object TransactionTable : IntIdTable() {
    const val coin_length: Int = 5
    val user_id = uuid("user_id")
    val coin_src = varchar("coin_src", coin_length)
    val coin_dest = varchar("coin_dest", coin_length)
    val value_src = decimal("value_src", 5, 5)
    val value_dest = decimal("value_dest", 5, 5)
    val rate = double("rate")
    val creationDate = datetime("creationDate")
}


