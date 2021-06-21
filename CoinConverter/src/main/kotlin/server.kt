
fun main() {
    val path = System.getProperty("user.dir")
    val dbFileName = "data.db"
    App(path, dbFileName).routes().start()
}
