package controller

import Exchange
import bean.ExchangeService
import bean.TransactionResponse
import entity.Transaction
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import org.jetbrains.exposed.sql.Database
import repository.ExchangeBusiness
import repository.TransactionRepository
import java.time.LocalDateTime
import java.util.*

class TransactionController(dbContext: Database, apiService: ExchangeService) {

    val business = ExchangeBusiness(apiService)
    val transactionRepository = TransactionRepository(dbContext)

    private fun getUserId(ctx: Context): UUID {
        try {
            return UUID.fromString(ctx.queryParam("UserId"))
        } catch (e: IllegalArgumentException) {
            throw BadRequestResponse("User id is not right!!!")
        }
    }

    private fun businessConvert(value: Double, from: String, to: String): Exchange {
        try {
            return business.convert(value, from, to)
        } catch (e: Exception) {
            throw BadRequestResponse("Error to convert $value from $from to $to \n ${e.message}")
        }
    }

    private fun repositoryPersist(user_id: UUID, exchange: Exchange): Transaction {
        try {
            return transactionRepository.Persist(
                user_id, exchange.coin_src, exchange.coin_dest,
                exchange.value_src, exchange.value_dest, exchange.rate,
                LocalDateTime.now()
            )
        } catch (e: Exception) {
            throw BadRequestResponse("Error to persist!!!")
        }
    }

    fun convert(ctx: Context): TransactionResponse {
        val value = ctx.pathParam<Double>("value").value!!
        val from = ctx.pathParam("from")
        val to = ctx.pathParam("to")
        val user_id = getUserId(ctx)
        val exchange = businessConvert(value, from, to)
        val transaction = repositoryPersist(user_id, exchange)

        val transactionResponse = TransactionResponse(
            transaction.id.value,
            transaction.user_id,
            transaction.coin_src,
            transaction.coin_dest,
            transaction.value_src,
            transaction.value_dest,
            transaction.rate,
            transaction.creationDate.toString()
        )

        ctx.json(transactionResponse)
        return transactionResponse
    }

    fun listAll(ctx: Context): List<Transaction> {
        val user_id = UUID.fromString(ctx.queryParam("UserId"))

        val transactions = transactionRepository.getAll(user_id)
        ctx.json(transactions)
        return transactions
    }


}