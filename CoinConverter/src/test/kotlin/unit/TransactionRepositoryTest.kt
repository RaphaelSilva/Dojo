package unit

import entity.TransactionTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.TestInstance
import repository.TransactionRepository

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionRepositoryTest {
    val dbContext = Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
    val transactionRepository = TransactionRepository(dbContext)

    val user_id = UUID.randomUUID()
    val coin_src = "SRC"
    val coin_dest = "DEST"
    val value_src = BigDecimal.valueOf(5.5)
    val value_dest = BigDecimal.valueOf(6.6)
    val rate = 0.3
    val localdatetime = LocalDateTime.now()

    @Test
    fun `insert new transaction`() {
        transaction(dbContext) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(TransactionTable)

            val exp = transactionRepository.Persist(
                user_id, coin_src, coin_dest, value_src, value_dest, rate, localdatetime
            )

            assertEquals(exp.user_id, user_id)
            assertEquals(exp.coin_src, coin_src)
            assertEquals(exp.coin_dest, coin_dest)
            assertEquals(exp.value_src, value_src)
            assertEquals(exp.value_dest, value_dest)
            assertEquals(exp.rate, rate)
            assertEquals(exp.creationDate, localdatetime)
            assertNotNull(exp.id, "User Id st")
            assertEquals(exp.id.value, 1, "User Id st")
        }
    }

    @Test
    fun `get all transaction from user_id`() {
        transaction(dbContext) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(TransactionTable)
            transactionRepository.Persist(
                user_id, coin_src, coin_dest, value_src, value_dest, rate, localdatetime
            )
            transactionRepository.Persist(
                UUID.randomUUID(), coin_src, coin_dest, value_src, value_dest, rate, localdatetime
            )
            transactionRepository.Persist(
                user_id, coin_src, coin_dest, value_src, value_dest, rate, localdatetime
            )
            val transactions = transactionRepository.getAll(user_id)
            assertEquals(transactions.size, 2, "select only two transaction from $user_id")

        }
    }


}