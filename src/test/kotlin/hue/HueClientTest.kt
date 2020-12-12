package hue

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class HueClientTest {

    val testHueClient = HueClient(MOCK_HTTP_CLIENT)

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
    fun `can find scenes designated as auto`(): Unit = runBlocking {
        val autoScenes = testHueClient.getAutoScenes()
        assertTrue(autoScenes.isNotEmpty())
        autoScenes.forEach {
            val (id, scene) = it
            assertEquals("Auto", scene.name)
        }
    }
}
