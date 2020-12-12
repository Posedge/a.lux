package hue

import CONFIG
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.slf4j.LoggerFactory

class HueClient (private val http: HttpClient) {

    constructor() : this (DEFAULT_HTTP_CLIENT)

    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun getLightGroups(): Map<Int, Group> =
        http.get<Map<Int, Group>>("${CONFIG.url}/api/${CONFIG.apiKey}/groups")
            .filterValues { it.type in setOf("Room", "Zone") }

    suspend fun getLight(id: Int): Light =
        http.get("${CONFIG.url}/api/${CONFIG.apiKey}/lights/$id")

    suspend fun getScenes(): Map<String, Scene> =
        http.get("${CONFIG.url}/api/${CONFIG.apiKey}/scenes")

    suspend fun getAutoScenes(): Map<String, Scene> {
        return getScenes()
            .filterValues { it.name == CONFIG.autoSceneName }
            .ifEmpty {
                error("No scenes named '${CONFIG.autoSceneName}' found on the hue")
            }
    }
}

data class Light(
    val state: LightState,
) {
    data class LightState (
        val on: Boolean,
        val bri: Int,
        val hue: Int,
        val sat: Int,
    )
}

/**
 * A group on the hue is a collection of lights. Among other things, this can be a room or a zone.
 */
data class Group(
    val lights: List<Int>,
    val type: String,
)

/**
 * A scene on the hue is a configuration of one or more lights.
 */
data class Scene(
    val lights: List<Int>,
    val name: String,
    val type: String,
    val group: String?,
)