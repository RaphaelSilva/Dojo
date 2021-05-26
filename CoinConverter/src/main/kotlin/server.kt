
import bean.ExchangeApiConnector
import bean.ExchangeService
import org.jetbrains.exposed.sql.Database


fun main() {
    val path = System.getProperty("user.dir")
    val db = Database.connect("jdbc:sqlite:$path/data.db", "org.sqlite.JDBC")
    val apiService = ExchangeApiConnector.Create(ExchangeService::class.java)
    App(db, apiService).routes().start()
}