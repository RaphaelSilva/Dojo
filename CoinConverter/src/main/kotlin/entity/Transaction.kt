package entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Transaction(id: EntityID<Int>) : TransactionData, IntEntity(id) {
    companion object : IntEntityClass<Transaction>(TransactionTable)
    override var user_id by TransactionTable.user_id
    override var coin_src by TransactionTable.coin_src
    override var coin_dest by TransactionTable.coin_dest
    override var value_src by TransactionTable.value_src
    override var value_dest by TransactionTable.value_dest
    override var rate: Double by TransactionTable.rate
    override var creationDate by TransactionTable.creationDate
}