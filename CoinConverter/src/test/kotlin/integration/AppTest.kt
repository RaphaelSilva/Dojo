package integration

import App
import bean.TransactionResponse
import com.google.gson.Gson
import io.javalin.Javalin
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.test.assertTrue


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppTest {

    companion object {
        val path = System.getProperty("user.dir")
        val dbFileName = "data.test.db"
        val app = App(path, dbFileName).routes().start()
        val host = app.server()?.serverHost
        val port = app.server()?.serverPort
        val baseUrl = "http://$host:$port"
        val userId = UUID.randomUUID().toString()

        @BeforeClass
        @JvmStatic
        fun setup() {
            println("############### setup ###################")
            println("Running to target -> $baseUrl")
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            println("############### tearDown ###################")
            app.stop()
        }
    }

    private fun convert(value: Double, from: String, to: String, user_id: String = userId) {
        val response: HttpResponse<String> =
            Unirest.get("$baseUrl/Transaction/convert/$value/$from/$to?UserId=$user_id").asString()
        val transactionResponse = Gson().fromJson(response.body, TransactionResponse::class.java)
        assertThat(transactionResponse.value_dest.toDouble()).isCloseTo(
            transactionResponse.value_src.toDouble() * transactionResponse.rate,
            Percentage.withPercentage(1.0)
        )
        assertThat(response.status).isEqualTo(200)
    }

    @Test @Order(1)
    fun `Convert from BRL to JPY`() {
        convert(15.5, "BRL", "JPY")
    }

    @Test @Order(2)
    fun `Convert from USD to EUR`() {
        convert(15.5, "USD", "EUR")
    }

    @Test @Order(3)
    fun `Convert from BRL to USD`() {
        convert(15.5, "BRL", "USD")
    }

    @Test @Order(4)
    fun `Convert from EUR to USD`() {
        convert(15.5, "EUR", "USD")
    }

    @Test @Order(5)
    fun `Convert from JPY to USD`() {
        convert(15.5, "JPY", "USD", UUID.randomUUID().toString())
    }

    @Test @Order(6)
    fun `list all transaction from user id`() {
        val response: HttpResponse<String> = Unirest.get("$baseUrl/Transaction?UserId=$userId").asString()
        val transactionsResponse = Gson().fromJson(response.body, Array<TransactionResponse>::class.java)
        assertThat(transactionsResponse).isNotEmpty
        assertThat(response.status).isEqualTo(200)
    }

    @Test @Order(7)
    fun `it should throw a bad request 400 in value`() {
        val response: HttpResponse<String> =
            Unirest.get("$baseUrl/Transaction/convert/v/bbb/ccc?UserId=$userId").asString()
        println(response.body)
        assertThat(response.status).isEqualTo(400)
    }

    @Test @Order(8)
    fun `it should throw a bad request 400 after value`() {
        val response: HttpResponse<String> =
            Unirest.get("$baseUrl/Transaction/convert/10.6/bbb/ccc?UserId=$userId").asString()
        println(response.body)
        assertThat(response.status).isEqualTo(400)
    }
}