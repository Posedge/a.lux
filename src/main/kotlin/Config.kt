import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import service.LightService
import java.lang.Exception
import kotlin.system.exitProcess

private val logger = LoggerFactory.getLogger("Root")

data class Config(
    val url: String,
    val apiKey: String,
    val refreshIntervalMillis: Int,
    val autoSceneName: String,
    val groups: List<Group>,
) {
    data class Group(
        val name: String,
    )
}

private fun loadConfig(): Config {
    try {
        val configStream = LightService::class.java.classLoader.getResourceAsStream("config.yml")
        val configMap: Map<String, Any> = Yaml().load(configStream)
        return Config(
            configMap.getValue("url") as String,
            configMap.getValue("api_key") as String,
            configMap.getOrDefault("refresh_interval", 1000) as Int,
            configMap.getOrDefault("auto_scene_name", "Auto") as String,
            (configMap.getValue("groups") as List<Map<String, Any>>).map { groupMap ->
                Config.Group(groupMap.getValue("name") as String)
            },
        )
    } catch (e: Exception) {
        logger.error("Create a valid config.yml file and place it on the classpath " +
                "(resources directory or current directory). See config.sample.yml", e)
        exitProcess(1)
    }
}

val CONFIG: Config = loadConfig();