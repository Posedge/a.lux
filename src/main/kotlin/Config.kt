import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import java.lang.Exception
import kotlin.system.exitProcess

private val logger = LoggerFactory.getLogger("Root")

data class Config(
    val url: String,
    val apiKey: String,
    val refreshIntervalMillis: Int
)

private fun loadConfig(): Config {
    try {
        val configStream = LightService::class.java.classLoader.getResourceAsStream("config.yml")
        val configMap: Map<String, Any> = Yaml().load(configStream)
        return Config(
            configMap.getValue("url") as String,
            configMap.getValue("api_key") as String,
            configMap.getOrDefault("refresh_interval", 1000) as Int
        )
    } catch (e: Exception) {
        logger.error("Create a valid config.yml file (see config.yml.sample)", e)
        exitProcess(1)
    }
}

val CONFIG: Config = loadConfig();