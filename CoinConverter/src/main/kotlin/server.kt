import io.javalin.Javalin
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

fun main() {
    val app = Javalin.create().start(7000)
    app.get("/") { ctx -> ctx.result("Hello World") }
}