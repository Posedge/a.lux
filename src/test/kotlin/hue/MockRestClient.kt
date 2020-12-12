package hue

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.headersOf

/**
 * Configure the HttpClient to return predefined responses for testing.
 */
val MOCK_HTTP_CLIENT = HttpClient(MockEngine) {
    install(JsonFeature) {
        serializer = JacksonSerializer() {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
    install(Logging) {
        level = LogLevel.INFO
    }
    engine {
        addHandler { setupRequestHandlers(it) }
    }
}

/**
 * Handler that returns the predefined mock responses.
 * The response bodies are read from the (test) resources and are taken from my hue bridge.
 */
private fun MockRequestHandleScope.setupRequestHandlers(request: HttpRequestData): HttpResponseData {
    val responseBodyFile = when {
        request.url.encodedPath.endsWith("/groups") -> "groups.json"
        request.url.encodedPath.endsWith("/scenes") -> "scenes.json"
        else -> error("Unhandled ${request.url}")
    }
    return this.respond(
        readResource(responseBodyFile),
        headers = headersOf("content-type", "application/json;coding=UTF-8"),
    )
}

private fun readResource(path: String): ByteArray {
    return HueClientTest::class.java.classLoader.getResource(path)?.readBytes()
        ?: throw RuntimeException("Resource not found: $path")
}