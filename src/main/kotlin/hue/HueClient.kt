package hue

import CONFIG
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.slf4j.LoggerFactory

class HueClient (private val http: HttpClient) {

    constructor() : this (DEFAULT_HTTP_CLIENT)

    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun getLights(): Map<Int, Light> =
        http.get("${CONFIG.url}/api/${CONFIG.apiKey}/lights")

    suspend fun getLight(id: String): Light =
        http.get("${CONFIG.url}/api/${CONFIG.apiKey}/lights/$id")

    suspend fun getLightGroups(): Map<String, Group> =
        http.get<Map<String, Group>>("${CONFIG.url}/api/${CONFIG.apiKey}/groups")
            .filterValues { it.type in setOf("Room", "Zone") }

    suspend fun getScenes(): Map<String, GenericScene> =
        http.get("${CONFIG.url}/api/${CONFIG.apiKey}/scenes")

    suspend fun getAutoScenes(): Map<String, GenericScene> =
        getScenes()
            .filterValues { it.name == CONFIG.autoSceneName }
            .ifEmpty {
                error("No scenes named '${CONFIG.autoSceneName}' found on the hue")
            }
}