package hue

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class HueClient (private val http: HttpClient) {

    constructor() : this (DEFAULT_HTTP_CLIENT)

    suspend fun getLights(): Map<Int, Light> =
        http.get("${Config.url}/api/${Config.apiKey}/lights")

    suspend fun getLight(id: String): Light =
        http.get("${Config.url}/api/${Config.apiKey}/lights/$id")

    suspend fun getLightGroups(): Map<String, Group> =
        http.get<Map<String, Group>>("${Config.url}/api/${Config.apiKey}/groups")
            .filterValues { it.type in setOf("Room", "Zone") }

    suspend fun getScenes(): Map<String, GenericScene> =
        http.get("${Config.url}/api/${Config.apiKey}/scenes")

    suspend fun getScene(id: String): GroupScene =
        http.get("${Config.url}/api/${Config.apiKey}/scenes/$id")

    /**
     * Find the scenes that are named 'Auto' (the name is configurable).
     * Lights being in this state is the cue for a.lux to take over.
     */
    suspend fun getAutoScenes(): Map<String, GenericScene> =
        getScenes()
            .filterValues { it.name == Config.autoSceneName }
            .ifEmpty { error("No scenes named '${Config.autoSceneName}' found on the hue") }
}