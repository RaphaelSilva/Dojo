import bean.ExchangeApiConnector
import bean.ExchangeService
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExchangeApiConnectorTest {

    val minimoSymvols = arrayOf("BRL", "USD", "EUR", "JPY")

    @Test
    fun `list at least 4 symbols`() {
        val apiService = ExchangeApiConnector.Create(ExchangeService::class.java)
        val resp = apiService.listSymbols().execute()
        val body = resp.body()
        val filtered = body?.symbols?.filter { s -> s.key in minimoSymvols }
        assertNotNull(body)
        assertNotNull(filtered)
        assertEquals(filtered.size, minimoSymvols.size)
    }
}