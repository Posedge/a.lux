package hue

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging

val DEFAULT_HTTP_CLIENT = HttpClient(Apache) {
    install(JsonFeature) {
        serializer = JacksonSerializer() {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
    install(Logging) {
        level = LogLevel.INFO
    }
}
