
import bean.ExchangeApiConnector
import bean.ExchangeService
import entity.TransactionTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File


fun main() {
    val path = System.getProperty("user.dir")
    val databaseName = "data.db"
    val fileExists = File(path, databaseName).exists()
    val db = Database.connect("jdbc:sqlite:$path/$databaseName", "org.sqlite.JDBC")
    if(!fileExists){
        App.createDatabase(db)
    }
    val apiService = ExchangeApiConnector.Create(ExchangeService::class.java)
    App(db, apiService).routes().start()
}
