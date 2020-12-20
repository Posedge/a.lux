import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import service.LightService
import java.lang.Exception
import java.time.LocalTime
import kotlin.system.exitProcess

object Config {

    private val resourcePath: String = "config.yml"
    private val logger = LoggerFactory.getLogger(javaClass)
    private val configMap = readYaml()

    val url = configMap.getValue("url") as String
    val apiKey = configMap.getValue("api_key") as String
    val refreshIntervalMillis = configMap.getOrDefault("refresh_interval", 1000) as Int
    val autoSceneName = configMap.getOrDefault("auto_scene_name", "Auto") as String
    val groups: List<Group> = (configMap.getValue("groups") as List<Map<String, Any>>).map { groupMap ->
        val name = groupMap.getValue("name") as String
        val schedule = (groupMap.getValue("schedule") as List<Map<String, Any>>).map { scheduleMap ->
            val time = LocalTime.parse(scheduleMap.getValue("time") as String)
            val scene = scheduleMap.getValue("scene") as String
            Keyframe(time, scene)
        }
        Group(name, schedule)
    }

    data class Group(
        val name: String,
        val schedule: List<Keyframe>,
    )

    data class Keyframe(
        val time: LocalTime,
        val scene: String
    )

    private fun readYaml(): Map<String, Any> {
        try {
            val configStream = LightService::class.java.classLoader.getResourceAsStream(resourcePath)
            return Yaml().load(configStream)
        } catch (e: Exception) {
            logger.error("Create a valid config.yml file and place it on the classpath " +
                    "(resources directory or current directory). See config.sample.yml", e)
            exitProcess(1)
        }
    }
}
