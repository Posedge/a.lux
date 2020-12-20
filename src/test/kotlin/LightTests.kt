import hue.HueClient
import hue.MOCK_HTTP_CLIENT
import service.LightService
import java.time.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class LightTests {

    private val mockHueClient = HueClient(MOCK_HTTP_CLIENT)
    private val testLightService = LightService(mockHueClient)

    @Test
    fun `can find configured groups`() {
        assertTrue(testLightService.groups.containsKey("Living Room"))
        val livingRoomService = testLightService.groups.getValue("Living Room")
        assertEquals("Living Room", livingRoomService.name)
        assertTrue(livingRoomService.hueId.isNotBlank())
        assertNotNull(livingRoomService.schedule)
        assertEquals(5, livingRoomService.schedule.size)
        assertEquals(LocalTime.of(7, 0), livingRoomService.schedule[0].time)
    }
}