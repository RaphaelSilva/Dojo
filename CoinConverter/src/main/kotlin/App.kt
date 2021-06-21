import bean.ExchangeApiConnector
import bean.ExchangeService
import controller.TransactionController
import entity.TransactionTable
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class App {
    private val db: Database
    private val app = Javalin.create()

    constructor(path: String, dbFileName: String) {
        val fileExists = File(path, dbFileName).exists()
        db = Database.connect("jdbc:sqlite:$path/$dbFileName", "org.sqlite.JDBC")
        if (!fileExists) {
            transaction(db) {
                SchemaUtils.create(TransactionTable)
            }
        }
    }

    fun routes(service: ExchangeService = ExchangeApiConnector.Create(ExchangeService::class.java)): App {
        app.routes {
            path("Transaction") {
                get(TransactionController(db, service)::listAll)
                path("convert/:value/:from/:to") {
                    get(TransactionController(db, service)::convert)
                }
            }
        }
        return this
    }

    fun start(host:String = "localhost", port: Int = 7000): Javalin {
        return app.start(host, port)
    }
}