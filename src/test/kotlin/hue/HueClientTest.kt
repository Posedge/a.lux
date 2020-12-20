package hue

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class HueClientTest {

    private val testHueClient = HueClient(MOCK_HTTP_CLIENT)

    @Test
    fun `can find lights and their states`(): Unit = runBlocking {
        val lights = testHueClient.getLights()
        lights.values.forEach {
            assertTrue(it.state.ct != null || it.state.xy != null)
        }
        val light3 = testHueClient.getLight("3")
        assertTrue(light3.state.ct != null || light3.state.xy != null)
    }

    @Test
    fun `can find light groups`(): Unit = runBlocking {
        val groups = testHueClient.getLightGroups()
        assertTrue(groups.isNotEmpty())
        groups.values.forEach {
            assertTrue(it.lights.isNotEmpty())
            assertTrue(setOf("Room", "Zone").contains(it.type))
        }
    }

    @Test
    fun `can find scenes on the hue`(): Unit = runBlocking {
        val scenes = testHueClient.getScenes()
        assertTrue(scenes.isNotEmpty())
        scenes.values.forEach {
            if (it.type == "GroupScene") {
                assertNotNull(it.group)
            }
            assertTrue(it.lights.isNotEmpty())
        }
    }

    @Test
    fun `can find single scene with more detail`(): Unit = runBlocking {
        val scene = testHueClient.getScene("6vC8mVgejD9sGCY")
        assertTrue(scene.lights.isNotEmpty())
        assertTrue(scene.lightstates.isNotEmpty())
        scene.lightstates.forEach { (lightId, state) ->
            assertTrue(state.xy != null || state.ct != null)
        }
    }

    @Test
    fun `can find scenes designated as auto`(): Unit = runBlocking {
        val autoScenes = testHueClient.getAutoScenes()
        assertTrue(autoScenes.isNotEmpty())
        autoScenes.forEach { (id, scene) ->
            assertEquals("Auto", scene.name)
        }
    }
}
