import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import java.lang.Exception
import kotlin.system.exitProcess

val logger = LoggerFactory.getLogger("Main")

fun main(args: Array<String>) {
    val config: Config
    try {
        config = loadConfig()
    } catch (e: Exception) {
        logger.error("Create a valid config.yml file (see config.yml.sample)", e)
        exitProcess(1)
    }
    ALux(config)
}

data class Config(
    val url: String,
    val apiKey: String,
)

private fun loadConfig(): Config {
    val yaml = Yaml()
    val configStream = ALux::class.java.classLoader.getResourceAsStream("config.yml")
    val configMap: Map<String, String> = yaml.load(configStream)
    return Config(configMap.getValue("url"), configMap.getValue("api_key"));
}

class ALux(val config: Config) {
    val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("hello, connecting to hue at ${config.url}")
    }
}
