import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import org.slf4j.LoggerFactory

class HueClient {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val http = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer() {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }
    }

    suspend fun getLight(): Light {
        return http.request {
            url("${CONFIG.url}/api/${CONFIG.apiKey}/lights/3")
            method = HttpMethod.Get
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
