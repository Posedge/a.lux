package hue

import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import kotlin.test.Test

internal class HueClientTest {

    val logger = LoggerFactory.getLogger(javaClass)
    val testHueClient = HueClient(MOCK_HTTP_CLIENT)

    @Test
    fun `get light groups works`(): Unit = runBlocking {
        testHueClient.getLightGroups()
    }
}
