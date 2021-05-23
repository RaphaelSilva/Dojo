import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

import java.util.*

object Transations : Table(){
    val id: Column<Int> = integer("id").autoIncrement()
    val user_id: Column<UUID> = uuid("user_id").uniqueIndex()
}
