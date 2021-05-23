import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime
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

object TransactionTable : IntIdTable() {
    const val coin_length: Int = 5
    val user_id = uuid("user_id")
    val coin_src = varchar("coin_src", coin_length)
    val coin_dest = varchar("coin_dest", coin_length)
    val value_src = decimal("value_src", 5, 5)
    val value_dest = decimal("value_dest", 5, 5)
    val rate = float("rate")
    val creationDate = datetime("creationDate")
}

class Transaction(id: EntityID<Int>) : TransactionData, IntEntity(id) {
    companion object : IntEntityClass<Transaction>(TransactionTable)
    override var user_id by TransactionTable.user_id
    override var coin_src by TransactionTable.coin_src
    override var coin_dest by TransactionTable.coin_dest
    override var value_src by TransactionTable.value_src
    override var value_dest by TransactionTable.value_dest
    override var rate by TransactionTable.rate
    override var creationDate by TransactionTable.creationDate
}
