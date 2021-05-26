import bean.ExchangeService
import controller.TransactionController
import entity.TransactionTable
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class App(private val db: Database, private val service: ExchangeService) {
    val app = Javalin.create()

    fun routes(): App {
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

    fun createDatabase() {
        transaction(db) {
            SchemaUtils.create(TransactionTable)
        }
    }

    fun start(): Javalin {
        return app.start(7000)
    }

}