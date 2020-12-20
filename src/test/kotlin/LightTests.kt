import hue.HueClient
import hue.MOCK_HTTP_CLIENT
import service.LightService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class LightTests {

    private val mockHueClient = HueClient(MOCK_HTTP_CLIENT)
    private val testLightService = LightService(mockHueClient)

    @Test
    fun `can find configured groups`() {
        assertTrue(testLightService.groups.containsKey("Living Room"))
        val livingRoom = testLightService.groups.getValue("Living Room")
        assertEquals("Living Room", livingRoom.name)
        assertTrue(livingRoom.hueId.isNotBlank())
    }
}