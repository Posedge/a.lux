import org.yaml.snakeyaml.Yaml
import java.lang.Exception
import kotlin.system.exitProcess

data class Config(
    val url: String,
    val apiKey: String,
)

private fun loadConfig(): Config {
    try {
        val yaml = Yaml()
        val configStream = ALux::class.java.classLoader.getResourceAsStream("config.yml")
        val configMap: Map<String, String> = yaml.load(configStream)
        return Config(configMap.getValue("url"), configMap.getValue("api_key"));
    } catch (e: Exception) {
        logger.error("Create a valid config.yml file (see config.yml.sample)", e)
        exitProcess(1)
    }
}

val CONFIG: Config = loadConfig();