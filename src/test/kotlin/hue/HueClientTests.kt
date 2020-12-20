package hue

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class HueClientTests {

    private val testHueClient = HueClient(MOCK_HTTP_CLIENT)

    @Test
    fun `can find lights and their states`(): Unit = runBlocking {
        val lights = testHueClient.getLights()
        for (light in lights.values) {
            assertTrue(light.state.ct != null || light.state.xy != null)
        }
        val light3 = testHueClient.getLight("3")
        assertTrue(light3.state.ct != null || light3.state.xy != null)
    }

    @Test
    fun `can find light groups`(): Unit = runBlocking {
        val groups = testHueClient.getLightGroups()
        assertTrue(groups.isNotEmpty())
        for (group in groups.values) {
            assertTrue(group.lights.isNotEmpty())
            assertTrue(group.name.isNotBlank())
            assertTrue(setOf("Room", "Zone").contains(group.type))
        }
    }

    @Test
    fun `can find scenes on the hue`(): Unit = runBlocking {
        val scenes = testHueClient.getScenes()
        assertTrue(scenes.isNotEmpty())
        for (scene in scenes.values) {
            if (scene.type == "GroupScene") {
                assertNotNull(scene.group)
            }
            assertTrue(scene.lights.isNotEmpty())
        }
    }

    @Test
    fun `can find single scene with more detail`(): Unit = runBlocking {
        val scene = testHueClient.getScene("6vC8mVgejD9sGCY")
        assertTrue(scene.lights.isNotEmpty())
        assertTrue(scene.lightstates.isNotEmpty())
        for (state in scene.lightstates.values) {
            assertTrue(state.xy != null || state.ct != null)
        }
    }

    @Test
    fun `can find scenes designated as auto`(): Unit = runBlocking {
        val autoScenes = testHueClient.getAutoScenes()
        assertTrue(autoScenes.isNotEmpty())
        for (scene in autoScenes.values) {
            assertEquals("Auto", scene.name)
        }
    }
}
