import org.slf4j.LoggerFactory
import service.LightService

fun main(args: Array<String>) {
    LoggerFactory.getLogger("ROOT").info("A.lux is starting up. Configuration: $Config")
    LightService()
}