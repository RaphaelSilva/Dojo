
import bean.ExchangeApiConnector
import bean.ExchangeService
import entity.TransactionTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File


fun main() {
    val path = System.getProperty("user.dir")
    val dbFileName = "data.db"
    App(path, dbFileName).routes().start()
}
