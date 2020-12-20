package hue

import CONFIG
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class HueClient (private val http: HttpClient) {

    constructor() : this (DEFAULT_HTTP_CLIENT)

    suspend fun getLights(): Map<Int, Light> =
        http.get("${CONFIG.url}/api/${CONFIG.apiKey}/lights")

    suspend fun getLight(id: String): Light =
        http.get("${CONFIG.url}/api/${CONFIG.apiKey}/lights/$id")

    suspend fun getLightGroups(): Map<String, Group> =
        http.get<Map<String, Group>>("${CONFIG.url}/api/${CONFIG.apiKey}/groups")
            .filterValues { it.type in setOf("Room", "Zone") }

    suspend fun getScenes(): Map<String, GenericScene> =
        http.get("${CONFIG.url}/api/${CONFIG.apiKey}/scenes")

    suspend fun getScene(id: String): GroupScene =
        http.get("${CONFIG.url}/api/${CONFIG.apiKey}/scenes/$id")

    /**
     * Find the scenes that are named 'Auto' (the name is configurable).
     * Lights being in this state is the cue for a.lux to take over.
     */
    suspend fun getAutoScenes(): Map<String, GenericScene> =
        getScenes()
            .filterValues { it.name == CONFIG.autoSceneName }
            .ifEmpty { error("No scenes named '${CONFIG.autoSceneName}' found on the hue") }
}