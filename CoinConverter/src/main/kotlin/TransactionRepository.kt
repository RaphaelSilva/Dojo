import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class TransactionRepository constructor(dataContext: Database) {

    var db: Database = dataContext

    fun Persist(
        p_user_id: UUID,
        p_coin_src: String,
        p_coin_dest: String,
        p_value_src: BigDecimal,
        p_value_dest: BigDecimal,
        p_rate: Float,
        p_creationDate: LocalDateTime
    ): Transaction {
        return transaction(this.db) {
            return@transaction Transaction.new {
                user_id = p_user_id
                coin_src = p_coin_src
                coin_dest = p_coin_dest
                value_src = p_value_src
                value_dest = p_value_dest
                rate = p_rate
                creationDate = p_creationDate
            }
        }
    }

    fun getAll(user_id: UUID): List<Transaction> {
        return transaction(this.db) {
            return@transaction Transaction.all().filter { t -> t.user_id == user_id }.toList()
        }

    }
}

