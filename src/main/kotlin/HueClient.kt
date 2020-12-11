import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import org.slf4j.LoggerFactory

private val DEFAULT_HTTP_CLIENT = HttpClient(Apache) {
    install(JsonFeature) {
        serializer = JacksonSerializer() {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
    install(Logging) {
        level = LogLevel.INFO
    }
}

class HueClient (private val http: HttpClient) {

    constructor() : this (DEFAULT_HTTP_CLIENT)

    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun getRoomsAndZones(): Map<Int, Group> {
        return http.get<Map<Int, Group>>("${CONFIG.url}/api/${CONFIG.apiKey}/groups")
            .filterValues { it.type in setOf("Room", "Zone") }
    }

    suspend fun getLight(id: Int): Light {
        return http.get("${CONFIG.url}/api/${CONFIG.apiKey}/lights/$id")
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